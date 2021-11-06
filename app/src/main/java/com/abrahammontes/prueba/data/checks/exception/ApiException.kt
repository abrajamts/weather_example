package com.abrahammontes.prueba.data.checks.exception

import java.io.IOException

data class ApiException(
    val clave: String?,
    override var message: String?
) : IOException(message.orEmpty())