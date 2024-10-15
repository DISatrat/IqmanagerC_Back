package org.iqmanager.service.paymentService;

import org.iqmanager.dto.PaymentDTO;
import org.iqmanager.models.OrderElement;
import org.iqmanager.models.Payment;
import org.iqmanager.models.Post;
import org.iqmanager.models.enum_models.PaymentStatus;
import org.iqmanager.models.enum_models.StatusOrder;
import org.iqmanager.repository.OrderElementDAO;
import org.iqmanager.repository.PaymentDAO;
import org.iqmanager.service.orderElementService.OrderElementService;
import org.iqmanager.service.postService.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;


@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentDAO paymentDAO;
    private final ModelMapper modelMapper;
    private final OrderElementService orderElementService;
    private final PostService postService;
    private final OrderElementDAO orderElementDAO;

    @Autowired
    public PaymentServiceImpl(PaymentDAO paymentDAO, ModelMapper modelMapper, OrderElementService orderElementService, PostService postService, OrderElementDAO orderElementDAO) {
        this.paymentDAO = paymentDAO;
        this.modelMapper = modelMapper;
        this.orderElementService = orderElementService;
        this.postService = postService;
        this.orderElementDAO = orderElementDAO;
    }

    @Override
    @Transactional
    public Payment addPayment(PaymentDTO paymentDTO) {

        Optional<Payment> paymentOlder= paymentDAO.findByTransactionId(paymentDTO.getTransactionId());
        Payment payment;
        if (paymentOlder.isPresent()) {
            payment = paymentOlder.get();
            payment.setPaymentStatus(paymentDTO.getPaymentStatus());
            payment.setPrice(paymentDTO.getPrice());
            payment.setOrderElement(orderElementService.getOrderElement(paymentDTO.getOrderElementId()));
            payment.setPaidInterest(paymentDTO.getPaidInterest());
            payment.setRefundedAmount(paymentDTO.getRefundedAmount());
        }else{
            payment = modelMapper.map(paymentDTO, Payment.class);
        }

        OrderElement orderElement = orderElementService.getOrderElement(paymentDTO.getOrderElementId());
        if (orderElement == null) {
            throw new IllegalArgumentException("OrderElement with ID " + paymentDTO.getOrderElementId() + " does not exist.");
        }
        Post post = postService.getPost(orderElement.getPost().getId());

        payment.setOrderElement(orderElement);
        payment.setCreatedAt(Instant.now().plus(Duration.ofHours(3)));

        byte prepayment = post.getPrepayment();
        int paymentAmount = payment.getPrice().intValue();
        if (payment.getPaymentStatus() == PaymentStatus.succeeded) {
            if (prepayment != 0 && !orderElement.getStatusOrder().equals(StatusOrder.ADVANCE_PAID.name())) {
                if (paymentAmount == (post.getPrepayment() * orderElement.getLeftToPay())/100) {
                    orderElement.setStatusOrder(StatusOrder.ADVANCE_PAID.name());
                    orderElement.setLeftToPay(orderElement.getLeftToPay() - paymentAmount);
                }
            } else if (orderElement.getStatusOrder().equals(StatusOrder.ADVANCE_PAID.name()) || prepayment == 0) {
                orderElement.setLeftToPay(orderElement.getLeftToPay() - paymentAmount);
            }
            if (orderElement.getLeftToPay() == 0) {
                orderElement.setStatusOrder(StatusOrder.WAITING_EXECUTION.name());
            }
            orderElementDAO.save(orderElement);
        }
        return paymentDAO.save(payment);
    }


    @Override
    public Optional<Payment> getPaymentByTransactionId(String transactionId) {
        return paymentDAO.findByTransactionId(transactionId);
    }

}

