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

    public PayController(OrderElementService orderElementService) {
        this.orderElementService = orderElementService;
    }




}
