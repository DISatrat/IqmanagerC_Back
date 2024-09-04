package org.iqmanager.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.time.Instant;

/** Форма заявки */

@Entity
@Table(name = "request")
@Getter
@Setter
@NoArgsConstructor
public class RequestForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    /** Имя */
    @Column(name = "name")
    private String name;

    /** Телефон */
    @Column(name = "phone")
    private String phone;

    /** Комментарии */
    @Column(name = "comment")
    @Lob
    private String comment;

    @Column(name = "image")
    private String image;

    @Column(name = "date_order")
    private Instant dateOrder;

    @Transient
    private MultipartFile file;
}
