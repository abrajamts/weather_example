package com.abrahammontes.prueba.dialogs

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.abrahammontes.prueba.R

class LoadingDialog : DialogFragment() {
    private var mView: View? = null
    private var loadingView : ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_Loading)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
            .apply {
                setCancelable(false)
                setCanceledOnTouchOutside(false)
                setOnKeyListener { _, _, _ -> true }
            }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = mView ?: inflater.inflate(
        R.layout.fragment_loading, null, false) ?: super.onCreateView(inflater, container, savedInstanceState)

    companion object {
        const val TAG = "LoadingDialog"
        fun show(fm: FragmentManager) {
            if (fm.findFragmentByTag(TAG) == null) {
                try {
                    LoadingDialog().show(fm, TAG)
                } catch (e: Exception) {
                    Log.e(TAG, e.message, e.cause)
                }
            }
        }

        fun hide(fm: FragmentManager) {
            try {
                (fm.findFragmentByTag(TAG) as? LoadingDialog)?.dismiss()
            } catch (e: Exception) {
                Log.e(TAG, e.message, e.cause)
            }
        }
    }
}