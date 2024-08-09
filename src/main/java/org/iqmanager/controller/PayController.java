package org.iqmanager.controller;

import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.iqmanager.models.OrderElement;
import org.iqmanager.service.orderElementService.OrderElementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static org.iqmanager.ApplicationC.URL_WEB;



@RestController
@RequestMapping("/api")
@Validated
@CrossOrigin(origins = URL_WEB)
public class PayController {
    private final Logger logger = LoggerFactory.getLogger(PayController.class);
    private final OrderElementService orderElementService;



    @Value("${sber.username}")
    private String userName;

    @Value("${sber.password}")
    private String password;

    public PayController(OrderElementService orderElementService) {
        this.orderElementService = orderElementService;
    }

    @PostMapping("/register.do")
    public ResponseEntity<String> registrationOfPayment(@RequestParam String returnUrl,
                                                        @RequestParam String failUrl,
                                                        @RequestParam long orderId,
                                                        @RequestParam boolean prepayment,
                                                        @RequestParam(required = false, defaultValue = "RUB") String currency,
                                                        @RequestParam (required = false, defaultValue = "false") boolean sberPay){


        long amount;
        OrderElement orderElement = orderElementService.getOrderElement(orderId);
        if(prepayment){
            amount = orderElement.getOrderPrice() * (orderElement.getPost().getPrepayment() / 100);
        } else {
            amount = orderElementService.getOrderElement(orderId).getOrderPrice();
        }


        try {

            Form form = Form.form()
                    .add("userName", userName)
                    .add("password",password)
                    .add("amount", "" + amount)
                    .add("currency", currency)
                    .add("returnUrl", returnUrl)
                    .add("failUrl", failUrl)
                    .add("orderNumber", "" + orderId);
            if(sberPay){
                form.add("back2app", "true");
            }


            Content response = Request.Post("https://3dsec.sberbank.ru/sbrfpayment/rest/register.do")
                    .bodyForm(form.build())
                    .execute().returnContent();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
