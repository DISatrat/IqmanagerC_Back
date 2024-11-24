package org.iqmanager.dto;

import lombok.Getter;
import lombok.Setter;
import org.iqmanager.models.OrderElement;
import org.iqmanager.models.enum_models.StatusOrder;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/** Элемент заказа */

@Getter
@Setter
public class OrderElemDTO {

    /** id объявления */
    private long idPost;

    /** Дата события */
    private Instant dateEvent;

    /** Длительность */
    private float duration;

    /** Количество людей */
    private int people;

    /** Количество */
    private long quantity;

    /** Адресс */
    private String address;

    /** Описане события */
    private String eventTitle;

    /** Коментарий к заказу */
    private String comment;

    /** Массив из доп. услуг */
    private Set<ExtraForOrderElementDTO> extra;



    public long getTariffId() {
        for(ExtraForOrderElementDTO extraForOrderElementDTO : extra) {
            if(Objects.equals(extraForOrderElementDTO.getType(), "SELECT_SINGLE")) {
                return extraForOrderElementDTO.getIdRatesAndService()[0];
            }
        }
        return 0;
    }

    public List<Long> getServicesId() {
        List<Long> servicesId = new ArrayList<>();
        for(ExtraForOrderElementDTO extraForOrderElementDTO : extra) {
            if(Objects.equals(extraForOrderElementDTO.getType(), "SELECT_MULTIPLE")) {
                servicesId.addAll(Arrays.stream(extraForOrderElementDTO.getIdRatesAndService()).boxed().collect(Collectors.toList()));
            }
        }
        return servicesId;
    }

    public double getFactor(String PaymentType, String PostType) {
        if(PaymentType.equals("PEOPLE")) {
            return people;
        } else if(PaymentType.equals("HOURS")) {
            return duration;
        }else if(PostType.equals("PRODUCT") && quantity!= 0 ) {
            return quantity;
        }
        return 0;
    }

    public  OrderElement DTOtoOrder(OrderElemDTO orderElemDTO) {

        return new OrderElement(
                orderElemDTO.getDateEvent(),
                orderElemDTO.getDuration(),
                orderElemDTO.getPeople(),
                orderElemDTO.getQuantity(),
                StatusOrder.WAITING_PERFORMER.toString(),
                orderElemDTO.getAddress(),
                orderElemDTO.getEventTitle(),
                orderElemDTO.getComment()
                );
    }
}
