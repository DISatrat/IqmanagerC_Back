package org.iqmanager.repository;

import org.iqmanager.models.Extra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;


@RepositoryRestController
public interface ExtraDAO extends JpaRepository<Extra, Long> {
}
