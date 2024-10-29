package org.iqmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.iqmanager.models.Promo;
import org.iqmanager.models.resources.ImagesPromo;
import org.modelmapper.ModelMapper;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class PromoDTO {

    /** Название */
    private String name;
    /** Статус */
    private String status;
    /** Дата */
    private Instant date;
    /** Изображение */
    private String image;
    /** Правила */
    private String rules;

    private Set<ImagesPromo> imagesSet;
}
