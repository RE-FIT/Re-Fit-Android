package com.example.refit.presentation.closet

import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
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
import java.util.Date

class ClothRegistrationFragment :
    BaseFragment<FragmentClothRegistrationBinding>(R.layout.fragment_cloth_registration) {

    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var takePicture: ActivityResultLauncher<Uri>
    private var photoUri: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.photoUri = uri.toString()
                    binding.ivClothRegisterSelectedCloth.visibility = View.VISIBLE
                } else {
                    Timber.d("PhotoPicker : No media selected")
                }
            }

        takePicture =
            registerForActivityResult(ActivityResultContracts.TakePicture()) {
                if(it) {
                    photoUri?.let {
                        binding.photoUri = photoUri.toString()
                        binding.ivClothRegisterSelectedCloth.visibility = View.VISIBLE
                    }
                }
        }

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
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun openCamera() {
        photoUri = FileUtil.createImageFile(requireActivity())
        takePicture.launch(photoUri)
    }
}