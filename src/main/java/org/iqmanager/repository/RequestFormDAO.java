package org.iqmanager.repository;

import org.iqmanager.models.RequestForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

@RepositoryRestController
public interface RequestFormDAO extends JpaRepository<RequestForm, Long> {
}
