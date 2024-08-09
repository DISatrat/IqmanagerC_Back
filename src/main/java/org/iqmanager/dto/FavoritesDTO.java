package org.iqmanager.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.iqmanager.models.Post;


@Getter
@Setter
@NoArgsConstructor
public class FavoritesDTO {

    private long id;
    // имя
    private String name;

    private String zipImage;

    private String blurImage;

    private long price;

    private boolean like;

    private byte stars;

    private String currency;

    public FavoritesDTO(long id, String name, String zipImage,String blurImage, long price, boolean like, byte stars, String currency) {
        this.id = id;
        this.name = name;
        this.zipImage = zipImage;
        this.blurImage = blurImage;
        this.price = price;
        this.like = like;
        this.stars = stars;
        this.currency = currency;
    }

    public static FavoritesDTO favoritesToDTO(Post post) {
        return new FavoritesDTO(post.getId(), post.getName(), post.getZipImageKey(),post.getBlurImage(), post.getPrice(), post.isLike(), post.getStars(), post.getCurrency());
    }
}
