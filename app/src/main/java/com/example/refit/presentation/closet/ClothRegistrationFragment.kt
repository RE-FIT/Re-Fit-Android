package com.example.refit.presentation.closet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.refit.R
import com.example.refit.databinding.FragmentClothRegistrationBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.DialogUtil.showsClothRegisterPhotoDialog
import com.example.refit.presentation.common.NavigationUtil.popBackStack
import com.example.refit.presentation.dialog.closet.ClothRegisterPhotoDialogListener
import com.example.refit.util.FileUtil
import timber.log.Timber

class ClothRegistrationFragment :
    BaseFragment<FragmentClothRegistrationBinding>(R.layout.fragment_cloth_registration) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleAddClothPhoto()
    }

    private fun handleAddClothPhoto() {
        binding.cvClothRegisterPhotoContainer.setOnClickListener {
            showsClothRegisterPhotoDialog(object : ClothRegisterPhotoDialogListener {
                override fun onClickTakePhoto() {
                    openCamera()
                }

                override fun onClickGallery() {
                    openGallery()
                }
            })
        }
    }

    private fun openGallery() {
        val pickMultipleMedia =
            registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(1)) { uris ->
                if (uris.isNotEmpty()) {
                    binding.photoUri = uris[0].toString()
                } else {
                    Timber.d("PhotoPicker : No media selected")
                }
            }
        pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun openCamera() {

    }

}