package org.iqmanager.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.iqmanager.models.enum_models.PaymentMethod;
import org.iqmanager.models.enum_models.PaymentStatus;

import javax.persistence.*;
import java.math.BigDecimal;
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

    /** Идентификатор платежа от ЮKassa */
    @Column(name = "transaction_id")
    private String transactionId;

    /** Дата оплаты */
    @Column(name = "pay_date")
    private Instant payDate;

    /** Метод оплаты */
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    /** Сумма оплаты */
    @Column(name="price")
    private BigDecimal price;

    /** Сумма, полученная магазином после вычета комиссии */
    @Column(name = "paid_interest")
    private BigDecimal paidInterest;

    /** Статус платежа */
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    /** Признак тестового платежа */
    @Column(name = "is_test")
    private boolean isTest;

    /** Сумма возврата, если был возврат */
    @Column(name = "refunded_amount")
    private BigDecimal refundedAmount;

    /** Описание транзакции */
    @Column(name = "description")
    private String description;

    /** Валюта платежа */
    @Column(name = "currency")
    private String currency;

    /** Дата создания транзакции */
    @Column(name = "created_at")
    private Instant createdAt;

    /**Оплачен ли заказ исполнителю**/
    @Column(name = "is_paid_to_perf")
    private boolean isPaidToPerf;

    /**Чек оплаты исполнителю**/
    @Column(name = "payment_to_perf_pdf")
    private String payToPerfPDF;

    /** Заказ */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_element_id", referencedColumnName = "id")
    @JsonIgnore
    private OrderElement orderElement;

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", transactionId='" + transactionId + '\'' +
                ", payDate=" + payDate +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", price=" + price +
                ", paidInterest=" + paidInterest +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", isTest=" + isTest +
                ", refundedAmount=" + refundedAmount +
                ", description='" + description + '\'' +
                ", currency='" + currency + '\'' +
                ", createdAt=" + createdAt +
                ", orderElement=" + orderElement +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return id == payment.id && isTest == payment.isTest && Objects.equals(transactionId, payment.transactionId)
                && Objects.equals(payDate, payment.payDate) && Objects.equals(paymentMethod, payment.paymentMethod)
                && Objects.equals(price, payment.price) && Objects.equals(paidInterest, payment.paidInterest)
                && Objects.equals(paymentStatus, payment.paymentStatus) && Objects.equals(refundedAmount, payment.refundedAmount)
                && Objects.equals(description, payment.description) && Objects.equals(currency, payment.currency)
                && Objects.equals(createdAt, payment.createdAt) && Objects.equals(orderElement, payment.orderElement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isTest, transactionId, payDate, paymentMethod, price, paidInterest, paymentStatus,
                refundedAmount, description, currency, createdAt, orderElement);
    }
}
