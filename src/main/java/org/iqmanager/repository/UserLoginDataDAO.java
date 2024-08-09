package org.iqmanager.repository;

import org.iqmanager.models.UserLoginData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

@RepositoryRestController
public interface UserLoginDataDAO extends JpaRepository<UserLoginData, Long> {
    UserLoginData findByUsername(String username);
}
