package org.iqmanager.controller;

import org.iqmanager.dto.UserDataDTO;
import org.iqmanager.models.UserData;
import org.iqmanager.service.userDataService.UserDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.iqmanager.ApplicationC.URL_WEB;

@RestController
@Validated
@RequestMapping(value = "/api",produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = URL_WEB)
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserDataService userDataService;

    public UserController(UserDataService userDataService) {
        this.userDataService = userDataService;
    }

    @GetMapping("/user")
    public ResponseEntity<UserData> user() {
        try {
            return ResponseEntity.ok(userDataService.getLoginnedAccount());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("UserController -> user ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /** Добавление данных о пользователе */
    @PostMapping("/addData")
    public ResponseEntity<String> addData(@RequestBody UserDataDTO userDataDTO) {
        try {
            UserData userData = userDataService.getLoginnedAccount();
            userData.setName(userDataDTO.getName());
            userData.setLastName(userDataDTO.getLastName());
            userData.setEmail(userDataDTO.getEmail());
            userData.setWeb(userDataDTO.getWeb());
            userData.setCountry(userDataDTO.getCountry());
            userData.setRegion(userDataDTO.getRegion());
            userData.setCity(userDataDTO.getCity());
            userDataService.save(userData);
            return ResponseEntity.ok("Successfully");
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("UserController -> addData ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error addData");
        }
    }

}
