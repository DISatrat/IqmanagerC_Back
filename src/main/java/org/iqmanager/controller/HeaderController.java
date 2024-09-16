package org.iqmanager.controller;

import org.iqmanager.dto.HeaderDTO;
import org.iqmanager.dto.PostListDTO;
import org.iqmanager.models.OrderElement;
import org.iqmanager.models.Post;
import org.iqmanager.models.RequestForm;
import org.iqmanager.models.UserData;
import org.iqmanager.models.enum_models.StatusOrder;
import org.iqmanager.service.fileService.FileService;
import org.iqmanager.service.postService.PostService;
import org.iqmanager.service.ratesService.requestFormService.RequestFormService;
import org.iqmanager.service.userDataService.UserDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.iqmanager.ApplicationC.URL_WEB;


@RestController
@Validated
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = URL_WEB)
public class HeaderController {

    private final Logger logger = LoggerFactory.getLogger(HeaderController.class);
    private final UserDataService userDataService;
    private final RequestFormService requestFormService;
    private final PostService postService;
    private final FileService fileService;

    @Value("${directory.path.image}")
    private String imageDirectoryPath;

    public HeaderController(UserDataService userDataService, RequestFormService requestFormService, PostService postService, FileService fileService) {
        this.userDataService = userDataService;
        this.requestFormService = requestFormService;
        this.postService = postService;
        this.fileService = fileService;
    }

    /**
     * Данные для хедера
     */
    @GetMapping("/header")
    public ResponseEntity<HeaderDTO> getUser() {
        if (userDataService.hasUserLoginned()) {
            try {
                UserData userData = userDataService.getUser(userDataService.getLoginnedAccount().getId());
                HeaderDTO headerDTO = new HeaderDTO(userData.getName(),
                        userData.isAgent(),
                        userData.getFavorites().stream().map(Post::getId).collect(Collectors.toList()),
                        userData.getOrderElements().stream()
                                .filter(x -> Objects.equals(x.getStatusOrder(), StatusOrder.WAITING_PERFORMER.toString()) || Objects.equals(x.getStatusOrder(), StatusOrder.WAITING_PAYMENT.toString()) || Objects.equals(x.getStatusOrder(), StatusOrder.WAITING_EXECUTION.toString()) || Objects.equals(x.getStatusOrder(), StatusOrder.ADVANCE_PAID.toString()))
                                .map(OrderElement::getId).collect(Collectors.toList()));
                return ResponseEntity.ok(headerDTO);
            } catch (Exception e) {
                e.printStackTrace();
                logger.warn("HeaderController -> getUser ERROR");
                return ResponseEntity.badRequest().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Форма заявки
     */
    @PostMapping(value = "/requestForm", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> saveTicket(@ModelAttribute RequestForm requestForm) {
        try {
            requestForm.setDateOrder(Instant.now());
            requestForm.setImage(fileService.upload(requestForm.getFile(), imageDirectoryPath));
            requestFormService.save(requestForm);
            return ResponseEntity.ok("Successfully");
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("HeaderController -> saveTicket ERROR");
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Поисковик
     */
    @GetMapping("/search")
    public ResponseEntity<List<PostListDTO>> searchBar(HttpServletResponse response,
                                                       @RequestParam(value = "s") String request, // Введенное слово
                                                       @RequestParam(value = "p") int page, //Номер страницы
                                                       @RequestParam(value = "q") int quantity, //Количество элементов на странице
                                                       @RequestParam(value = "min", required = false, defaultValue = "0") int min, // Минимальная цена
                                                       @RequestParam(value = "max", required = false, defaultValue = "10000000") int max, // Максимальнаяя ценв
                                                       @RequestParam(value = "stars", required = false, defaultValue = "0") int stars, // Минимальная оценка
                                                       @RequestParam(value = "pu", required = false, defaultValue = "false") boolean priceUp, // По возрастанию цены
                                                       @RequestParam(value = "pd", required = false, defaultValue = "false") boolean priceDown,// По убыванию цены
                                                       @RequestParam(value = "su", required = false, defaultValue = "false") boolean starsUp, // По возрастанию оценки
                                                       @RequestParam(value = "sd", required = false, defaultValue = "false") boolean starsDown // По убыванию оценки
    ) {
        try {
            List<PostListDTO> posts = postService.searchPostsByText(request);
            if (!posts.isEmpty()) {
                posts = posts.stream().filter(x -> x.getPrice() >= min && x.getPrice() <= max && x.getStars() >= stars).collect(Collectors.toList());
                if (priceUp)
                    posts = posts.stream().sorted((x1, x2) -> Math.toIntExact(x1.getPrice() - x2.getPrice())).collect(Collectors.toList());
                else if (priceDown)
                    posts = posts.stream().sorted((x1, x2) -> Math.toIntExact(x2.getPrice() - x1.getPrice())).collect(Collectors.toList());
                else if (starsUp)
                    posts = posts.stream().sorted((x1, x2) -> x1.getStars() - x2.getStars()).collect(Collectors.toList());
                else if (starsDown)
                    posts = posts.stream().sorted((x1, x2) -> x2.getStars() - x1.getStars()).collect(Collectors.toList());
                response.addHeader("QP", String.valueOf(posts.size()));
                return ResponseEntity.ok(posts.stream().skip((long) (page - 1) * quantity).limit(quantity).collect(Collectors.toList()));
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(Collections.emptyList());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("HeaderController -> searchBar ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }

    }
}
