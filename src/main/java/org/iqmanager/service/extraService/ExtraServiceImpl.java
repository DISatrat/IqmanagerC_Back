package org.iqmanager.service.extraService;

import org.iqmanager.dto.ExtraForOrderElementDTO;
import org.iqmanager.models.OrderedExtras;
import org.iqmanager.models.RatesAndServices;
import org.iqmanager.repository.ExtraDAO;
import org.iqmanager.repository.OrderExtraDAO;
import org.iqmanager.repository.RatesAndServicesDAO;
import org.iqmanager.service.extraService.ExtraService;
import org.iqmanager.service.orderedExtraService.OrderedExtraService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class ExtraServiceImpl implements ExtraService {
    private final ExtraDAO extraDAO;
    private final OrderExtraDAO orderExtraDAO;
    private final RatesAndServicesDAO ratesAndServicesDAO;
    private final OrderedExtraService orderedExtraService;

    public ExtraServiceImpl(ExtraDAO extraDAO, OrderExtraDAO orderExtraDAO, RatesAndServicesDAO ratesAndServicesDAO, OrderedExtraService orderedExtraService) {
        this.extraDAO = extraDAO;
        this.orderExtraDAO = orderExtraDAO;
        this.ratesAndServicesDAO = ratesAndServicesDAO;
        this.orderedExtraService = orderedExtraService;
    }

    /** Получение доп. услуг по id */
    @Override
    public Set<OrderedExtras> getExtras(Set<ExtraForOrderElementDTO> extras) {
        Set<OrderedExtras> orderedExtras = new HashSet<>();
        for (ExtraForOrderElementDTO extra : extras) {
            OrderedExtras orderedExtra = new OrderedExtras();
            orderedExtra.setTitle(extra.getTitle());
            orderedExtra.setType(extra.getType());
            Set<RatesAndServices> ratesAndServices = new HashSet<>();
            for(long idRatesAndService : extra.getIdRatesAndService()) {
                RatesAndServices ratesAndService = new RatesAndServices();
                ratesAndService.setId(idRatesAndService);
                ratesAndService.setName(ratesAndServicesDAO.getOne(idRatesAndService).getName());
                ratesAndService.setPrice(ratesAndServicesDAO.getOne(idRatesAndService).getPrice());
                ratesAndService.setImage(ratesAndServicesDAO.getOne(idRatesAndService).getImage());
                ratesAndService.setExtra(ratesAndServicesDAO.getOne(idRatesAndService).getExtra());
                ratesAndServices.add(ratesAndService);
            }

            orderedExtra.setRatesAndServices(ratesAndServices);
            orderedExtras.add(orderExtraDAO.save(orderedExtra));
        }
        return orderedExtras;
    }
}
