package org.iqmanager.controller;


import org.iqmanager.dto.BannerToShowDTO;
import org.iqmanager.dto.PromoDTO;
import org.iqmanager.service.bannerService.BannerService;
import org.iqmanager.service.promoService.PromoService;
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
public class PromoController {

    private static final Logger logger = LoggerFactory.getLogger(PromoController.class);
    private final PromoService promoService;

    public PromoController(PromoService promoService) {
        this.promoService = promoService;
    }
    @GetMapping("/getAllPromo")
    public ResponseEntity<?> getAllPromo() {
        try {
            List<PromoDTO> promos = promoService.getAllPromo();
            return ResponseEntity.ok(promos);
        } catch (Exception e) {
            logger.error("Error in getAllPromo: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/getPromoById/{id}")
    public ResponseEntity<?> getPromoById(@PathVariable Long id) {
        try {
            return promoService.getPromoById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("Error in getPromoById: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
