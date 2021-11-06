package com.abrahammontes.prueba.data.factory

interface ApiService {
    fun <T> create(service: Class<T>) : T
}