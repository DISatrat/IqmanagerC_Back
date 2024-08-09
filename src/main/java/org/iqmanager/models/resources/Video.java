package org.iqmanager.models.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.iqmanager.models.Post;

import javax.persistence.*;

/** Модель данных видео */
@Table(name = "video")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    /** Путь к видео */
    @Column(name = "path")
    private String path;

    /** Объявление */
    @ManyToOne
    @JoinColumn(name = "id_post")
    @JsonIgnore
    private Post post;

}