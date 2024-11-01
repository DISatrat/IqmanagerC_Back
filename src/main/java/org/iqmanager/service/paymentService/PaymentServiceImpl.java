package org.iqmanager.service.paymentService;

import org.iqmanager.dto.PaymentDTO;
import org.iqmanager.models.Contract;
import org.iqmanager.models.OrderElement;
import org.iqmanager.models.Payment;
import org.iqmanager.models.Post;
import org.iqmanager.models.enum_models.PaymentStatus;
import org.iqmanager.models.enum_models.StatusOrder;
import org.iqmanager.repository.ContractDAO;
import org.iqmanager.repository.OrderElementDAO;
import org.iqmanager.repository.PaymentDAO;
import org.iqmanager.service.orderElementService.OrderElementService;
import org.iqmanager.service.postService.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;


@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentDAO paymentDAO;
    private final ModelMapper modelMapper;
    private final OrderElementService orderElementService;
    private final PostService postService;
    private final OrderElementDAO orderElementDAO;
    private final ContractDAO contractDAO;

    @Autowired
    public PaymentServiceImpl(PaymentDAO paymentDAO, ModelMapper modelMapper, OrderElementService orderElementService, PostService postService, OrderElementDAO orderElementDAO, ContractDAO contractDAO) {
        this.paymentDAO = paymentDAO;
        this.modelMapper = modelMapper;
        this.orderElementService = orderElementService;
        this.postService = postService;
        this.orderElementDAO = orderElementDAO;
        this.contractDAO = contractDAO;
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
        //Contract contract = contractDAO.findContractByPerformerData(post.getPerformer());

        payment.setOrderElement(orderElement);
        if(payment.getCreatedAt()==null){
            payment.setCreatedAt(Instant.now());
        }
        int paymentAmount = payment.getPrice().intValue();
        if (payment.getPaymentStatus() == PaymentStatus.succeeded && orderElement.getStatusOrder().equals(StatusOrder.WAITING_PAYMENT.name())) {
                orderElement.setLeftToPay(orderElement.getLeftToPay() - paymentAmount);
            }
            if (orderElement.getLeftToPay() == 0) {
                orderElement.setStatusOrder(StatusOrder.WAITING_EXECUTION.name());
            }
            orderElementDAO.save(orderElement);

         //   int perfPay;
//            if (Objects.equals(contract.getLegalStatus(), "тип 1")) {
//                perfPay = 94;
//            } else if (Objects.equals(contract.getLegalStatus(), "тип 2")) {
//                perfPay = 90;
//            } else if (Objects.equals(contract.getLegalStatus(), "тип 3")) {
//                perfPay = 85;
//            } else if (Objects.equals(contract.getLegalStatus(), "тип 4")) {
//                perfPay = 80;
//            } else {
//                throw new IllegalArgumentException("Unknown legal status: " + contract.getLegalStatus());
//            }
//            payment.setPayToPerformer(payment.getPaidInterest()
//                    .multiply(BigDecimal.valueOf(perfPay))
//                    .divide(BigDecimal.valueOf(100), RoundingMode.HALF_DOWN));
        return paymentDAO.save(payment);
    }


    @Override
    public Optional<Payment> getPaymentByTransactionId(String transactionId) {
        return paymentDAO.findByTransactionId(transactionId);
    }

}

