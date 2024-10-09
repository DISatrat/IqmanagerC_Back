package org.iqmanager.controller;

import org.iqmanager.dto.PaymentDTO;
import org.iqmanager.models.Payment;
import org.iqmanager.models.enum_models.PaymentStatus;
import org.iqmanager.service.paymentService.PaymentService;
import org.iqmanager.service.userDataService.UserDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.iqmanager.ApplicationC.URL_WEB;



@RestController
@RequestMapping("/api")
@Validated
@CrossOrigin(origins = URL_WEB)
public class PaymentController {
    private final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    private final PaymentService paymentService;
    private final UserDataService userDataService;
    public PaymentController(PaymentService paymentService, UserDataService userDataService) {
        this.paymentService = paymentService;
        this.userDataService = userDataService;
    }
    @PostMapping("/addPayment")
    public ResponseEntity<?> addPayment(@RequestBody PaymentDTO paymentDTO) {
        try {
            if (userDataService.hasUserLoginned()) {
                Payment payment = paymentService.addPayment(paymentDTO);
                if (payment != null) {
                    return ResponseEntity.ok(payment);
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OrderElement with ID " + paymentDTO.getOrderElementId() + " does not exist.");
                }
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("PaymentController -> addPayment ERROR: ");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/getPaymentStatus/{orderElementId}")
    public ResponseEntity<List<String>> getPaymentStatusByOrderElementId(@PathVariable Long orderElementId) {
        try {
            if (userDataService.hasUserLoginned()) {
                List<String> paymentStatuses = paymentService.getPaymentStatusByOrderElementId(orderElementId);
                if (!paymentStatuses.isEmpty()) {
                    return ResponseEntity.ok(paymentStatuses);
                } else {
                    return ResponseEntity.notFound().build();
                }
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("PaymentController -> getPaymentStatusByOrderElementId ERROR: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
