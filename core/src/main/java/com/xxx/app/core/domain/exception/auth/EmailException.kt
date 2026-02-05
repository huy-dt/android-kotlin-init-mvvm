package com.xxx.app.core.domain.exception.auth

import com.xxx.app.core.domain.exception.DomainException

class EmailEmptyException :
    DomainException("Email is empty")

class InvalidEmailException :
    DomainException("Invalid email format")

class EmailAlreadyExistsException :
    DomainException("Email already exists")
