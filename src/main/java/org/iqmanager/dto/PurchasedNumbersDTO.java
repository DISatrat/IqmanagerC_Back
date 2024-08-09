package org.iqmanager.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.iqmanager.models.PerformerData;
import org.iqmanager.models.Post;

@Getter
@Setter
@NoArgsConstructor
public class PurchasedNumbersDTO {
    private String phone;
    private long postId;
    private String postName;
    private String performerName;
    private String zipImage;
    private String blurImage;

    public PurchasedNumbersDTO(String phone, long postId, String postName, String performerName, String zipImage, String blurImage) {
        this.phone = phone;
        this.postId = postId;
        this.postName = postName;
        this.performerName = performerName;
        this.zipImage = zipImage;
        this.blurImage = blurImage;
    }

    public static PurchasedNumbersDTO createPurchasedNumbersDTO(Post post) {
        PerformerData performerData = post.getPerformer();
        return new PurchasedNumbersDTO(performerData.getPhone(), post.getId(), post.getName(), performerData.getName() + performerData.getLastName(), post.getZipImageKey(), post.getBlurImage());
    }
}
