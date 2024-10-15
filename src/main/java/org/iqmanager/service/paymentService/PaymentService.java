package org.iqmanager.service.paymentService;

import org.iqmanager.dto.PaymentDTO;
import org.iqmanager.models.OrderElement;
import org.iqmanager.models.Payment;
import org.iqmanager.models.enum_models.PaymentStatus;

import java.util.List;
import java.util.Optional;


public interface PaymentService {
    Optional<Payment> getPaymentByTransactionId(String transactionId);
    Payment addPayment(PaymentDTO paymentDTO);

}
