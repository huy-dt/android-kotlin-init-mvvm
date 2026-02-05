package com.xxx.app.core.domain.valueobject

import com.xxx.app.core.domain.exception.auth.*

@JvmInline
value class Username private constructor(val value: String) {

    companion object {
        operator fun invoke(raw: String): Username {
            val value = raw.trim()

            if (value.isEmpty())
                throw UsernameEmptyException()

            if (value.length < 3)
                throw UsernameTooShortException()

            if (value.length > 30)
                throw UsernameTooLongException()

            // future rule
            // if (!value.matches(Regex("^[a-zA-Z0-9_]+$")))
            //     throw InvalidUsernameException()

            return Username(value)
        }
    }
}
