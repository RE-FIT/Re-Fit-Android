package com.example.refit.presentation.closet

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.refit.R
import com.example.refit.databinding.FragmentClothRegistrationBinding
import com.example.refit.presentation.closet.viewmodel.ClosetViewModel
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.DialogUtil.showsClothRegisterPhotoDialog
import com.example.refit.presentation.common.DropdownMenuManager
import com.example.refit.presentation.dialog.closet.ClothRegisterPhotoDialogListener
import com.example.refit.util.FileUtil
import com.google.android.material.chip.Chip
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class ClothRegistrationFragment :
    BaseFragment<FragmentClothRegistrationBinding>(R.layout.fragment_cloth_registration) {

    private val closetViewModel: ClosetViewModel by sharedViewModel()

    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var takePicture: ActivityResultLauncher<Uri>
    private var photoUri: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = closetViewModel
        initGalleryLauncher()
        initCameraLauncher()
        handleAddClothPhoto()
        handleSelectSeasonCategory()
        handleClickWearingMonthOption()
        handleClickInvalidSeasonConfirm()
        handleClickWearingGoalOptionSecond()
    }

    private fun handleClickWearingGoalOptionSecond() {
        binding.etClothRegisterWearingGoalOptionSecond.setOnFocusChangeListener { _, isFocus ->
            closetViewModel.setWearingGoalNumberOptionStatus(isFocus)
        }
    }

    private fun handleSelectSeasonCategory() {
        binding.cgClothRegisterWearingSeason.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.size > 0) {
                val checkedSeason = binding.root.findViewById<Chip>(checkedIds[0])
                closetViewModel.checkSeasonValidation(
                    checkedSeason.text.toString(),
                    resources.getStringArray(R.array.cloth_register_wearing_season).toList()
                )
            }
        }
    }

    private fun handleClickInvalidSeasonConfirm() {
        binding.cgClothRegisterSeasonConfirm.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.size > 0) {
                val checkedResponse = binding.root.findViewById<Chip>(checkedIds[0])
                closetViewModel.checkInvalidSeasonConfirmResponse(
                    checkedResponse.text.toString(),
                    resources.getStringArray(R.array.cloth_register_invalid_season_confirm).toList()
                )
            }
        }
    }

    private fun handleClickWearingMonthOption() {
        binding.cvClothRegisterWearingGoalOptionFirstContainer.setOnClickListener {
            val popupMenu = DropdownMenuManager.createPopupMenu(
                requireActivity(),
                it,
                R.style.ListPopupMenuWindow_ClothRegister,
                R.layout.list_popup_window_item_window_dark,
                R.array.cloth_register_wearing_month_option
            )
            popupMenu.setOnItemClickListener { _, view, _, _ ->
                val itemDescription = (view as TextView).text.toString()
                closetViewModel.setWearingGoalMonthOptionStatus(true, itemDescription)
                popupMenu.dismiss()
            }
            popupMenu.show()
        }
    }

    private fun handleAddClothPhoto() {
        binding.cvClothRegisterPhotoContainer.setOnClickListener {
            showsClothRegisterPhotoDialog(object : ClothRegisterPhotoDialogListener {
                override fun onClickTakePhoto() {
                    photoUri = FileUtil.createImageFile(requireActivity())
                    takePicture.launch(photoUri)
                }

                override fun onClickGallery() {
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
            })
        }
    }

    private fun initGalleryLauncher() {
        pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.photoUri = uri.toString()
                    binding.ivClothRegisterSelectedCloth.visibility = View.VISIBLE
                } else {
                    Timber.d("선택된 사진이 없음")
                }
            }
    }

    private fun initCameraLauncher() {
        takePicture =
            registerForActivityResult(ActivityResultContracts.TakePicture()) {
                if (it) {
                    photoUri?.let {
                        binding.photoUri = photoUri.toString()
                        binding.ivClothRegisterSelectedCloth.visibility = View.VISIBLE
                    }
                } else {
                    Timber.d("사진 가져오기 실패")
                }
            }
    }
}