package org.iqmanager.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

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

    /** Название */
    @Column(name = "name")
    private String name;

    /** Дата и время создания */
    @Column(name = "date")
    private Instant date;

    /** Страна */
    @Column(name = "country")
    private String country;

    /** Регион */
    @Column(name = "region")
    private String region;

    /** Мета-тег title */
    @Column(name = "title")
    private String title;

    /** Мета-тег description */
    @Column(name = "description")
    private String description;

    /** Турбо-страница */
    @Column(name = "turbo_status")
    private String turboStatus;

    /** Ссылка на турбо страницу */
    @Column(name = "link")
    private String link;

    /** Статус загрузки */
    @Column(name = "upload_status")
    private String uploadStatus;

    /** Режим загрузки */
    @Column(name = "upload_mode")
    private String uploadMode;

    /** ID объявления */
    @Column(name = "post_id")
    private long postId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;

}
