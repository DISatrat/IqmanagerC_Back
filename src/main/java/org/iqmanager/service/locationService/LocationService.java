package org.iqmanager.service.locationService;

import org.iqmanager.models.location.City;
import org.iqmanager.models.location.Country;
import org.iqmanager.models.location.Region;

import java.util.List;

public interface LocationService {
    List<Country> findCountry(String s);
    List<Region> findRegion(long id, String r);

    List<City> findCity(long region, String s);
}
