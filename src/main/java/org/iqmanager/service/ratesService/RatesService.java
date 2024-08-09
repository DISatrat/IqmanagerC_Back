package org.iqmanager.service.ratesService;

import org.iqmanager.models.RatesAndServices;

public interface RatesService {
    RatesAndServices getRates(long id);
    long getPriceService(long id);
}
