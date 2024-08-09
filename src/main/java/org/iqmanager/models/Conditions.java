package org.iqmanager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

/** Доп. информация к объявлению */

@Entity
@Table(name = "conditions")
@Getter
@Setter
@NoArgsConstructor
public class Conditions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    /** Минимальное время */
    @Column(name = "min_time")
    private byte minTime;

    /** Максимальное время */
    @Column(name = "max_time")
    private byte maxTime;

    /** Радиус для доствки */
    @Column(name = "radius")
    private long radius;

    /** Минимальное колиество людей */
    @Column(name = "min_person")
    private byte minPerson;

    /** Максимальное количество людей */
    @Column(name = "max_person")
    private byte maxPerson;

    /** Доп. цена */
    @Column(name = "additional_price")
    private long additionalPrice;

    /** Нужно ли спрашивать аддрес */
    @Column(name ="ask_for_the_address")
    private boolean askForTheAddress;

    /** Нужно ли спрашивать количество людей */
    @Column(name ="ask_the_number_of_people")
    private boolean askTheNumberOfPeople;

    /** Нужно ли спрашивать длительность */
    @Column(name ="ask_the_duration")
    private boolean askTheDuration;

    @Column(name = "visible") // значит исполнитель заплатил, и его номер видно
    private boolean visible;

    @Column(name = "ransom_price") // цена покупки номера для заказчика
    private long ransomPrice;

    /** Объявление */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn (name="post_id", referencedColumnName = "id")
    @JsonIgnore
    private Post post;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conditions that = (Conditions) o;
        return id == that.id && minTime == that.minTime && maxTime == that.maxTime && radius == that.radius && minPerson == that.minPerson && maxPerson == that.maxPerson && additionalPrice == that.additionalPrice && askForTheAddress == that.askForTheAddress && askTheNumberOfPeople == that.askTheNumberOfPeople && askTheDuration == that.askTheDuration;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, minTime, maxTime, radius, minPerson, maxPerson, additionalPrice, askForTheAddress, askTheNumberOfPeople, askTheDuration);
    }

    @Override
    public String toString() {
        return "Conditions{" +
                "id=" + id +
                ", minTime=" + minTime +
                ", maxTime=" + maxTime +
                ", radius=" + radius +
                ", minPerson=" + minPerson +
                ", maxPerson=" + maxPerson +
                ", additionalPrice=" + additionalPrice +
                ", askForTheAddress=" + askForTheAddress +
                ", askTheNumberOfPeople=" + askTheNumberOfPeople +
                ", askTheDuration=" + askTheDuration +
                '}';
    }
}
