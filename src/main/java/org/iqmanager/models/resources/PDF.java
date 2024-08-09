package org.iqmanager.models.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.iqmanager.models.Post;

import javax.persistence.*;


/** Модель данных документов */
@Table(name = "pdf")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class PDF {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    /** Путь к документу */
    @Column(name = "path")
    private String path;

    @Column(name = "name")
    private String name;

    /** Объявление */
    @ManyToOne
    @JoinColumn(name = "id_post")
    @JsonIgnore
    private Post post;


}