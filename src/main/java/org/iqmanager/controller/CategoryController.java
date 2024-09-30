package org.iqmanager.controller;


import org.iqmanager.dto.CategoryDTO;
import org.iqmanager.dto.PostListDTO;
import org.iqmanager.models.Category;
import org.iqmanager.service.categoryService.CategoryService;
import org.iqmanager.service.postService.PostService;
import org.iqmanager.service.userDataService.UserDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.iqmanager.ApplicationC.URL_WEB;

@Validated
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = URL_WEB)
public class CategoryController {

    private final Logger logger = LoggerFactory.getLogger(BasketController.class);
    private final CategoryService categoryService;
    private final UserDataService userDataService;
    private final PostService postService;

    @Autowired
    public CategoryController(CategoryService categoryService, UserDataService userDataService, PostService postService) {
        this.categoryService = categoryService;
        this.userDataService = userDataService;
        this.postService = postService;
    }

    /**
     * Начальная страница, получаем список категория с id_parent = 0
     */
    @GetMapping("/")
    public ResponseEntity<List<CategoryDTO>> index(@RequestParam(value = "lg", defaultValue = "ru") String language) {
        try {
            List<CategoryDTO> categories = categoryService.findList(0L, language);
            if (categories.size() >= 1) {
                return ResponseEntity.ok(categories);
            } else {
                return ResponseEntity.noContent().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("CategoryController -> index ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }


    @GetMapping("/allCategory")
    public ResponseEntity<List<CategoryDTO>> getAllCategory(@RequestParam(value = "lg", defaultValue = "ru")
                                                            String language) {
        try {
            return ResponseEntity.ok(categoryService.findAll(language));
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("CategoryController -> allCategory ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    //проверка
    @GetMapping("/postIdsByCategory/{catId}")
    public ResponseEntity<ArrayList<Long>> getAllPosts(@PathVariable("catId") long categoryId) {
        try {
            return ResponseEntity.ok(postService.getAllPostIdsByCategoryId(categoryId));
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("CategoryController -> allCategory ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    //проверка
    @GetMapping("/allPostIds")
    public ResponseEntity<ArrayList<Long>> getAllPostIds() {
        try {
            return ResponseEntity.ok(postService.getAllPostIds());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("CategoryController -> allCategory ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Листаем категории
     */
    @GetMapping("/category/{id}")
    public ResponseEntity<List<CategoryDTO>> getCategory(HttpServletResponse response,
                                                         @PathVariable("id") long parentId,
                                                         @RequestParam(value = "lg", defaultValue = "ru") String language) {
        try {
            List<CategoryDTO> categories = categoryService.findList(parentId, language);
            if (!categories.isEmpty()) {
                response.addHeader("category", Base64.getEncoder().encodeToString(categoryService.getNameCategory(parentId, language).getBytes(StandardCharsets.UTF_8)));
                return ResponseEntity.ok(categories);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Collections.emptyList());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("CategoryController -> getCategory ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    @GetMapping("/history/{id}")
    public ResponseEntity<String[][]> getHistory(@PathVariable("id") long parentId,
                                                 @RequestParam(value = "lg", defaultValue = "ru") String language) {
        try {

            return ResponseEntity.ok(categoryService.getHistory(parentId, language));
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("CategoryController -> getCategory ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //проверка
    @GetMapping("/getAllPost")
    public ResponseEntity<List<PostListDTO>> getAllPosts(HttpServletResponse response,
                                                         @RequestParam(value = "id") long idCategory,
                                                         @RequestParam(value = "lg", defaultValue = "ru") String language) {
        try {
            Category category = categoryService.find(idCategory);
            List<PostListDTO> posts = postService.getListPosts(category);
            if (!posts.isEmpty()) {
                response.addHeader("category", Base64.getEncoder().encodeToString(categoryService.getNameCategory(category.getId(), language).getBytes(StandardCharsets.UTF_8)));
                return ResponseEntity.status(HttpStatus.OK).body(posts);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Collections.emptyList());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("CategoryController -> getPosts ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }


    /**
     * Получаем список объявлений по индексу категории
     */
    @GetMapping("/posts")
    public ResponseEntity<List<PostListDTO>> getPosts(HttpServletResponse response,
                                                      @RequestParam(value = "id") long idCategory, // id категории
                                                      @RequestParam(value = "lg", defaultValue = "ru") String language,
                                                      @RequestParam(value = "c") String country, //Страна
                                                      @RequestParam(value = "r") String region, //Регион
                                                      @RequestParam(value = "p") int page, //Номер страницы
                                                      @RequestParam(value = "q") int quantity, //Количество элементов на странице
                                                      @RequestParam(value = "min", required = false, defaultValue = "0") long min, // min price
                                                      @RequestParam(value = "max", required = false, defaultValue = "10000000") long max, // max price
                                                      @RequestParam(value = "s", required = false, defaultValue = "0") byte stars, // Минимальная оценка
                                                      @RequestParam(value = "pu", required = false, defaultValue = "false") boolean priceUp, // По возрастанию цены
                                                      @RequestParam(value = "pd", required = false, defaultValue = "false") boolean priceDown,// По убыванию цены
                                                      @RequestParam(value = "su", required = false, defaultValue = "false") boolean starsUp, // По возрастанию оценки
                                                      @RequestParam(value = "sd", required = false, defaultValue = "false") boolean starsDown // По убыванию оценки
    ) {
        try {
            List<PostListDTO> posts = null;
            Category category = categoryService.find(idCategory);
            if (!priceDown && !priceUp && !starsUp && !starsDown) {
                posts = postService.getPostsPagination(idCategory, country, region, stars, (byte) 5, min, max, page, quantity);
            } else if (priceDown) {
                posts = postService.getPostsPaginationOrderByPriceDesc(category, country, region, stars, (byte) 5, min, max, PageRequest.of(page, quantity));
            } else if (priceUp) {
                posts = postService.getPostsPaginationOrderByPriceAsc(category, country, region, stars, (byte) 5, min, max, PageRequest.of(page, quantity));
            }
            else if (starsDown) {
                posts = postService.getPostsPaginationOrderByStarsDesc(category, country, region, stars, (byte) 5,min, max, PageRequest.of(page, quantity));
            }
            else if (starsUp) {
                posts = postService.getPostsPaginationOrderByStarsAsc(category, country, region, stars, (byte) 5, min, max, PageRequest.of(page, quantity));
            }

            if (!posts.isEmpty()) {
                response.addHeader("category", Base64.getEncoder().encodeToString(categoryService.getNameCategory(category.getId(), language).getBytes(StandardCharsets.UTF_8)));
                return ResponseEntity.status(HttpStatus.OK).body(posts);
            } else {
                response.addHeader("category", Base64.getEncoder().encodeToString(categoryService.getNameCategory(category.getId(), language).getBytes(StandardCharsets.UTF_8)));
                return ResponseEntity.status(HttpStatus.OK).body(Collections.emptyList());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("CategoryController -> getPosts ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    /**
     * Количество постов
     */
    //проверка
    @GetMapping("/getQuantityPosts")
    public ResponseEntity<Long> getQuantityPage(@RequestParam(value = "id") long idCategory, // id категории
                                                @RequestParam(value = "c") String country, // Страна
                                                @RequestParam(value = "r") String region, // Регион
                                                @RequestParam(value = "min", required = false, defaultValue = "0") long min, // min price
                                                @RequestParam(value = "max", required = false, defaultValue = "10000000") long max, //max price
                                                @RequestParam(value = "s", required = false, defaultValue = "0") byte stars // Минимальная оценка
    ) {
        try {
            return ResponseEntity.ok(postService.getQuantityPosts(idCategory, country, region, min, max, stars, (byte) 5));
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("CategoryController -> getPosts ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0L);
        }
    }

    /**
     * Добавить в избранное
     */
    @PatchMapping("/favorite/{id}")
    public ResponseEntity<String> addToFavorite(@PathVariable("id") long id_post) {
        try {
            userDataService.addToFavorite(id_post);
            return ResponseEntity.ok().body("Successfully");
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("CategoryController -> addToFavorite ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Add to favorites error");
        }
    }
}
