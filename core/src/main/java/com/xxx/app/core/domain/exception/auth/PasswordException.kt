package com.xxx.app.core.domain.exception.auth

import com.xxx.app.core.domain.exception.DomainException

class PasswordEmptyException :
    DomainException("Password is empty")

class PasswordTooShortException :
    DomainException("Password is too short")

class WeakPasswordException :
    DomainException("Password is too weak")
