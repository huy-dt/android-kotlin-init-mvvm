package com.xxx.app.core.domain.valueobject

@JvmInline
value class Id(val value: Long) {
    init {
        require(value > 0)
    }
}
