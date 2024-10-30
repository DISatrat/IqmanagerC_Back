package org.iqmanager.controller;

import org.iqmanager.dto.BasketDTO;
import org.iqmanager.dto.FeedbackDTO;
import org.iqmanager.dto.PurchasedNumbersDTO;
import org.iqmanager.models.OrderElement;
import org.iqmanager.models.Post;
import org.iqmanager.models.enum_models.StatusOrder;
import org.iqmanager.service.extraService.ExtraService;
import org.iqmanager.service.orderElementService.OrderElementService;
import org.iqmanager.service.postService.PostService;
import org.iqmanager.service.userDataService.UserDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.iqmanager.ApplicationC.URL_WEB;

@RestController
@RequestMapping("/api")
@Validated
@CrossOrigin(origins = URL_WEB)
@Transactional
public class BasketController {

    private final Logger logger = LoggerFactory.getLogger(BasketController.class);
    private final PostService postService;
    private final ExtraService extraService;
    private final OrderElementService orderElementService;
    private final UserDataService userDataService;

    public BasketController(PostService postService, ExtraService extraService, OrderElementService orderElementService, UserDataService userDataService) {
        this.postService = postService;
        this.extraService = extraService;
        this.orderElementService = orderElementService;
        this.userDataService = userDataService;
    }

    /**
     * Записи из корзины
     */
    @GetMapping("/basket")
    public ResponseEntity<Map<String, Object>> getBasket(@RequestBody(required = false) long[] idArr,
                                                         @RequestParam(required = false) List<String> filter,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        try {
            if (userDataService.hasUserLoginned()) {
                Pageable pageable = PageRequest.of(page, size);
                Page<BasketDTO> basketPage;

                if (filter != null) {
                    basketPage = userDataService.getFilteredBasket(filter, pageable);
                } else {
                    basketPage = userDataService.getBasket(pageable);
                }
                Map<String, Object> response = new LinkedHashMap<>();
                response.put("basket", basketPage.getContent());
                response.put("size", basketPage.getTotalElements());
                return ResponseEntity.ok(response);
            } else {
                List<BasketDTO> basket = new ArrayList<>();
                for (long id : idArr) {
                    basket.add(orderElementService.getBasketDTO(id));
                }
                return ResponseEntity.ok(Collections.singletonMap("basket", basket));
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("BasketController -> getBasket ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyMap());
        }
    }

    // оставить отзыв
    // Principal principal
    @PostMapping("/giveFeedback")
    public ResponseEntity<?> addFeedback(@RequestBody FeedbackDTO feedbackDTO) {
        try {
            HashMap<String,String> map = new HashMap<>();

            if (userDataService.hasUserLoginned()) {

//                String username = "admin";
                String username = userDataService.getLoginnedAccount().getName();
                OrderElement order = orderElementService.getOrderElement(feedbackDTO.getOrderId());

                if (Objects.equals(order.getStatusOrder(), "EXECUTED")) {

                    Post post = order.getPost();
                    order.setStatusOrder(String.valueOf(StatusOrder.EXECUTED_REVIEWED));
                    postService.addNewFeedback(post.getId(), username, feedbackDTO.getText(), feedbackDTO.getStars());
                    map.put("status", "200");
                    return ResponseEntity.ok().body(map);
                } else {
                    map.put("status", "400");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
                }
            } else {
                map.put("status", "401");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            HashMap<String,String> map = new HashMap<>();
            map.put("status", "404");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
        }
    }


    /**
     * Удалить запись из корзины
     */
    @DeleteMapping("/delElOr")
    public ResponseEntity<String> deleteOrderElement(@RequestBody long[] idArr) {
        try {
            for (long id : idArr) {
                orderElementService.deleteOrderElem(id);
            }
            return ResponseEntity.ok().body("Successfully");
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("BasketController -> deleteOrderElement ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during removal");
        }
    }


    @Transactional
    @PostMapping("/addOrderElArr")
    public ResponseEntity<String> addOrderElementsArr(@RequestBody @Validated long[] orderElemList) {
        try {
            if (userDataService.hasUserLoginned()) {
                for (long id : orderElemList) {
                    userDataService.addToBasket(orderElementService.getOrderElement(id));
                }
                return ResponseEntity.ok("Successfully");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("BasketController -> addOrderElementsArr ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during add");
        }
    }

    @GetMapping("/getPurchasedPhone")
    public ResponseEntity<List<PurchasedNumbersDTO>> getPurchasedPhone() {
        try {
            if (userDataService.hasUserLoginned()) {
                return ResponseEntity.ok(userDataService.getPurchaseNumbers());
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("BasketController -> getPurchasedPhone ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}












