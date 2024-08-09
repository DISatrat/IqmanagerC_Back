package org.iqmanager.service.ratesService;

import org.iqmanager.models.RatesAndServices;
import org.iqmanager.repository.RatesAndServicesDAO;
import org.iqmanager.service.ratesService.RatesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RatesServiceImpl implements RatesService {
    private final RatesAndServicesDAO ratesAndServicesDAO;

    public RatesServiceImpl(RatesAndServicesDAO ratesAndServicesDAO) {
        this.ratesAndServicesDAO = ratesAndServicesDAO;
    }

    @Override
    public RatesAndServices getRates(long id) {
        return ratesAndServicesDAO.getOne(id);
    }

    @Override
    public long getPriceService(long id) {
        return ratesAndServicesDAO.getOne(id).getPrice();
    }
}
