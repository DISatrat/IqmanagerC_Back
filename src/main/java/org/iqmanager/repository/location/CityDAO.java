package org.iqmanager.repository.location;

import org.iqmanager.models.location.City;
import org.iqmanager.models.location.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import java.util.List;

@RepositoryRestController
public interface CityDAO extends JpaRepository<City, Long> {
    @Query("SELECT u FROM City u WHERE u.region=:r AND u.city LIKE:c")
    List<City> findCities(@Param("r") long r, @Param("c") String c);
}
