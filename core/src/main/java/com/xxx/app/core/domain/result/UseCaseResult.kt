package com.xxx.app.core.domain.result

import com.xxx.app.core.domain.exception.DomainException

sealed class UseCaseResult<out T> {
    data class Success<T>(val data: T) : UseCaseResult<T>()
    data class Error(val exception: DomainException) : UseCaseResult<Nothing>()
}
