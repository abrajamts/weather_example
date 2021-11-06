package com.abrahammontes.prueba.data.dummy

import android.content.res.AssetManager
import java.net.URL

class DummyCacheImpl constructor(private val assets: AssetManager) : DummyCache {

    override fun getDummy(url: URL): String {
        val path = url.path.split("/".toRegex()).last()
        return assets.open("dummy/$path.json").bufferedReader().use {
            it.readText()
        }
    }
}