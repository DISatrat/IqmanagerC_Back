package org.iqmanager.controller;

import org.iqmanager.models.SeoCategories;
import org.iqmanager.models.SeoPosts;
import org.iqmanager.service.seoService.SeoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.iqmanager.ApplicationC.URL_WEB;

@RestController
@RequestMapping("/api")
@Validated
@CrossOrigin(origins = URL_WEB)
public class SeoController {

    private final SeoService seoService;
    private final Logger logger = LoggerFactory.getLogger(SeoController.class);

    public SeoController(SeoService seoService) {
        this.seoService = seoService;
    }

    /** Получение seo для объявления */
    @GetMapping("/seoPost/{id}")
    public ResponseEntity<SeoPosts> getSeoPost(@PathVariable("id") long id) {
        try {
            return ResponseEntity.ok(seoService.getSeoPosts(id));
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("SeoController -> getSeoPost ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /** Получение seo для постов по категории */
    @GetMapping("/seoCategories/{id}")
    public ResponseEntity<SeoCategories> getSeoCategories(@PathVariable("id") long id) {
        try {
            return ResponseEntity.ok(seoService.getSeoCategories(id));
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("SeoController -> getSeoPost ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
