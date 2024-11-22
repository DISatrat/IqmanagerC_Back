package org.iqmanager.models.enum_models;
/** Статусы заказа */
public enum StatusOrder {


    /** Ждет ответа исполнителя */
    WAITING_PERFORMER,

    /** Ожидание оплаты */
    WAITING_PAYMENT,

    /** Внесен аванс, ожидание полной оплаты*/
    ADVANCE_PAID,

    /** Ожидает исполнения, заказ полностью оплачен */
    WAITING_EXECUTION,

    /** Исполнено */
    EXECUTED,
    EXECUTED_REVIEWED, // выполнено, отзыв оставлен

    /** Просрочена оплата после внесения аванса */
    CANCELLED_ADVANCE_OVERDUE,

    /** Небыли внесены аванс или полная стоимость*/
    CANCELLED_NOT_PAID,

    /** Отказано исполнителем */
    CANCELLED_BY_PERFORMER,

    /** Подана заявка на возврат средств */
    REFUND_REQUESTED,

    /**Выполнен и оплачен iqmanager**/
    EXECUTED_AND_PAID
}
