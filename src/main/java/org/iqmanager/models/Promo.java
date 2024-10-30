package org.iqmanager.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.iqmanager.models.enum_models.PromoStatus;
import org.iqmanager.models.resources.ImagesPromo;

import javax.persistence.*;
import java.time.Instant;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "promo")
@Getter
@Setter

public class Promo {

    /** id */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    /** Название промо акции */
    @Column(name = "name")
    private String name;

    /** Статус промо акции */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PromoStatus status;

    /** Дата */
    @Column(name = "date")
    private Instant date;

    /** Правила */
    @Column(name = "rules")
    private String rules;

    /** Изображения */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "promo")
    private Set<ImagesPromo> imagesSet;

}
