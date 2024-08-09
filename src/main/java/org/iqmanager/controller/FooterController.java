package org.iqmanager.controller;

import org.iqmanager.models.PhotoReport;
import org.iqmanager.models.Poster;
import org.iqmanager.service.footerService.FooterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.iqmanager.ApplicationC.URL_WEB;

@Validated
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = URL_WEB)
public class FooterController {
    private final Logger logger = LoggerFactory.getLogger(FooterController.class);
    private final FooterService footerService;

    public FooterController(FooterService footerService) {
        this.footerService = footerService;
    }

    @GetMapping("/poster")
    public ResponseEntity<List<Poster>> getPoster() {
        try {
            return ResponseEntity.ok(footerService.getPosters());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("FooterController -> getPoster ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/photoReport")
    public ResponseEntity<List<PhotoReport>> photoReport() {
        try {
            return ResponseEntity.ok(footerService.getPhotoReports());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("FooterController -> photoReport ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
