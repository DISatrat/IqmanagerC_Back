package org.iqmanager.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;


/** Модель данных комментариев к заказу */

@Entity
@Table(name = "comment")
@Getter
@Setter
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    /** Имя оставившего комментарий */
    @Column(name = "user_name")
    private String name;

    /** Текст комментария */
    @Column(name = "text")
    @Lob
    private String text;

    /** Оценка от 1 до 5 */
    @Column(name = "stars")
    private byte stars;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private Post post;

    @Column(name = "date")
    private Instant date=Instant.now();

    public Comment(String name, String text, byte stars) {
        this.name = name;
        this.stars = stars;
        this.text=text;
    }


}
