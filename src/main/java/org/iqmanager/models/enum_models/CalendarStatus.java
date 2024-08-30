package org.iqmanager.models.enum_models;

public enum CalendarStatus {

    /** Увелечение цены */
    PRICE_UP,

    /**Снижение цены */
    PRICE_DOWN,

    /** Занят */
    BUSY,

    /**Снижение цены для агента */
    PRICE_DOWN_FOR_AGENT,

    /** на рассмотрении заказа */
    CONSIDERATION_OF_ORDER,

    /** частичная занятость */
    NOT_FULLY_OCCUPIED,

    /** Выходной */
    DAY_OFF
}
