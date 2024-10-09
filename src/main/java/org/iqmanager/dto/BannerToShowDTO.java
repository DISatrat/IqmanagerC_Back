package org.iqmanager.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class BannerToShowDTO {

    private String title;
    /** Наличие ссылки */
    private boolean linkPresence;
    /** Адрес сслыки */
    private String linkAddress;
    /** Надпись на ссылке */
    private String linkInscription;
    /** Порядковый номер баннера */
    private int serialNumber;
    /** Фон баннера */
    private String bannerBackgroundImage;

}
