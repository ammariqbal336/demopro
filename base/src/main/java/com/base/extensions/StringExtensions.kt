package com.base.extensions

import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.view.View
import androidx.core.content.ContextCompat

/*
* Extension functions to SpannableString class
* */

//adding underline to spannable string
fun SpannableString.applyUnderline(start: Int, end: Int): SpannableString {
    this.setSpan(UnderlineSpan(), start, end, 0)
    return this
}

//appling bold to spanable string
fun SpannableString.applyBold(start: Int, end: Int): SpannableString {
    this.setSpan(StyleSpan(Typeface.BOLD), start, end, 0)
    return this
}

//apply color to spannable string
fun SpannableString.applyColor(color: String, start: Int, end: Int): SpannableString {
    this.setSpan(ForegroundColorSpan(Color.parseColor(color)), start, end, 0)
    return this
}

//apply color to spannable string
fun SpannableString.applyColor(color: Int, start: Int, end: Int): SpannableString {
    this.setSpan(ForegroundColorSpan(color), start, end, 0)
    return this
}

fun SpannableString.applyClick(start: Int, end: Int, onClick: (View) -> Unit): SpannableString {
    this.setSpan(object : ClickableSpan() {
        override fun onClick(widget: View) = onClick(widget)
        override fun updateDrawState(ds: TextPaint) {
            ds.bgColor = Color.TRANSPARENT
            ds.isFakeBoldText = true
            ds.isUnderlineText = true
        }
    }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    return this
}
