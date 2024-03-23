package com.presenter.utils

import android.graphics.*
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.telephony.PhoneNumberUtils
import android.text.Html
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.google.android.material.textfield.TextInputEditText
import com.presenter.R
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*



@BindingAdapter(value = ["imageUrl_default_icon"])
fun loadImage_default_icon(view: ImageView, imageUrl_default_icon: String?) {
    try {
        val url = GlideUrl(
            imageUrl_default_icon, LazyHeaders.Builder()
                .addHeader("User-Agent", "your-user-agent")
                .build()
        )
        Log.d("url Image", imageUrl_default_icon ?: "")
        Glide.with(view.context)
            .load(url ?: "")
            .placeholder(R.drawable.default_contacticon)

            .into(view)
    } catch (e: Exception) {
        Glide.with(view.context)
            .load(imageUrl_default_icon ?: "")
            .placeholder(R.drawable.default_contacticon)
            .into(view)
        Log.d("url Image catch", imageUrl_default_icon ?: "")
    }

}






@BindingAdapter("bind:parse_time")
fun TextView.parseTime(time: String?) {
    try {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        var convertedDate: Date? = null
        convertedDate = sdf.parse(time)
        val standardDateFormat = SimpleDateFormat("hh:mm a")
        text = standardDateFormat.format(convertedDate)
    } catch (e: Exception) {
        e.printStackTrace()
        text = "-"
    }
}












