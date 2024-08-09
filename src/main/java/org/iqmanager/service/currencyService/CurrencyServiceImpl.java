package org.iqmanager.service.currencyService;

import org.iqmanager.repository.CurrencyDAO;
import org.iqmanager.service.currencyService.CurrencyService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyDAO currencyDAO;

    public CurrencyServiceImpl(CurrencyDAO currencyDAO) {
        this.currencyDAO = currencyDAO;
    }

    @Cacheable("exchange")
    @Override
    public long getExchange(String currency) {
        return currencyDAO.getOne(currency).getAttitudeToRuble();
    }
}
