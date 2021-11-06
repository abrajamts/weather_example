package com.abrahammontes.prueba.data.dummy

import java.net.URL

interface DummyCache {
    fun getDummy(url: URL) : String
}