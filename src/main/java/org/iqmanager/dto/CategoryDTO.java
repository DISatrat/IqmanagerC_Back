package org.iqmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.iqmanager.models.Category;
import org.iqmanager.models.CategoryName;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private long id;

    private long idParent;

    private boolean itsEnd;

    private boolean hidden;

    private String image;

    private String blurImage;

    private String categoryNames;

    private Map<String, Long> history;

    public static CategoryDTO categoryToDTO(Category category, String language) {
        Optional<String> name = category.getCategoryNames().stream().filter(x -> Objects.equals(x.getLanguage(), language)).map(CategoryName::getName).findFirst();
        return name.map(s -> new CategoryDTO(category.getId(), category.getIdParent(), category.isItsEnd(), category.getImage(),category.getBlurImage() ,s, category.isHidden())).orElseGet(() -> new CategoryDTO(category.getId(), category.getIdParent(), category.isItsEnd(), category.getImage(), category.getBlurImage(), category.getCategoryNames().stream().filter(x -> Objects.equals(x.getLanguage(), "en")).map(CategoryName::getName).findFirst().get(),category.isHidden()));
    }

    public CategoryDTO(long id, long idParent, boolean itsEnd, String image, String blurImage, String categoryNames, boolean hidden) {
        this.id = id;
        this.idParent = idParent;
        this.itsEnd = itsEnd;
        this.image = image;
        this.hidden = hidden;
        this.categoryNames = categoryNames;
    }
}
