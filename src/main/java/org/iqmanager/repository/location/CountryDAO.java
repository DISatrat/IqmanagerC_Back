package org.iqmanager.repository.location;

import org.iqmanager.models.location.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import java.util.List;

@RepositoryRestController
public interface CountryDAO extends JpaRepository<Country, Long> {

    @Query("SELECT u FROM Country u WHERE u.country LIKE :s")
    List<Country> findCountry(@Param("s") String s);
}
