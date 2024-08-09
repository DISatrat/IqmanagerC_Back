package org.iqmanager.dto;

import lombok.Getter;
import lombok.Setter;

/** Данные пользователя */

@Getter
@Setter
public class UserDataDTO {

    /** Имя */
    private String name;

    /** Фамилия */
    private String lastName;

    /** Email */
    private String email;

    /** Ссылка на соц. сети */
    private String web;

    /** Страна */
    private String country;

    /** Регион */
    private String region;

    /** Город */
    private String city;
}
