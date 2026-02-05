package com.xxx.app.core.domain.valueobject

@JvmInline
value class Email(val value: String) {
    init {
        require(value.isNotBlank())
        require(value.contains("@"))
    }
}
