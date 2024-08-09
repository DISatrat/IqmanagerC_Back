package org.iqmanager.service.locationService;

import org.iqmanager.models.location.City;
import org.iqmanager.models.location.Country;
import org.iqmanager.models.location.Region;
import org.iqmanager.repository.location.CityDAO;
import org.iqmanager.repository.location.CountryDAO;
import org.iqmanager.repository.location.RegionDAO;
import org.iqmanager.service.locationService.LocationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LocationServiceImpl implements LocationService {
    private final CityDAO cityDAO;
    private final RegionDAO regionDAO;
    private final CountryDAO countryDAO;

    public LocationServiceImpl(CityDAO cityDAO, RegionDAO regionDAO, CountryDAO countryDAO) {
        this.cityDAO = cityDAO;
        this.regionDAO = regionDAO;
        this.countryDAO = countryDAO;
    }

    /** Получить страны по введенному запросу */
    @Override
    public List<Country> findCountry(String s) {
        return countryDAO.findCountry(s);
    }

    /** Получить регионы по стране и введенному запросу */
    @Override
    public List<Region> findRegion(long id, String r) {
        return regionDAO.findRegions(id, r);
    }

    /** Получить города по региону и введенному запросу */
    @Override
    public List<City> findCity(long region, String s) {
        return cityDAO.findCities(region, s);
    }
}
