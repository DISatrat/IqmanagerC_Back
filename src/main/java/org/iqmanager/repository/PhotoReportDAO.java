package org.iqmanager.repository;

import org.iqmanager.models.PhotoReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

@RepositoryRestController
public interface PhotoReportDAO extends JpaRepository<PhotoReport, Long> {
}
