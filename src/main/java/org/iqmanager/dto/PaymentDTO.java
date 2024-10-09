package org.iqmanager.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.iqmanager.models.enum_models.PaymentMethod;

import java.math.BigDecimal;
import java.time.Instant;
@Getter
@Setter
@NoArgsConstructor
public class PaymentDTO {
    private long id;
    private String transactionId;
    private Instant payDate;
    private PaymentMethod paymentMethod;
    private BigDecimal price;
    private BigDecimal paidInterest;
    private String paymentStatus;
    private boolean isTest;
    private BigDecimal refundedAmount;
    private String description;
}
