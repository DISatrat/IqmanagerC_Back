package org.iqmanager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.time.Instant;

/** Модель календаря исполнителя */

@Entity
@Table(name = "calendar")
@Getter
@Setter
@NoArgsConstructor
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    /** Начало собтия */
    @Column(name = "begin_date")
    Instant beginDate;

    /** Конец события */
    @Column(name = "end_date")
    Instant endDate;

    /** Изменение цены */
    @Column(name = "change_price")
    byte changePrice;

    /** Статус */
    @Column(name = "status")
    String status;

    @Column(name = "used")
    int used = 0;

    /** Исполнитель */
    @ManyToOne
    @JoinColumn(name = "performer_id")
    @JsonIgnore
    PerformerData performer;

    @OneToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private OrderElement orderElement;
}
