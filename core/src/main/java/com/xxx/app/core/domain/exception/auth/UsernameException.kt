package com.xxx.app.core.domain.exception.auth

import com.xxx.app.core.domain.exception.DomainException

class UsernameEmptyException :
    DomainException("Username is empty")

class UsernameTooShortException :
    DomainException("Username is too short")

class UsernameTooLongException :
    DomainException("Username is too long")

class InvalidUsernameException :
    DomainException("Invalid username")

class UsernameAlreadyExistsException :
    DomainException("Username already exists")
