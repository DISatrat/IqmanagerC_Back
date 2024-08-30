package org.iqmanager.repository;

import org.iqmanager.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import java.util.List;

@RepositoryRestController
public interface AdminDataDAO extends JpaRepository<Admin, Long> {



}
