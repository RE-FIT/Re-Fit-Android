package com.example.refit.presentation.common.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import retrofit2.http.Url
import java.net.URL

object ImageBinding {

    @JvmStatic
    @BindingAdapter("setImage")
    fun setImage(view: ImageView, url: String?) {
        url?.let {
            Glide.with(view)
                .load(url)
                .into(view)
        }
    }
}