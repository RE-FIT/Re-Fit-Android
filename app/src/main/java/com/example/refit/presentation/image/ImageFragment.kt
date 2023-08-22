package com.example.refit.presentation.image

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.refit.R
import com.example.refit.databinding.FragmentImageBinding
import com.example.refit.presentation.common.BaseFragment

class ImageFragment : BaseFragment<FragmentImageBinding>(R.layout.fragment_image) {

    val args: ImageFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val url = args.imageUrl.toString()

        Glide.with(binding.root)
            .load(url)
            .into(binding.image)
    }
}