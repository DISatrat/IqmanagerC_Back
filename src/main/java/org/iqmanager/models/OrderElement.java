package org.iqmanager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.Set;

/** Модель данных элемента заказа */

@Entity
@Table(name = "order_element")
@Getter
@Setter
@NoArgsConstructor
public class OrderElement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "date_order")
    private Instant dateOrder;

    /** Осталось оплатить */
    @Column(name = "left_to_pay")
    private long leftToPay;

    /** Дата события */
    @Column(name = "date_event")
    private Instant dateEvent;

    @Column(name = "price_delivery")
    private long priceDelivery;

    @Column(name = "order_price")
    private long orderPrice;

    /** Длительность */
    @Column(name = "duration")
    private float duration;

    /** Количество людей */
    @Column(name = "people")
    private long people;

    /** Кличество */
    @Column(name= "quantity")
    private long quantity;

    /** Статус заказа */
    @Column(name = "status")
    private String statusOrder;

    /** Место проведения */
    @Column(name = "address")
    private String address;

    @Column(name = "event_title")
    private String eventTitle;

    @Lob
    @Column(name = "comment")
    private String comment;


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private Post post;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "basket",
            joinColumns = @JoinColumn(name = "id_order_element"),
            inverseJoinColumns = @JoinColumn(name = "id_user_data"))
    @JsonIgnore
    private UserData userData;

    /** Оплата */
    @OneToMany(mappedBy = "orderElement", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnore
    private List <Payment> payments;


    @OneToMany(cascade = CascadeType.MERGE,fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "order_element_id")
    @JsonIgnore
    private Set<OrderedExtras> orderedExtras;

    public void addOrderedExtras(OrderedExtras orderedExtra) {
        orderedExtras.add(orderedExtra);
    }

    public void deleteFromOrderedExtras(OrderedExtras orderedExtra) {
        orderedExtras.remove(orderedExtra);
    }

    public OrderElement(Instant dateEvent, float duration, long people, long quantity, String statusOrder, String address, String eventTitle, String comment) {
        this.dateEvent = dateEvent;
        this.duration = duration;
        this.people = people;
        this.quantity = quantity;
        this.statusOrder = statusOrder;
        this.address = address;
        this.eventTitle = eventTitle;
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderElement that = (OrderElement) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}













