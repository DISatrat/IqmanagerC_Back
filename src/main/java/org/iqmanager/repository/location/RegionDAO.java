package org.iqmanager.repository.location;

import org.iqmanager.models.location.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import java.util.List;

@RepositoryRestController
public interface RegionDAO extends JpaRepository<Region, Long> {
    @Query("SELECT u FROM Region u WHERE u.country_id=:c AND u.region LIKE :r")
    List<Region> findRegions(@Param("c") long c, @Param("r") String r);
}
