package com.xxx.app.core.domain.valueobject

@JvmInline
value class Timestamp(val value: Long) {
    init {
        require(value > 0)
    }
}
