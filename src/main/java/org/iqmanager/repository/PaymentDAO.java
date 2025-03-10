package org.iqmanager.repository;


import org.iqmanager.models.OrderElement;
import org.iqmanager.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import java.util.List;
import java.util.Optional;


@RepositoryRestController
public interface PaymentDAO extends JpaRepository<Payment, Long> {
    Optional<Payment> findByTransactionId(String transactionId);
}
