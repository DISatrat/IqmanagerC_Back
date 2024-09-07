package org.iqmanager.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "banner")
@Getter
@Setter
public class Banner {

    /** id */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "title")
    private String title;
    /** Наличие ссылки */
    @Column(name = "link_presence")
    private boolean linkPresence;

    /** Адрес сслыки */
    @Column(name = "link_address")
    private String linkAddress;

    /** Надпись на ссылке */
    @Column(name = "link_inscription")
    private String linkInscription;

    /** Порядковый номер баннера */
    @Column(name = "serial_number")
    private int serialNumber;

    /** Видимость баннера */
    @Column(name = "banner_visible")
    private boolean bannerVisible;

    /** Фон баннера */
    @Column(name = "banner_background")
    private String bannerBackground;
}
