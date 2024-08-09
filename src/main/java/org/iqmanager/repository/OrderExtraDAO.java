package org.iqmanager.repository;

import org.iqmanager.models.OrderedExtras;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

@RepositoryRestController
public interface OrderExtraDAO extends JpaRepository<OrderedExtras, Long> {
}
