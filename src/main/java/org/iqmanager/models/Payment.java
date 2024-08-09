package org.iqmanager.models;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

/** Модель данных оплаты */

@Entity
@Table(name = "payment")
@Getter
@Setter
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    /** Дата оплаты */
    @Column(name = "pay_date")
    private Instant payDate;

    /** Метод оплаты */
    @Column(name = "payment_method")
    private String paymentMethod;

    /** Номер транзакции */
    @Column(name="payment_number")
    private String paymentNumber;

//    номер документа

    @Column(name = "document_number")
    private String documentNumber;

//    статус платежа

    @Column(name = "payment_status")
    private String paymentStatus;

    /** Сумма оплаты */
    @Column(name="price")
    private long price;

    @Column(name = "paid_interest")
    private double paidInterest;

    /** Заказ */
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_element_id", referencedColumnName = "id")
    private OrderElement orderElement;

    public Payment(Instant payDate, String paymentMethod, String paymentNumber, long price, OrderElement orderElement) {
        this.payDate = payDate;
        this.paymentMethod = paymentMethod;
        this.paymentNumber = paymentNumber;
        this.price = price;
        this.orderElement = orderElement;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", payDate=" + payDate +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentNumber='" + paymentNumber + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return id == payment.id && price == payment.price && Objects.equals(payDate, payment.payDate) && Objects.equals(paymentMethod, payment.paymentMethod) && Objects.equals(paymentNumber, payment.paymentNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, payDate, paymentMethod, paymentNumber, price);
    }
}
