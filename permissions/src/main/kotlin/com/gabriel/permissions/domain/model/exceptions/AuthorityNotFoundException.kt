package com.gabriel.permissions.domain.model.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class AuthorityNotFoundException(message: String) : RuntimeException(message)
