package org.iqmanager.repository;

import org.iqmanager.models.UserData;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import java.util.List;

@RepositoryRestController
public interface UserDataDAO extends JpaRepository<UserData, Long> {


    @EntityGraph(value = "user-data-basket-entity-graph")
    UserData getById(long id);
    @EntityGraph(value = "user-data-favorite-entity-graph")
    UserData getFirstById(long id);


}
