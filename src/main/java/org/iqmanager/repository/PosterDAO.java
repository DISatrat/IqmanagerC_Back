package org.iqmanager.repository;

import org.iqmanager.models.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

@RepositoryRestController
public interface PosterDAO extends JpaRepository<Banner, Long> {
}
