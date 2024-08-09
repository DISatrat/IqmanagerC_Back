package org.iqmanager.repository;

import org.iqmanager.models.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

@RepositoryRestController
public interface CurrencyDAO extends JpaRepository<Currency, String> {
}
