package org.iqmanager.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.iqmanager.models.OrderElement;

import javax.persistence.Column;
import java.time.Instant;
import java.util.Set;

/** Корзина */
@Setter
@Getter
@NoArgsConstructor
public class BasketDTO {

    /** id */
    private long id;

    /** Изображение */
    private String image;

    private long dateOrder;

    private long leftToPay;

    private String blurImage;
    /** Дата начала события */
    private long dateEvent;

    /** Длительность */
    private float duration;

    /** Количество людей */
    private long people;

    private long quantity;

    private String status;

    /** Адресс исполнения */
    private String address;

    /** id объявления */
    private long id_post;

    /** Название объявления */
    private String name;

    /** Цена */
    private long price;

    /** Цена за доставку */
    private long priceDelivery;

    /** Одобрено iqManager */
    private boolean like;

    /** Адресс исполнителя */
    private String address_post;

    /** Тип объявления */
    private String postType;

    /** Тип вычисления цены */
    private String paymentType;

    /** Возможная предоплата */
    private byte prepayment;

    /** Тип доставки */
    private String deliveryType;

    /** Страна */
    private String country;

    /** Регион */
    private String region;

    /** Город */
    private String city;

    /** Закзанные доп услуги */
    private Set<ExtraForBasketDTO> extra;

    /** Валюта */
    private String currency;

    public BasketDTO(long id,long dateOrder, long dateEvent, String blurImage, String image ,float duration, long people, long quantity,
                     String status ,String address, long id_post, String name,
                     long price, long priceDelivery, long leftToPay, boolean like,
                     String address_post, String postType, String paymentType, byte prepayment, String deliveryType,
                     Set<ExtraForBasketDTO> extra, String currency) {
        this.id = id;
        this.dateOrder = dateOrder;
        this.image = image;
        this.blurImage = blurImage;
        this.dateEvent = dateEvent;
        this.duration = duration;
        this.people = people;
        this.quantity = quantity;
        this.status = status;
        this.address = address;
        this.id_post = id_post;
        this.name = name;
        this.price = price;
        this.priceDelivery = priceDelivery;
        this.leftToPay = leftToPay;
        this.like = like;
        this.address_post = address_post;
        this.postType = postType;
        this.paymentType = paymentType;
        this.prepayment = prepayment;
        this.deliveryType = deliveryType;
        this.extra = extra;
        this.currency = currency;
    }

    public static BasketDTO BasketToDTO(OrderElement orderElement) {

        Set<ExtraForBasketDTO> extra = ExtraForBasketDTO.ExtraToDTOFromBasket(orderElement.getOrderedExtras());

        long dateEvent = 0;
        if(orderElement.getDateEvent() != null){
            dateEvent = orderElement.getDateEvent().getEpochSecond();
        }

        return new BasketDTO(orderElement.getId(), orderElement.getDateOrder().getEpochSecond(),dateEvent , orderElement.getPost().getBlurImage(), orderElement.getPost().getZipImageKey(),orderElement.getDuration(),
                orderElement.getPeople(), orderElement.getQuantity(), orderElement.getStatusOrder(),orderElement.getAddress(), orderElement.getPost().getId(), orderElement.getPost().getName(),
                orderElement.getOrderPrice(), orderElement.getPriceDelivery(), orderElement.getLeftToPay(), orderElement.getPost().isLike(),
                orderElement.getPost().getAddress(), orderElement.getPost().getPostType(), orderElement.getPost().getPaymentType(),orderElement.getPost().getPrepayment(),
                orderElement.getPost().getDeliveryType(), extra, orderElement.getPost().getCurrency());
    }
}
