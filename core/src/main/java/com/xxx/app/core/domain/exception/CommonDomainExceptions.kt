package com.xxx.app.core.domain.exception

class InvalidInputException(
    message: String = "Invalid input"
) : DomainException(message)

class NotFoundException(
    message: String = "Resource not found"
) : DomainException(message)

class AlreadyExistsException(
    message: String = "Resource already exists"
) : DomainException(message)

class UnauthorizedException(
    message: String = "Unauthorized"
) : DomainException(message)

class ForbiddenException(
    message: String = "Forbidden"
) : DomainException(message)
