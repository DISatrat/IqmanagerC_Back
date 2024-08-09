package org.iqmanager.models.location;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/** Регион */

@Entity
@Table(name = "region")
@Getter
@Setter
@NoArgsConstructor
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    /** Название региона */
    @Column(name = "name")
    private String region;

    /** id страны */
    @Column(name = "country_id")
    private long country_id;

}
