package com.abrahammontes.prueba.activities.base

import androidx.appcompat.app.AppCompatActivity
import com.abrahammontes.prueba.PruebaApplication
import com.abrahammontes.prueba.common.GeneralViewModel
import com.abrahammontes.prueba.dialogs.LoadingDialog
import org.koin.java.KoinJavaComponent.inject

abstract class BaseWeatherActivity : AppCompatActivity() {

    protected val app : PruebaApplication = PruebaApplication().getInstance()

    protected var viewModel: Lazy<GeneralViewModel> = inject(GeneralViewModel::class.java)

    fun showProgressDialog() {
        LoadingDialog.Companion.show(supportFragmentManager)
    }

    fun dismissProgressDialog() {
        LoadingDialog.Companion.hide(supportFragmentManager)
    }
}