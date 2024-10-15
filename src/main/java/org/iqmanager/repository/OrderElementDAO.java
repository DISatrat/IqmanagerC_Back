package org.iqmanager.repository;

import org.iqmanager.models.OrderElement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import java.util.List;

@RepositoryRestController
public interface OrderElementDAO extends JpaRepository<OrderElement, Long> {

    OrderElement findOrderElementById(long id);

}
