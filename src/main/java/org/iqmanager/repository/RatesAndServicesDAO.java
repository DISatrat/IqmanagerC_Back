package org.iqmanager.repository;

import org.iqmanager.models.RatesAndServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

@RepositoryRestController
public interface RatesAndServicesDAO extends JpaRepository<RatesAndServices, Long> {
}
