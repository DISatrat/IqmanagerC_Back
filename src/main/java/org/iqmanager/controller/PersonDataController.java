package org.iqmanager.controller;


import org.iqmanager.models.UserData;
import org.iqmanager.service.userDataService.UserDataService;
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
public class PersonDataController {

    private final Logger logger = LoggerFactory.getLogger(PersonDataController.class);
    private final UserDataService userDataService;

    public PersonDataController(UserDataService userDataService) {
        this.userDataService = userDataService;
    }

    /** Изменить личные данные */
    @PatchMapping("/editData")
    public ResponseEntity<String> editData(@Validated @RequestBody UserData userData) {
        try {
            userDataService.save(userData);
            if(userDataService.getUser(userData.getId()).equals(userData)) {
                return ResponseEntity.ok("Successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error edit");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("PersonDataController -> editData ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error edit");
        }
    }

//    /** История заказов */
//    @GetMapping("/orders")
//    public ResponseEntity<List<Order>> getOrders() {
//
//    }

}
