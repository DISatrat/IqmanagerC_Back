package org.iqmanager.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.iqmanager.models.OrderedExtras;
import org.iqmanager.models.RatesAndServices;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class ExtraForBasketDTO {
    private String title;
    private String type;
    private Set<RatesAndServices> ratesAndServices;

    public ExtraForBasketDTO(String title, String type, Set<RatesAndServices> ratesAndServices) {
        this.title = title;
        this.type = type;
        this.ratesAndServices = ratesAndServices;
    }

    public static Set<ExtraForBasketDTO> ExtraToDTOFromBasket(Set<OrderedExtras> extras) {
        Set<ExtraForBasketDTO> extraForBasketDTO = new HashSet<>();
        for(OrderedExtras extra : extras){
            Hibernate.initialize(extra.getRatesAndServices());
            extraForBasketDTO.add(new ExtraForBasketDTO(extra.getTitle(), extra.getType(), extra.getRatesAndServices()));
        }
        return extraForBasketDTO;
    }
}
