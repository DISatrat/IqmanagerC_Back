package org.iqmanager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/** Модель исполнителя */

@Entity
@Table(name = "performer_data")
@Getter
@Setter
@NoArgsConstructor
public class PerformerData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    /** Имя */
    @Column(name = "name")
    private String name;

    /** Фамилия */
    @Column(name = "last_name")
    private String lastName;

    /** Кошелек */
    @Column(name = "wallet")
    private long wallet;

    /** Телефон */
    @Column(name = "phone")
    private String phone;


    /** Ссылка на соц сети */
    @Column(name = "web")
    private String web;

    /** Календарь */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "performer")
    @JsonIgnore
    List<Calendar> calendar;

    /** Объявление */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "performer")
    @JsonIgnore
    List<Post> posts;
}
