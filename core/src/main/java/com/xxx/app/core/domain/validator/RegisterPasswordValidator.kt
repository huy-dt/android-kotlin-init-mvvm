package com.xxx.app.core.domain.validator

import com.xxx.app.core.domain.exception.auth.WeakPasswordException

object RegisterPasswordValidator {

    fun validate(password: String) {
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasDigit = password.any { it.isDigit() }

        if (!hasUpperCase || !hasDigit) {
            throw WeakPasswordException()
        }
    }
}
