package com.xxx.app.core.domain.exception.auth

import com.xxx.app.core.domain.exception.DomainException

class PhoneEmptyException : DomainException("Phone number is required")
class PhoneInvalidException : DomainException("Invalid phone number format")
class PhoneNotVerifiedException : DomainException("Phone number is not verified")
