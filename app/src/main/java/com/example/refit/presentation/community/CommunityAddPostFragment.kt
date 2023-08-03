package com.example.refit.presentation.community

import android.annotation.SuppressLint
import android.icu.text.DecimalFormat
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ArrayRes
import androidx.appcompat.app.WindowDecorActionBar.TabImpl
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.content.ContextCompat
import com.example.refit.R
import com.example.refit.databinding.FragmentCommunityAddPostBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.DialogUtil
import com.example.refit.presentation.common.DialogUtil.showCommunityAddShippingFeeDiaglog
import com.example.refit.presentation.common.DropdownMenuManager
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.common.NavigationUtil.navigateUp
import com.example.refit.presentation.community.viewmodel.CommunityAddPostViewModel
import com.example.refit.presentation.dialog.community.CommunityAddShippingFeeDialogListener
import com.example.refit.util.FileUtil
import com.google.android.material.chip.Chip
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class CommunityAddPostFragment :
    BaseFragment<FragmentCommunityAddPostBinding>(R.layout.fragment_community_add_post) {

    private val communityAddPostViewModel: CommunityAddPostViewModel by sharedViewModel()
    private lateinit var pickMultipleMedia: ActivityResultLauncher<PickVisualMediaRequest>

    private var photoUris: List<String>? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cmviewmodel = communityAddPostViewModel
        communityAddPostViewModel.setPriceInputCompleted("")
        CommunityAddContentsOptionDropdown()
        selectTransactionChipType()
        handleAfterInputPrice()
        handledAddShippingFee()
        handlePostcodeSetting()
        handleAddCommunityPhoto()
        handleRegisterButton()
        selectShippingFeeType()
        observeEditTextChanges()
        initGalleryLauncher()
    }


    // 추천 착용 성별, 카테고리, 사이즈, 배송 관련 드롭다운
    private fun CommunityAddContentsOptionDropdown() {
        binding.cvCommunityAddpostRecommendGender.setOnClickListener {
            communityAddPostViewModel.setClickedOptionRG(clicked = true)
            val listPopupWindow = getPopupMenu(it, R.array.community_item_search_option_gender)
            setPopupItemClickListener(it.id, listPopupWindow)
            listPopupWindow.show()
        }

        binding.cvCommunityAddpostClothesCategory.setOnClickListener {
            communityAddPostViewModel.setClickedOptionCategory(clicked = true)
            val listPopupWindow = getPopupMenu(it, R.array.community_item_search_option_category)
            setPopupItemClickListener(it.id, listPopupWindow)
            listPopupWindow.show()
        }

        binding.cvCommuntiyAddpostSize.setOnClickListener {
            communityAddPostViewModel.setClickedOptionSize(clicked = true)
            val listPopupWindow = getPopupMenu(it, R.array.community_item_search_option_size)
            setPopupItemClickListener(it.id, listPopupWindow)
            listPopupWindow.show()
        }

        binding.cvCommunityAddpostTransactionMethod.setOnClickListener {
            communityAddPostViewModel.setClickedOptionTM(clicked = true)
            communityAddPostViewModel.setFilledStatus(5, false, "")
            val listPopupWindow =
                getPopupMenu(it, R.array.community_item_search_option_transaction_method)
            setPopupItemClickListener(it.id, listPopupWindow)
            listPopupWindow.show()
        }
    }

    private fun setPopupItemClickListener(viewId: Int, popupMenu: ListPopupWindow) {
        popupMenu.setOnItemClickListener { _, view, _, _ ->
            val itemDescription = (view as TextView).text.toString()
            when (viewId) {
                binding.cvCommunityAddpostRecommendGender.id -> {
                    binding.tvCommunityAddpostRecommendGender.text = itemDescription
                    binding.cvCommunityAddpostRecommendGender.strokeColor =
                        ContextCompat.getColor(requireContext(), R.color.white)
                    communityAddPostViewModel.setFilledStatus(2, true, itemDescription)
                }

                binding.cvCommunityAddpostClothesCategory.id -> {
                    binding.tvCommunityAddpostClothesCategory.text = itemDescription
                    binding.cvCommunityAddpostClothesCategory.strokeColor =
                        ContextCompat.getColor(requireContext(), R.color.white)
                    communityAddPostViewModel.setFilledStatus(3, true, itemDescription)
                }

                binding.cvCommuntiyAddpostSize.id -> {
                    binding.tvCommuntiyAddpostSize.text = itemDescription
                    binding.cvCommuntiyAddpostSize.strokeColor =
                        ContextCompat.getColor(requireContext(), R.color.white)
                    communityAddPostViewModel.setFilledStatus(4, true, itemDescription)
                }

                binding.cvCommunityAddpostTransactionMethod.id -> {
                    binding.tvCommunityAddpostTransactionMethod.text = itemDescription
                    communityAddPostViewModel.setFilledStatus(5, true, itemDescription)
                    binding.cvCommunityAddpostTransactionMethod.strokeColor =
                        ContextCompat.getColor(requireContext(), R.color.white)
                    communityAddPostViewModel.selectTransactionMethod(itemDescription)
                }
            }
            popupMenu.dismiss()
        }
    }


    private fun getPopupMenu(
        anchorView: View,
        @ArrayRes items: Int,
    ): ListPopupWindow {
        return DropdownMenuManager.createPopupMenu(
            requireActivity(),
            anchorView,
            R.style.ListPopupMenuWindow_CommunityAddPostOption,
            R.layout.list_popup_window_item_window_dark,
            items
        )
    }


    private fun selectTransactionChipType() {
        binding.cgCommunityAddpostMethod.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.size > 0) {
                communityAddPostViewModel.setClickedOptionTM(clicked = false)
                binding.tvCommunityAddpostTransactionMethod.text = "거래 희망 방식"
                val checkedMethodType = binding.root.findViewById<Chip>(checkedIds[0])
                communityAddPostViewModel.checkTransactionType(
                    checkedMethodType.text.toString(),
                    resources.getStringArray(R.array.community_item_search_option_type).toList()
                )
            }

        }
    }

    private fun selectShippingFeeType() {
        binding.rgCommunityAddpostInputFee.setOnCheckedChangeListener { _, id ->
            val type: Boolean = when (id) {
                R.id.rb_community_addpost_input_include_fee -> {
                    communityAddPostViewModel.setFilledStatus(8, true, "")
                    binding.tvCommunityAddpostSf.text = ""
                    binding.tvCommunityAddpostFeeInput.text =
                        getString(R.string.community_addpost_contents_detail_fourth_input)
                    false
                }

                R.id.rb_community_addpost_input_exclude_fee -> {
                    communityAddPostViewModel.setFilledStatus(8, false, "")
                    true
                }
                else -> false
            }
            communityAddPostViewModel.setFilledStatus(10, type, "")
        }
    }

    private fun handledAddShippingFee() {
        binding.tvCommunityAddpostFeeInput.setOnClickListener {
            showCommunityAddShippingFeeDiaglog(object : CommunityAddShippingFeeDialogListener {
                override fun onClickDone(fee: Int) {
                    communityAddPostViewModel.setFilledStatus(8, status = true, "")
                    communityAddPostViewModel.setShippingFee(fee)
                    val feeText = communityAddPostViewModel.getDecimalFormat(fee.toString())
                    binding.tvCommunityAddpostSf.text = feeText + "원"
                }
            }, communityAddPostViewModel).show(
                requireActivity().supportFragmentManager,
                "CommunityAddShippingFeeDialog"
            )
        }
    }

    private fun observeEditTextChanges() {
        binding.etCommunityAddpostTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val isFilled = p0?.isNotEmpty() == true
                communityAddPostViewModel.setFilledStatus(1, isFilled, "")
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        binding.etCommunityAddpostPrice.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                communityAddPostViewModel.setPriceInputCompleted("")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val isFilled = p0?.isNotEmpty() == true
                communityAddPostViewModel.setFilledStatus(6, isFilled, "")
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.etCommunityAddpostDetail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val isFilled = p0?.isNotEmpty() == true
                communityAddPostViewModel.setFilledStatus(7, isFilled, "")
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    private fun handleAfterInputPrice() {
        binding.llCommunityAddpostView.setOnClickListener {
            val inputPriceText = binding.etCommunityAddpostPrice.text.toString()
            binding.tvCommunityAddpostPrice.text =
                "₩ " + communityAddPostViewModel.getDecimalFormat(inputPriceText) + "원"
            communityAddPostViewModel.setPriceInputCompleted(inputPriceText)
        }
    }

    private fun handlePostcodeSetting() {
        binding.cvCommunityAddpostRegion.setOnClickListener {
            // navigate(R.id.action_communityAddPostFragment_to_postcodeFragment)
        }
    }

    private fun handleAddCommunityPhoto() {
        binding.photolen = "0/5"
        binding.ivCommunityImageContainer.setOnClickListener {
            pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
        }
    }

    private fun handleRegisterButton() {
        binding.btnCommunityAddPostRegister.setOnClickListener {
            val title = binding.etCommunityAddpostTitle.text.toString()
            val detail = binding.etCommunityAddpostDetail.text.toString()
            val imageFiles = mutableListOf<File>()
            photoUris?.let {
                for (uriString in it) {
                    val uri = Uri.parse(uriString)
                    val copiedFile = copyFileToInternalStorage(uri)
                    Timber.d("file URI 값 정상 작동되는지 확인 : $uri ================ $copiedFile")

                    copiedFile?.let { file ->
                        if (file.exists()) {
                            imageFiles.add(file)
                        } else {
                            Timber.e("파일이 존재하지 않습니다: $file")
                        }
                    }
                }
            }
            communityAddPostViewModel.createPost(title, detail, imageFiles)
            navigateUp()
        }
    }
    private fun copyFileToInternalStorage(uri: Uri): File? {
        val context = requireContext().applicationContext
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val fileName = getFileName(uri) ?: return null

        // 내부 저장소에 새 파일 생성
        val file = File(context.filesDir, fileName)
        file.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }
        return file
    }

    @SuppressLint("Range")
    private fun getFileName(uri: Uri): String? {
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                if (!displayName.isNullOrEmpty()) {
                    return displayName
                }
            }
        }
        return null
    }


    private fun initGalleryLauncher() {
        pickMultipleMedia =
            registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) { uris ->
                photoUris = null
                if (uris != null) {
                    // 몇몇 기종에서는 5개 선택 제한이 안 되는 이슈 발생 -> 5개 초과로 받아와도 5개까지만 보여주도록 추가 설정
                    val selectedUris = uris.take(5)
                    photoUris = selectedUris.map { uri -> uri.toString() }
                    val len = photoUris!!.size
                    binding.photolen = "$len/5"
                    communityAddPostViewModel.setFilledStatus(0, status = true, "")
                    if (selectedUris.isNotEmpty()) {
                        for (uri in selectedUris) {
                            communityAddPostViewModel.setFilledImage(len)
                            binding.photoUris = photoUris
                        }
                    } else {
                        Log.d("PhotoPicker", "No media selected")
                    }
                } else {
                    photoUris = null
                    Log.d("PhotoPicker", "No media selected")
                }
            }
    }

    private fun uriToFile(uri: Uri): File? {
        val context = requireContext().applicationContext
        val filePathColumn = arrayOf(MediaStore.Images.Media.DISPLAY_NAME)
        Timber.d("filePathColumn Test; ${filePathColumn[0]}")
        val cursor = context.contentResolver.query(uri, filePathColumn, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                Timber.d("columnIndex Test: $columnIndex")
                val fileName = cursor.getString(columnIndex)
                val filePath = File(context.filesDir, fileName).path
                return File(filePath)
            }
        }
        return null
    }


    override fun onDestroy() {
        super.onDestroy()
        communityAddPostViewModel.initAllStatus()
        communityAddPostViewModel.initTransactionStatus()
        communityAddPostViewModel.initFilledState()
    }

}