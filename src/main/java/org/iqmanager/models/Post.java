package org.iqmanager.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.iqmanager.models.enum_models.CalendarType;
import org.iqmanager.models.enum_models.PostStatus;
import org.iqmanager.models.resources.Images;
import org.iqmanager.models.resources.PDF;
import org.iqmanager.models.resources.Video;

import javax.persistence.*;
import java.time.Instant;
import java.util.*;



/** Модель данных объявления */

@Entity
@Table(name = "post")
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    /** Название объявления */
    @Column(name = "name")
    private String name;

    /** Путь к изображению */
    @Column(name = "image")
    private String imageKey;

    @Column(name = "zip_image")
    private String zipImageKey;

    @Column(name = "blur_image")
    private String blurImage;

    /** Цена */
    @Column(name = "price")
    private long price;

    // цена до изменения цена по дефолту 0
    @Column(name = "price_before_calendar")
    private long calendarStatus;

    @Column(name = "currency")
    private String currency;

    @Column(name = "price_in_rubles")
    private long priceInRubles;

    /** Цена за фиксированную доставку */
    @Column(name = "price_for_delivery_fix")
    private long priceForDeliveryFix;

    /** Цена за доставку за километр */
    @Column(name = "price_for_delivery_km")
    private long priceForDeliveryKm;

    /** Описание */
    @Column(name = "title")
    @Lob
    private String title;

    /** Просмотры */
    @Column(name="views")
    private long views;

    /** Сайт доверяет */
    @Column(name="\"like\"")
    private boolean like;

    /** Оценка */
    @Column(name = "stars")
    private byte stars;

    /** Статус объявления */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PostStatus status;

    /** Адрес исполнителя */
    @Column(name = "address")
    private String address;

    /** Тип объявления (товар или услуга) */
    @Column(name = "post_type")
    private String postType;

    /** Тип объявления (товар или услуга) */
    @Column(name = "payment_type")
    private String paymentType;

    /** Возможный аванс в процентах */
    @Column(name = "prepayment")
    private byte prepayment;

    /** Тип доставки */
    @Column(name = "delivery_type")
    private String deliveryType;

    /** Страна размещения*/
    @Column(name = "country")
    private String country;

    /** Регион размещения */
    @Column(name = "region")
    private String region;

    /** Город размещения */
    @Column(name = "city")
    private String city;

    /** Дата создания */
    @Column(name = "date_creation")
    private Instant dateCreation;

    /** Дата последнего изменения */
    @Column(name = "date_edit")
    private Instant dateEdit;

    @Column(name = "calendar_type")
    @Enumerated(EnumType.STRING)
    private CalendarType calendarType;


    /** id исполнителя */
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_performer")
    @JsonIgnore
    private PerformerData performer;

    /** Доп. условия */
    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "post")
    @JsonIgnore
    private Conditions conditions;

    /** Изображения */
    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "post")
    @JsonIgnore
    private Set<Images> imagesSet;

    /** Видео */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "post")
    @JsonIgnore
    private Set<Video> videoSet;

    /** Документы */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "post")
    @JsonIgnore
    private Set<PDF> pdfSet;

    /** Доп. услуги */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private Set<Extra> extra;

    /** Категории */
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "category_x_post", joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    @JsonIgnore
    private Set<Category> categories;

    /** Комментарии */
    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.REMOVE}, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private List<Comment> comments;

    public long getPrice() {
        return extra.stream()
                .filter(extra -> "SELECT_SINGLE".equals(extra.getType()))
                .flatMap(extra -> extra.getRatesAndServices().stream())
                .mapToLong(RatesAndServices::getPrice)
                .min()
                .orElse(price);
    }



    public Post(String name, long price, String title, String postType, String paymentType, String deliveryType, PostStatus status) {
        this.name = name;
        this.price = price;
        this.title = title;
        this.postType = postType;
        this.paymentType = paymentType;
        this.deliveryType = deliveryType;
        this.status = status;
    }


    public Post(String name, String imageKey, String zipImageKey, String blurImage, long price, String currency, long priceForDeliveryFix, long priceForDeliveryKm, String title, String address, String postType, String paymentType, byte prepayment, String deliveryType, String country, String region, String city ) {
        this.name = name;
        this.imageKey = imageKey;
        this.zipImageKey = zipImageKey;
        this.blurImage = blurImage;
        this.price = price;
        this.currency = currency;
        this.priceForDeliveryFix = priceForDeliveryFix;
        this.priceForDeliveryKm = priceForDeliveryKm;
        this.title = title;
        this.address = address;
        this.postType = postType;
        this.paymentType = paymentType;
        this.prepayment = prepayment;
        this.deliveryType = deliveryType;
        this.country = country;
        this.region = region;
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        if (id != post.id) return false;
        if (price != post.price) return false;
        if (priceForDeliveryFix != post.priceForDeliveryFix) return false;
        if (priceForDeliveryKm != post.priceForDeliveryKm) return false;
        if (views != post.views) return false;
        if (like != post.like) return false;
        if (stars != post.stars) return false;
        if (prepayment != post.prepayment) return false;
        if (!Objects.equals(name, post.name)) return false;
        if (!Objects.equals(imageKey, post.imageKey)) return false;
        if (!Objects.equals(title, post.title)) return false;
        if (!Objects.equals(address, post.address)) return false;
        if (!Objects.equals(postType, post.postType)) return false;
        if (!Objects.equals(paymentType, post.paymentType)) return false;
        if (!Objects.equals(deliveryType, post.deliveryType)) return false;
        if (!Objects.equals(country, post.country)) return false;
        if (!Objects.equals(region, post.region)) return false;
        if (!Objects.equals(city, post.city)) return false;
        return status == post.status;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (imageKey != null ? imageKey.hashCode() : 0);
        result = 31 * result + (int) (price ^ (price >>> 32));
        result = 31 * result + (int) (priceForDeliveryFix ^ (priceForDeliveryFix >>> 32));
        result = 31 * result + (int) (priceForDeliveryKm ^ (priceForDeliveryKm >>> 32));
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (int) (views ^ (views >>> 32));
        result = 31 * result + (like ? 1 : 0);
        result = 31 * result + (int) stars;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (postType != null ? postType.hashCode() : 0);
        result = 31 * result + (paymentType != null ? paymentType.hashCode() : 0);
        result = 31 * result + (int) prepayment;
        result = 31 * result + (deliveryType != null ? deliveryType.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (region != null ? region.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        return result;
    }
}
