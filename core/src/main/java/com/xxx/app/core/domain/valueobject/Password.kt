package com.xxx.app.core.domain.valueobject

import com.xxx.app.core.domain.exception.auth.*

@JvmInline
value class Password(val value: String) {
    init {
        if (value.isBlank())
            throw PasswordEmptyException()

        if (value.length < 6)
            throw PasswordTooShortException()
    }
}
