package org.iqmanager.repository;


import org.iqmanager.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

@RepositoryRestController
public interface PaymentDAO extends JpaRepository<Payment, Long> {
}
