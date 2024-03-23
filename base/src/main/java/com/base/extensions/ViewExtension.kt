package com.base.extensions

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.base.util.SafeClickListener

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}





fun EditText.onTextChanged(listener: (String) -> Unit) {


    this.addTextChangedListener(object : TextWatcher {


        override fun afterTextChanged(s: Editable?) {


            listener(s.toString())


        }


        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}


        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}


    })
}

