package org.iqmanager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/** Тарифы И Услуги */
@Entity
@Table(name = "rates_and_services")
@Getter
@Setter
@NoArgsConstructor
public class RatesAndServices {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private long price;

    @Column(name = "image")
    private String image;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "extra_id")
    @JsonIgnore
    private Extra extra;

}
