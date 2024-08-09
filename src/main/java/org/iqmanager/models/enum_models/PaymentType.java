package org.iqmanager.models.enum_models;

public enum PaymentType {
    /** Фиксированная оплата */
    FIX,

    /** Оплата за каждого человека */
    PEOPLE,

    /** Оплата за час */
    HOURS,

    /** Фиксированная + за час */
    HOURS_AND_FIX
}
