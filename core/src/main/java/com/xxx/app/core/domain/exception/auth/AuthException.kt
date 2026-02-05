package com.xxx.app.core.domain.exception.auth

import com.xxx.app.core.domain.exception.DomainException

class InvalidCredentialsException :
    DomainException("Invalid username or password")

class UserNotFoundException :
    DomainException("User not found")

class UnauthorizedException :
    DomainException("Unauthorized")

class UserAlreadyRegisteredException :
    DomainException("User already registered")

class AccountDisabledException :
    DomainException("Account is disabled")

