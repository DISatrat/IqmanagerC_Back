package org.iqmanager.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UserAuthDataDTO {

    /** Логин */
    private String username;

    /** Пароль */
    private String password;

    private String code;

}
