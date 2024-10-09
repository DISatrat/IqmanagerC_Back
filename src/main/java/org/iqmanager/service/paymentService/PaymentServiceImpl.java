package org.iqmanager.service.paymentService;

import org.iqmanager.dto.PaymentDTO;
import org.iqmanager.models.OrderElement;
import org.iqmanager.models.Payment;
import org.iqmanager.models.enum_models.PaymentStatus;
import org.iqmanager.repository.PaymentDAO;
import org.iqmanager.service.orderElementService.OrderElementService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentDAO paymentDAO;
    private final ModelMapper modelMapper;
    private final OrderElementService orderElementService;

    @Autowired
    public PaymentServiceImpl(PaymentDAO paymentDAO, ModelMapper modelMapper, OrderElementService orderElementService) {
        this.paymentDAO = paymentDAO;
        this.modelMapper = modelMapper;
        this.orderElementService = orderElementService;
    }


    @Override
    public Payment addPayment(PaymentDTO paymentDTO) {
        OrderElement orderElement = orderElementService.getOrderElement(paymentDTO.getOrderElementId());
        if (orderElement == null) {
            throw new IllegalArgumentException("OrderElement with ID " + paymentDTO.getOrderElementId() + " does not exist.");
        }
        Payment payment = modelMapper.map(paymentDTO, Payment.class);
        payment.setOrderElement(orderElement);
        payment.setCreatedAt(Instant.now());
        return paymentDAO.save(payment);
    }

    @Override
    public Optional<Payment> getPaymentById(Long id) {
        return paymentDAO.findById(id);
    }

    @Override
    public List<String> getPaymentStatusByOrderElementId(Long orderElementId) {
        OrderElement orderElement = orderElementService.getOrderElement(orderElementId);
        if (orderElement == null) {
            throw new IllegalArgumentException("OrderElement with ID " + orderElementId + " does not exist.");
        }
        List<Payment> payments = paymentDAO.findAllByOrderElementId(orderElementId);
        return payments.stream()
                .map(payment -> payment.getId() + " - " + payment.getPaymentStatus())
                .collect(Collectors.toList());
    }
}

