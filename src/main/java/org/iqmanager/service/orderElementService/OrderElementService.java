package org.iqmanager.service.orderElementService;

import org.iqmanager.dto.BasketDTO;
import org.iqmanager.dto.OrderElemDTO;
import org.iqmanager.models.Calendar;
import org.iqmanager.models.OrderElement;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderElementService {
    OrderElement save(OrderElement orderElement);

    void deleteOrderElem(long id);

    void deleteAllOderElem();

    OrderElement getOrderElement(long id);

    String getCustomerPhone(long id);

    double calculateDelivery(String address, long idPost, Double distance);

    BasketDTO getBasketDTO(long id);

    long calculatePrice(OrderElemDTO orderElemDTO, OrderElement orderElement);

    long calculatePriceChange(double partInsideDuration, long tFactor, Calendar calendar);
}
