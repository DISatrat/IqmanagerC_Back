package org.iqmanager.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/** Названия категорий */
@Getter
@Setter
@Entity
@Table(name = "category_name")
@NoArgsConstructor
public class CategoryName {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "language")
    private String language;

    @ManyToOne
    @JoinColumn(name = "id_category")
    Category category;
}
