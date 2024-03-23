package com.base.util

import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.FragmentActivity
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KeyboardUtil @Inject constructor(var inputMethodManager: InputMethodManager) {

    fun closeKeyboard(activity: FragmentActivity) {
        try {
            val context = WeakReference<FragmentActivity>(activity)
            if (context.get() != null) {
                //receiving current window focus
                var view: View? = context.get()!!.currentFocus
                if (view == null)
                //create view object
                    view = View(context.get()!!)
                //hide keyboard
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showKeyboard() = try {
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    } catch (e: Exception) {
        e.printStackTrace()
    }


    protected fun activateHideKeyboardUponTouchingScreen(view: View, fragmentActivity: FragmentActivity) {
        if (view !is EditText) {
            view.setOnTouchListener { _, _ ->
                closeKeyboard(fragmentActivity!!)
                return@setOnTouchListener false
            }
        }
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val v = view.getChildAt(i)
                activateHideKeyboardUponTouchingScreen(view, fragmentActivity)
            }
        }
    }
}