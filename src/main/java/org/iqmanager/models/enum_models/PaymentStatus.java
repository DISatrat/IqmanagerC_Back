package org.iqmanager.models.enum_models;

public enum PaymentStatus {

    /** Платеж создан и ожидает действий от пользователя */
    pending,

    /** Платеж оплачен, деньги авторизованы и ожидают списания */
    waiting_for_capture,

    /** Успешный */
    succeeded,

    /** Отклонён */
    canceled
}
