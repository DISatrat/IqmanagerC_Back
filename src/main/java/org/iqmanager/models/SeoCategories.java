package org.iqmanager.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/** SEO для категорий */

@Entity
@Table(name = "seo_categories")
@Getter
@Setter
@NoArgsConstructor
public class SeoCategories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Lob
    @Column(name = "title")
    private String title;

    @Lob
    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "keywords")
    private String keyword;

    //todo убрать?
    @Column(name = "robots")
    private String robot;


    //todo убрать?
    @Lob
    @Column(name = "content_type")
    private String contentType;

    @Lob
    @Column(name = "canonical")
    private String canonical;


    @Column(name = "category_id")
    private long categoriesId;
}
