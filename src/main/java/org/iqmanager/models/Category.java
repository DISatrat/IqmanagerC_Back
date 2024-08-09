package org.iqmanager.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;



/** Модель категорий */
@Entity
@Table(name = "category")
@Getter
@Setter
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    /** Id родительского раздела каталога, если равен нулю, значит это начальный раздел */
    @Column(name = "id_parent")
    private long idParent;

    /** Является ли категория конечной, только в конечную категорию можно помещать объявления */
    @Column(name = "final")
    private boolean itsEnd = false;

    @Column(name = "hidden")
    private boolean hidden = false;

    /** Названия обложки для категории, весь путь указывать не нужно (img.png) */
    @Column(name = "image")
    private String image;

    @Column(name = "blur_image")
    private String blurImage;

    @OneToMany(mappedBy = "category", cascade = CascadeType.PERSIST, orphanRemoval = true)
    List<CategoryName> categoryNames;


    /** Объявления в данной категории */
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinTable(name="category_x_post",
    joinColumns = @JoinColumn(name = "category_id"),
    inverseJoinColumns = @JoinColumn(name = "post_id"))
    @JsonIgnore
    private List<Post> posts = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinTable(name="parent_x_child_category",
            joinColumns = @JoinColumn(name = "parent_id"),
            inverseJoinColumns = @JoinColumn(name = "child_id"))
    @JsonIgnore
    private List<Category> categories = new ArrayList<>();
}
