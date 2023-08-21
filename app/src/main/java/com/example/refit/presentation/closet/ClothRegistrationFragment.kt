package com.example.refit.presentation.closet

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.refit.R
import com.example.refit.databinding.FragmentClothRegistrationBinding
import com.example.refit.presentation.closet.viewmodel.ClothAddViewModel
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.CustomSnackBar
import com.example.refit.presentation.common.DialogUtil.createAlertBasicDialog
import com.example.refit.presentation.common.DialogUtil.showClothRegisterPhotoDialog
import com.example.refit.presentation.common.DropdownMenuManager
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.dialog.AlertBasicDialogListener
import com.example.refit.presentation.dialog.closet.ClothRegisterPhotoDialogListener
import com.example.refit.util.EventObserver
import com.example.refit.util.FileUtil
import com.google.android.material.chip.Chip
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber
import java.io.File


class ClothRegistrationFragment :
    BaseFragment<FragmentClothRegistrationBinding>(R.layout.fragment_cloth_registration) {

    private val clothAddViewModel: ClothAddViewModel by sharedViewModel()

    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var takePicture: ActivityResultLauncher<Uri>
    private var photoUri: Uri? = null
    private var requestedClothIdForUpdate: Long? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if(photoUri == null && binding.cgClothRegisterWearingSeason.checkedChipId == -1) {
                        navigate(R.id.action_clothRegistrationFragment_to_nav_closet)
                    } else {
                        createAlertBasicDialog(
                            resources.getString(R.string.cloth_register_cancel_title),
                            resources.getString(R.string.cloth_register_cancel_positive),
                            resources.getString(R.string.cloth_register_cancel_negative),
                            object : AlertBasicDialogListener {
                                override fun onClickPositive() {
                                    clothAddViewModel.initAllStatus()
                                    navigate(R.id.action_clothRegistrationFragment_to_nav_closet)
                                }

                                override fun onClickNegative() {
                                }
                            }).show(requireActivity().supportFragmentManager, null)
                    }
                }
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = clothAddViewModel
        initGalleryLauncher()
        initCameraLauncher()
        handleAddClothPhoto()
        handleSelectSeasonCategory()
        handleClickWearingMonthOption()
        handleClickInvalidSeasonConfirm()
        handleClickWearingNumberOption()
        handleClickAddCompleteButton()
        handleSelectedClothCategory()
        handleFixClothInfo()

        clothAddViewModel.registeredClothId.observe(viewLifecycleOwner, EventObserver {
            CustomSnackBar.make(
                binding.root,
                R.layout.custom_snack_bar_basic,
                R.anim.anim_show_snack_bar_from_bottom
            ).setTitle("옷 등록을 완료했습니다!", null).show()
            navigate(R.id.action_clothRegistrationFragment_to_nav_closet)
        })

        clothAddViewModel.isSuccessUpdatingClothInfo.observe(
            viewLifecycleOwner,
            EventObserver { isSuccess ->
                if (isSuccess) {
                    CustomSnackBar.make(
                        binding.root,
                        R.layout.custom_snack_bar_basic,
                        R.anim.anim_show_snack_bar_from_bottom
                    ).setTitle("정보가 수정됐습니다", null).show()
                    navigate(R.id.action_clothRegistrationFragment_to_nav_closet)
                }
            })
    }

    private fun handleClickAddCompleteButton() {
        binding.btnClothRegisterComplete.setOnClickListener {
            if (photoUri != null) {
                clothAddViewModel.requestRegisteringCloth(
                    File(FileUtil.convertResizeImage(requireActivity(), photoUri!!)!!),
                    null
                )
            } else {
                clothAddViewModel.requestRegisteringCloth(null, requestedClothIdForUpdate?.toInt())
            }
        }
    }

    private fun handleSelectSeasonCategory() {
        binding.cgClothRegisterWearingSeason.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.size > 0) {
                val checkedSeason = binding.root.findViewById<Chip>(checkedIds[0])
                clothAddViewModel.checkSeasonValidation(
                    checkedSeason.text.toString(),
                    resources.getStringArray(R.array.cloth_register_wearing_season).toList()
                )
            }
        }
    }

    private fun handleSelectedClothCategory() {
        binding.cgClothRegisterCategory.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.size > 0) {
                val checkedClothCategory = binding.root.findViewById<Chip>(checkedIds[0])
                clothAddViewModel.setClothCategory(
                    resources.getStringArray(R.array.closet_cloth_category).toList(),
                    checkedClothCategory.text.toString()
                )
            }
        }
    }

    private fun handleClickInvalidSeasonConfirm() {
        binding.cgClothRegisterSeasonConfirm.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.size > 0) {
                val checkedResponse = binding.root.findViewById<Chip>(checkedIds[0])
                clothAddViewModel.checkInvalidSeasonConfirmResponse(
                    checkedResponse.text.toString(),
                    resources.getStringArray(R.array.cloth_register_invalid_season_confirm).toList()
                )
            }
        }
    }

    // ----------------------- 목표 착용 횟수 -----------------------

    private fun handleClickWearingMonthOption() {
        binding.cvClothRegisterWearingGoalOptionFirstContainer.setOnClickListener {
            val popupMenu = DropdownMenuManager.createPopupMenu(
                it,
                R.style.ListPopupMenuWindow_ClothRegister,
                R.layout.list_popup_window_item_window_dark,
                R.array.cloth_register_wearing_month_option
            )
            popupMenu.setOnItemClickListener { _, view, _, _ ->
                val itemDescription = (view as TextView).text.toString()
                clothAddViewModel.setWearingGoalMonthOptionStatus(true, itemDescription)
                popupMenu.dismiss()
            }
            popupMenu.show()
        }
    }

    private fun handleClickWearingNumberOption() {
        binding.cvClothRegisterWearingGoalOptionSecondContainer.setOnClickListener {
            val popupMenu = DropdownMenuManager.createPopupMenu(
                it,
                R.style.ListPopupMenuWindow_ClothRegister,
                R.layout.list_popup_window_item_window_dark,
                getWearingGoalNumberList()
            )
            popupMenu.setOnItemClickListener { _, view, _, _ ->
                val itemDescription = (view as TextView).text.toString()
                clothAddViewModel.setWearingGoalNumberOptionStatus(true, itemDescription)
                popupMenu.dismiss()
            }
            popupMenu.show()
        }
    }

    private fun getWearingGoalNumberList(): Int {
        return when (binding.tvClothRegisterWearingGoalOptionFirst.text.toString()) {
            resources.getString(R.string.cloth_register_wearing_goal_one_month) -> {
                R.array.cloth_register_wearing_number_by_one_month
            }

            resources.getString(R.string.cloth_register_wearing_goal_two_month) -> {
                R.array.cloth_register_wearing_number_by_two_month
            }

            else -> {
                R.array.cloth_register_wearing_number_by_three_month
            }
        }
    }


    // ----------------------- 사진 및 카메라 통한 옷 이미지 등록 -----------------------

    private fun handleAddClothPhoto() {
        binding.cvClothRegisterPhotoContainer.setOnClickListener {
            showClothRegisterPhotoDialog(object : ClothRegisterPhotoDialogListener {
                override fun onClickTakePhoto() {
                    photoUri = FileUtil.createImageFile(requireActivity())
                    takePicture.launch(photoUri)
                }

                override fun onClickGallery() {
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
            }).show(requireActivity().supportFragmentManager, "ClothRegisterPhotoDialog")
        }
    }

    private fun initGalleryLauncher() {
        pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.photoUri = uri.toString()
                    photoUri = uri
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
                    }
                } else {
                    Timber.d("사진 가져오기 실패")
                }
            }
    }

    // ----------------------- 옷 재등록 -----------------------

    private fun handleFixClothInfo() {
        clothAddViewModel.requestedFixClothInfo.observe(
            viewLifecycleOwner,
            EventObserver { clothInfo ->
                binding.photoUri = clothInfo.imageUrl
                (binding.cgClothRegisterCategory.getChildAt(clothInfo.category) as Chip).isChecked =
                    true
                (binding.cgClothRegisterWearingSeason.getChildAt(clothInfo.season) as Chip).isChecked =
                    true
                requestedClothIdForUpdate = clothInfo.id
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        clothAddViewModel.initAllStatus()
    }

}