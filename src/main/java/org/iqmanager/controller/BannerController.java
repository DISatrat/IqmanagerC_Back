package org.iqmanager.controller;

import org.iqmanager.dto.BannerToShowDTO;
import org.iqmanager.service.bannerService.BannerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.iqmanager.ApplicationC.URL_WEB;

@RestController
@RequestMapping("/api")
@Validated
@CrossOrigin(origins = URL_WEB)
public class BannerController {
    private static final Logger logger = LoggerFactory.getLogger(BannerController.class);
    private final BannerService bannerService;

    public BannerController(BannerService bannerService) {
        this.bannerService = bannerService;
    }

    @GetMapping("/getAllBanners")
    public ResponseEntity<?> getAllBanners() {
        try {
            List<BannerToShowDTO> banners = bannerService.getAllBanners();
            return ResponseEntity.ok(banners);
        } catch (Exception e) {
            logger.error("Error in getAllBanners: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

