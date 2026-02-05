package com.xxx.app.core.domain.valueobject

@JvmInline
value class PhoneNumber(val value: String) {
    init {
        require(value.matches(Regex("^\\+?[0-9]{9,15}$")))
    }
}
