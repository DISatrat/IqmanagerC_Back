package org.iqmanager.models.enum_models;
/** Варианты цены за доставку */

public enum DeliveryType {

    /** Без доставки */
    NO_DELIVERY,

    /** Бесплатно */
    FREE_UNLIMITED,

    /** Бесплатно в пределах радиуса, далее плата за километр */
    FREE_LIMITED_KILOMETER,

    /** Бесплатно в пределах радиуса, далее фиксированная плата */
    FREE_LIMITED_FIXED,

    /**  Оплата за километр */
    PER_KILOMETER,

    /** Фиксированная оплата */
    FIXED,

    /** Фиксированная оплата + за километр */
    FIXED_AND_PER_KILOMETER,

    /** Фиксированная оплата в пределах радиуса, далее + фиксированная плата */
    FIXED_PLUS_FIXED
}
