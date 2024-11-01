package org.iqmanager.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.iqmanager.models.OrderElement;
import org.iqmanager.models.enum_models.PaymentMethod;
import org.iqmanager.models.enum_models.PaymentStatus;

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
    private BigDecimal payToPerformer;
    private PaymentStatus paymentStatus;
    private boolean isTest;
    private BigDecimal refundedAmount;
    private String description;
    private long orderElementId;
    private Instant createdAt;
    private String currency;
}
