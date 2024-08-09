package org.iqmanager.service.orderedExtraService;

import org.iqmanager.models.OrderedExtras;
import org.iqmanager.repository.OrderExtraDAO;
import org.iqmanager.service.orderedExtraService.OrderedExtraService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderedExtraServiceImpl implements OrderedExtraService{

    private final OrderExtraDAO orderExtraDAO;

    public OrderedExtraServiceImpl(OrderExtraDAO orderExtraDAO) {
        this.orderExtraDAO = orderExtraDAO;
    }

    @Override
    public OrderedExtras save(OrderedExtras orderedExtras) {
        return orderExtraDAO.save(orderedExtras);
    }

    public void delete(long id) {
        orderExtraDAO.deleteById(id);
    }
}
