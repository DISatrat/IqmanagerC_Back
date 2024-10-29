package org.iqmanager.models.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.iqmanager.models.Promo;

import javax.persistence.*;

@Table(name = "images_x_promo")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class ImagesPromo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** Путь к изображению */
    @Column(name = "path")
    private String path;

    /** Объявление */
    @ManyToOne
    @JoinColumn(name = "id_promo")
    @JsonIgnore
    private Promo promo;
}
