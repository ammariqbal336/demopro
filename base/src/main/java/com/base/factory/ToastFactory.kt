package com.base.factory

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.base.R
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class ToastFactory @Inject constructor(@ApplicationContext private var context: Context) {

    private lateinit var toast: Toast
    private lateinit var view: View

    fun create(message: String, id: Int? = 0, duration: Int = Toast.LENGTH_SHORT) {

        if (::toast.isInitialized && toast.view?.isShown!!) toast.cancel()

        toast = Toast(context)

        if (!::view.isInitialized)
            view = LayoutInflater.from(context).inflate(R.layout.toast_layout, null)

        toast.view = view
        toast.duration = duration
        toast.view?.findViewById<TextView>(R.id.message)?.setText(message)
        if (id != null && id != 0) {
            toast.view?.findViewById<ImageView>(R.id.icon)?.visibility = View.VISIBLE
            toast.view?.findViewById<ImageView>(R.id.icon)?.setImageResource(id!!)
        } else {
            toast.view?.findViewById<ImageView>(R.id.icon)?.visibility = View.GONE
        }
        toast.show()

    }
}