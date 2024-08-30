package org.iqmanager.controller;

import org.iqmanager.dto.CalculateDTO;
import org.iqmanager.dto.CalendarDTO;
import org.iqmanager.dto.OrderElemDTO;
import org.iqmanager.dto.PostDTO;
import org.iqmanager.models.Conditions;
import org.iqmanager.models.OrderElement;
import org.iqmanager.models.PerformerData;
import org.iqmanager.service.calendarService.CalendarService;
import org.iqmanager.service.extraService.ExtraService;
import org.iqmanager.service.orderElementService.OrderElementService;
import org.iqmanager.service.performerService.PerformerService;
import org.iqmanager.service.postService.PostService;
import org.iqmanager.service.userDataService.UserDataService;
import org.iqmanager.util.CurrencyConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.iqmanager.ApplicationC.URL_WEB;

@Validated
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = URL_WEB)
public class PostController {
    private final Logger logger = LoggerFactory.getLogger(PostController.class);

    private final OrderElementService orderElementService;

    private final PostService postService;

    private final UserDataService userDataService;

    private final ExtraService extraService;

    private final CalendarService calendarService;

    private final PerformerService performerService;

    @Autowired
    public PostController(PostService postService, UserDataService userDataService, ExtraService extraService, OrderElementService orderElementService, CalendarService calendarService, PerformerService performerService) {
        this.orderElementService = orderElementService;
        this.postService = postService;
        this.userDataService = userDataService;
        this.extraService = extraService;
        this.calendarService = calendarService;
        this.performerService = performerService;
    }

    @GetMapping({"/post/{id}"})
    public ResponseEntity<PostDTO> getPost(@PathVariable("id") long id, @RequestParam(value = "id_category", required = false, defaultValue = "-1") long idCategory, @RequestParam(value = "lg", required = false, defaultValue = "en") String language) {
        try {
            PostDTO post = this.postService.getPostDTO(id, idCategory, language);
            post.setViews(post.getViews() + 1L);
            this.postService.updatePostViews(id, post.getViews());
            return ResponseEntity.ok(post);
        } catch (Exception e) {
            e.printStackTrace();
            this.logger.warn("PostController -> getPost ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping({"/addF/{id}"})
    public ResponseEntity<String> addFavorites(@PathVariable("id") long id) {
        if (this.userDataService.hasUserLoginned())
            try {
                this.userDataService.addToFavorite(id);
                return ResponseEntity.ok("Successfully");
            } catch (Exception e) {
                e.printStackTrace();
                this.logger.warn("PostController -> addFavorites ERROR");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during add");
            }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user");
    }

    @PostMapping({"/calculatePrice"})
    public ResponseEntity<Long> calculatePrice(@RequestBody @Validated CalculateDTO calculateDTO) {
        try {
            long finalPrice = 0L;
            finalPrice += calculateDTO.getPrice() * calculateDTO.getFactor();
            finalPrice += Arrays.stream(calculateDTO.getExtras()).sum();
            return ResponseEntity.ok(finalPrice);
        } catch (Exception e) {
            e.printStackTrace();
            this.logger.warn("PostController -> calculatePrice ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0L);
        }
    }

    @PostMapping({"/addOrderEl"})
    public ResponseEntity<String> addOrderElements(@RequestBody @Validated OrderElemDTO orderElemDTO, @RequestParam(value = "distance", required = false) Double distance) {
        try {
            OrderElement orderElement = orderElemDTO.DTOtoOrder(orderElemDTO);
            orderElement.setDateOrder(Instant.now());
            orderElement.setPost(postService.getPost(orderElemDTO.getIdPost()));
            orderElement.setOrderedExtras(extraService.getExtras(orderElemDTO.getExtra()));
            orderElement.setOrderPrice(orderElementService.calculatePrice(orderElemDTO, orderElement));
            if (orderElemDTO.getAddress() != null)
                orderElement.setPriceDelivery(Math.round(orderElementService.calculateDelivery(orderElemDTO.getAddress(), orderElemDTO.getIdPost(), distance)));
            orderElement.setLeftToPay(orderElement.getOrderPrice() + orderElement.getPriceDelivery());
            if (userDataService.hasUserLoginned()) {
                userDataService.addToBasket(orderElement);
                return ResponseEntity.ok("Successfully");
            }
            long id = this.userDataService.addToBasketForUnregistered(orderElement);
            return ResponseEntity.ok("" + id);
        } catch (Exception e) {
            e.printStackTrace();
            this.logger.warn("PostController -> addOrderElements ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during add");
        }
    }

    @GetMapping({"/getCalendar"})
    public ResponseEntity<List<CalendarDTO>> getCalendar(@RequestParam("idPost") long idPost) {
        try {
            PerformerData performerData = postService.getPerformerByPostId(idPost);
            return ResponseEntity.ok(CalendarDTO.calendarToDTO(calendarService.getCalendar(performerData)));
        } catch (Exception e) {
            e.printStackTrace();
            this.logger.warn("PostController -> getCalendar ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping({"/convert"})
    public ResponseEntity<String> convert(@RequestParam("from") String from, @RequestParam("to") String to, @RequestParam("amount") long amount) {
        try {
            return ResponseEntity.ok(CurrencyConverter.convert(from, to, amount));
        } catch (Exception e) {
            e.printStackTrace();
            this.logger.warn("PostController -> convert ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping({"/getPhone"})
    public ResponseEntity<String> getPhone(@RequestParam("postId") long postId, @RequestParam("performerId") long performerId, @RequestParam(value = "returnUrl", required = false, defaultValue = "") String returnUrl, @RequestParam(value = "failUrl", required = false, defaultValue = "") String failUrl, @RequestParam(value = "orderId", required = false, defaultValue = "0") long orderId, @RequestParam(value = "currency", required = false, defaultValue = "RUB") String currency, @RequestParam(value = "sberPay", required = false, defaultValue = "false") boolean sberPay) {
        try {
            if (this.userDataService.hasUserLoginned()) {
                Conditions conditions = postService.getPost(postId).getConditions();
                if (conditions.isVisible())
                    return ResponseEntity.ok(performerService.getPerformerPhone(performerId));
                this.userDataService.addPurchasedNumbers(postService.getPost(postId));
                return ResponseEntity.ok(performerService.getPerformerPhone(performerId));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            e.printStackTrace();
            this.logger.warn("PostController -> getPerformerPhone ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}