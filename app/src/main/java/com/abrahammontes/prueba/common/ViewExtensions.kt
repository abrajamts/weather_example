package com.abrahammontes.prueba.common

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel

fun AndroidViewModel.getMessageForException(e: Throwable) =
    getApplication<Application>().getMessageForException(e)

fun Context.getMessageForException(e: Throwable) = e.message