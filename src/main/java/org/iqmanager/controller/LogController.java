package org.iqmanager.controller;

import org.iqmanager.dto.UserAuthDataDTO;
import org.iqmanager.models.location.City;
import org.iqmanager.models.location.Country;
import org.iqmanager.models.location.Region;
import org.iqmanager.service.locationService.LocationService;
import org.iqmanager.service.userDataService.UserDataService;
import org.iqmanager.util.CodeGenerator;
import org.iqmanager.util.SendSMS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static org.iqmanager.ApplicationC.URL_WEB;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = URL_WEB)
@Validated
public class LogController {

    private final Logger logger = LoggerFactory.getLogger(LogController.class);
    private final UserDataService userDataService;
    private final LocationService locationService;

    @Autowired
    public LogController(UserDataService userDataService, LocationService locationService) {
        this.userDataService = userDataService;
        this.locationService = locationService;
    }

    private static HashMap<String, String> codes = new HashMap<>();

    /** Верифицировать номер */
    @GetMapping("/getCode")
    public ResponseEntity<String> getCode(@RequestParam("p") String phone) {
        try {
            //todo убрать заглушку
            String[] phones = {"+79999999999","+79051111111","+79052222222","+79053333333","+79054444444","+79055555555","+79056666666"};
            for(String p : phones) {
                if(p.equals(phone)) {
//                    String encodedPhone = UriUtils.encode(phone, "UTF-8");
                    codes.put(p, "0000");

                    System.out.println("0000");

                    return ResponseEntity.ok().build();
                }
            }
            //todo
            if (userDataService.userNotExists(phone)) {
                String code = CodeGenerator.generate();
                SendSMS.sendAuthorization(phone, code);
                codes.put(phone, code);
                System.out.println(code);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error verification");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("LogController -> getCode ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error getCode");
        }
    }

    /** Регистрация Заказчика */
    @PostMapping("/register")
    public ResponseEntity<String> registerCustomer(@Validated @RequestBody UserAuthDataDTO userAuthDataDTO) {
        try {
            if(codes.containsKey(userAuthDataDTO.getUsername())){
                if(Objects.equals(codes.get(userAuthDataDTO.getUsername()), userAuthDataDTO.getCode())){
                    userDataService.register(userAuthDataDTO.getUsername(), userAuthDataDTO.getPassword());
                    codes.remove(userAuthDataDTO.getUsername());
                    return ResponseEntity.ok("Successfully");
                } else {
                    System.out.println(1111);
                    System.out.println(codes);
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }
            } else {
                logger.error("User {} not found in verification codes", userAuthDataDTO.getUsername());
                System.out.println(codes);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("LogController -> register ERROR");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error registration");
        }
    }


    /** Выбор страны */
    @GetMapping("/choiceCountry/{c}")
    public ResponseEntity<List<Country>> choiceCountry(@PathVariable("c") String country) {
        return ResponseEntity.ok(locationService.findCountry(country+"%"));
    }

    /** Выбор региона */
    @GetMapping("/choiceRegion/{c}/{r}")
    public ResponseEntity<List<Region>> choiceRegion(@PathVariable("c") long country, @PathVariable("r") String region) {
        return ResponseEntity.ok(locationService.findRegion(country, region+'%'));
    }

    /** Выбор города */
    @GetMapping("/choiceCity/{c}/{r}")
    public ResponseEntity<List<City>> choiceCity(@PathVariable("c") long region, @PathVariable("r") String city) {
        return ResponseEntity.ok(locationService.findCity(region, city+'%'));
    }


    /** Сброс пароля */
    @PostMapping("/passwordReset")
    public ResponseEntity<String> passwordReset(@RequestBody @Validated UserAuthDataDTO userAuthDataDTO) {
        try {
            if(codes.containsKey(userAuthDataDTO.getUsername())){
                if(Objects.equals(codes.get(userAuthDataDTO.getUsername()), userAuthDataDTO.getCode())){
                    userDataService.passwordReset(userAuthDataDTO.getUsername(), userAuthDataDTO.getPassword());
                    codes.remove(userAuthDataDTO.getUsername());
                    return ResponseEntity.ok("Successfully");
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("LogController -> passwordReset ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error passwordReset");
        }
    }
}
