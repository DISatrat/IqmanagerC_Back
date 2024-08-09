package org.iqmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.iqmanager.models.Comment;
import org.iqmanager.models.Post;


import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.ToIntFunction;

/** Страница выбора объявления */

@Getter
@Setter
@NoArgsConstructor
public class PostListDTO {

    /** id */
    private long id;

    /** Название объявления */
    private String name;

    /** Описание */
    private String title;

    /** Изображение */
    private String image;

    private String blurImage;

    /** Адресс */
    private String address;

    /** Просмотры */
    private long views;

    /** Одобренно iqm */
    private boolean like;

    /** Оценка */
    private int stars;

    /** Цена */
    private long price;

    private String currency;

    public PostListDTO(long id, String name, String title, String image,String blurImage, String address, long views, boolean like, int stars, long price, String currency) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.image = image;
        this.blurImage = blurImage;
        this.address = address;
        this.views = views;
        this.like = like;
        this.stars = stars;
        this.price = price;
        this.currency = currency;
    }
}
