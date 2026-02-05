package com.xxx.app.core.domain.valueobject

import java.math.BigDecimal

data class Money(
    val amount: BigDecimal,
    val currency: String
) {
    init {
        require(amount >= BigDecimal.ZERO)
        require(currency.length == 3)
    }
}
