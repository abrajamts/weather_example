package com.abrahammontes.prueba.data.checks.exception

import java.io.IOException

data class NetworkException(
    override val message: String,
    override val cause: Throwable?
) : IOException(message, cause)