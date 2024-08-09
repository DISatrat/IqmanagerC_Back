package org.iqmanager.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.iqmanager.models.*;
import org.iqmanager.models.resources.Images;
import org.iqmanager.models.resources.PDF;
import org.iqmanager.models.resources.Video;
import org.springframework.core.io.ByteArrayResource;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/** Страница объявления */

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Getter
@Setter
@NoArgsConstructor
public class PostDTO {

    /** id */
    private long id;

    /** Название объявления */
    private String name;

    /** Изображение */
    private String image;

    private String blurImage;

    /** Цена */
    private long price;

    /** Валюта */
    private String currency;

    /** Описание */
    private String title;

    /** Просмотры */
    private long views;

    /** Одобрено iqManager */
    private boolean like;

    /** Статус */
    private boolean status;

    /** Адресс */
    private String address;

    /** Оценка */
    private byte stars;

    /** Тип поста */
    private String postType;

    /** Тип вычисления цены */
    private String paymentType;

    /** Предоплата */
    private byte prepayment;

    /** Тип доставки */
    private String deliveryType;

    /** Цена за фиксированную доставку */
    private long priceForDeliveryFix;

    /** Цена за доставку за километр */
    private long priceForDeliveryKm;

    /** Страна */
    private String country;

    /** Регион */
    private String region;

    /** Город */
    private String city;

    /** id исполнителя */
    private long performerId;

    /** Изобрадения к объявлению */
    private Set<Images> imagesSet;

    /** Видео к объявлению */
    private Set<Video> videoSet;

    /** Документы к объявлению */
    private Set<PDF> pdfSet;

    /** Допю услуги к объявлению */
    private Set<Extra> extra;

    /** Комментарии к объявлению */
    private List<CommentDTO> comments;

    /** Доп. условия к объявлению */
    private Conditions conditions;

    private String[][] history;

    public static PostDTO postToDto(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setImage(post.getImageKey());
        postDTO.setBlurImage(post.getBlurImage());
        postDTO.setName(post.getName());
        postDTO.setPrice(post.getPrice());
        postDTO.setCurrency(post.getCurrency());
        postDTO.setTitle(post.getTitle());
        postDTO.setViews(post.getViews());
        postDTO.setLike(post.isLike());
        postDTO.setStatus(post.isStatus());
        postDTO.setStars(post.getStars());
        postDTO.setAddress(post.getAddress());
        postDTO.setPostType(post.getPostType());
        postDTO.setPaymentType(post.getPaymentType());
        postDTO.setPrepayment(post.getPrepayment());
        postDTO.setDeliveryType(post.getDeliveryType());
        postDTO.setPriceForDeliveryFix(post.getPriceForDeliveryFix());
        postDTO.setPriceForDeliveryKm(post.getPriceForDeliveryKm());
        postDTO.setCountry(post.getCountry());
        postDTO.setRegion(post.getRegion());
        postDTO.setCity(post.getCity());
        postDTO.setPerformerId(post.getPerformer().getId());
        Hibernate.initialize(post.getImagesSet());
        postDTO.setImagesSet(post.getImagesSet());
        Hibernate.initialize(post.getVideoSet());
        postDTO.setVideoSet(post.getVideoSet());
        Hibernate.initialize(post.getPdfSet());
        postDTO.setPdfSet(post.getPdfSet());
        Hibernate.initialize(post.getExtra());
        postDTO.setExtra(post.getExtra());
        Hibernate.initialize(post.getComments());
        postDTO.setComments(commentToDTO(post.getComments()));
        Hibernate.initialize(post.getConditions());
        postDTO.setConditions(post.getConditions());
        return postDTO;
    }
    private static List<CommentDTO> commentToDTO(List<Comment> comments) {
        return comments.stream().map(x -> new CommentDTO(x.getId(), x.getName(), x.getText(), x.getStars(), x.getDate().getEpochSecond())).collect(Collectors.toList());
    }

}
