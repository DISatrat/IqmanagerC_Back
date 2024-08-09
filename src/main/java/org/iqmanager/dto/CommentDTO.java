package org.iqmanager.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Комментарий */

@Getter
@Setter
@NoArgsConstructor
public class CommentDTO {

    /** id */
    private long id;

    /** Имя комментатора */
    private String name;

    /** Текст комментария */
    private String text;

    /** Оценка */
    private byte stars;

    /** Дата оставления комментария */
    private long date;

    public CommentDTO(long id, String name, String text, byte stars, long date) {
        this.id = id;
        this.name = name;
        this.text = text;
        this.stars = stars;
        this.date = date;
    }
}
