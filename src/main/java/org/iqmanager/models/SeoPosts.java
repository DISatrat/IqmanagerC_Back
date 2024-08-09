package org.iqmanager.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/** SEO объявлений */

@Entity
@Table(name = "seo_posts")
@Getter
@Setter
@NoArgsConstructor
public class SeoPosts {
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

    // todo зачем роботы?
    @Column(name = "robots")
    private String robot;

    // todo что это?
    @Lob
    @Column(name = "content_type")
    private String contentType;

    @Lob
    @Column(name = "canonical")
    private String canonical;

    @Column(name = "post_id")
    private long postId;

}
