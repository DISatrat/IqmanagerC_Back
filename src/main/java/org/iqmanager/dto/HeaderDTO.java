package org.iqmanager.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/** Данные для хэдера */

@Getter
@Setter
@NoArgsConstructor
public class HeaderDTO {

    /** Имя пользователя */
    private String name;

    /** Является ли агентом */
    private boolean agent;

    /** Количество элементов в избранном */
    private List<Long> favorites;

    /** Количество элементов в корзине */
    private List<Long> basket;

    private boolean blockStatus;

    private String role = "ROLE_USER";
    public HeaderDTO(String name, boolean isAgent , List<Long> favorites, List<Long> basket, boolean blockStatus) {
        this.name = name;
        this.agent = isAgent;
        this.favorites = favorites;
        this.basket = basket;
        this.blockStatus = blockStatus;
    }

}
