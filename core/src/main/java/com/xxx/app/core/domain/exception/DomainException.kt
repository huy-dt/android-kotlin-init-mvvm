package com.xxx.app.core.domain.exception

open class DomainException(
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)
