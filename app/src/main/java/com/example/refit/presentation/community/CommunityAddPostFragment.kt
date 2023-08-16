package com.example.refit.presentation.community

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ArrayRes
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.content.ContextCompat
import com.example.refit.R
import com.example.refit.databinding.FragmentCommunityAddPostBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.DialogUtil.showCommunityAddShippingFeeDiaglog
import com.example.refit.presentation.common.DropdownMenuManager
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.common.NavigationUtil.navigateUp
import com.example.refit.presentation.community.viewmodel.CommunityAddPostViewModel
import com.example.refit.presentation.community.viewmodel.CommunityViewModel
import com.example.refit.presentation.dialog.community.CommunityAddShippingFeeDialogListener
import com.google.android.material.chip.Chip
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber
import java.io.File


class CommunityAddPostFragment :
    BaseFragment<FragmentCommunityAddPostBinding>(R.layout.fragment_community_add_post) {

    private val vmAdd: CommunityAddPostViewModel by sharedViewModel()
    private val vm: CommunityViewModel by sharedViewModel()
    private lateinit var pickMultipleMedia: ActivityResultLauncher<PickVisualMediaRequest>

    private var photoUris: List<String>? = null


    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cmviewmodel = vmAdd
        vmAdd.setPriceInputCompleted("")
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

        if (vmAdd.isModifyPost.value == true) {
            initTextValueIfModify()
            observeStatus()
        }
    }


    // 추천 착용 성별, 카테고리, 사이즈, 배송 관련 드롭다운
    private fun CommunityAddContentsOptionDropdown() {
        binding.cvCommunityAddpostRecommendGender.setOnClickListener {
            vmAdd.setClickedOptionRG(clicked = true)
            val listPopupWindow = getPopupMenu(it, R.array.community_item_search_option_gender)
            setPopupItemClickListener(it.id, listPopupWindow)
            listPopupWindow.show()
        }

        binding.cvCommunityAddpostClothesCategory.setOnClickListener {
            vmAdd.setClickedOptionCategory(clicked = true)
            val listPopupWindow = getPopupMenu(it, R.array.community_item_search_option_category)
            setPopupItemClickListener(it.id, listPopupWindow)
            listPopupWindow.show()
        }

        binding.cvCommuntiyAddpostSize.setOnClickListener {
            vmAdd.setClickedOptionSize(clicked = true)
            val listPopupWindow = getPopupMenu(it, R.array.community_item_search_option_size)
            setPopupItemClickListener(it.id, listPopupWindow)
            listPopupWindow.show()
        }

        binding.cvCommunityAddpostTransactionMethod.setOnClickListener {
            vmAdd.setClickedOptionTM(clicked = true)
            vmAdd.setFilledStatus(5, false, "")
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
                    vmAdd.setFilledStatus(2, true, itemDescription)
                }

                binding.cvCommunityAddpostClothesCategory.id -> {
                    binding.tvCommunityAddpostClothesCategory.text = itemDescription
                    binding.cvCommunityAddpostClothesCategory.strokeColor =
                        ContextCompat.getColor(requireContext(), R.color.white)
                    if(itemDescription == "신발" || itemDescription == "악세사리") {
                        binding.cvCommuntiyAddpostSize.isClickable = false
                        binding.tvCommuntiyAddpostSize.text = "상세설명 입력"
                        binding.cvCommuntiyAddpostSize.strokeColor =
                            ContextCompat.getColor(requireContext(), R.color.white)
                        binding.tvCommuntiyAddpostSize.setTextColor(ContextCompat.getColor(requireContext(), R.color.green1))
                        vmAdd.setFilledStatus(4, true, "상세설명 입력")
                    } else {
                        vmAdd.setClickedOptionSize(false)
                        binding.cvCommuntiyAddpostSize.isClickable = true
                        binding.tvCommuntiyAddpostSize.text = "사이즈를 선택해주세요"
                    }
                    vmAdd.setFilledStatus(3, true, itemDescription)
                }

                binding.cvCommuntiyAddpostSize.id -> {
                    binding.tvCommuntiyAddpostSize.text = itemDescription
                    binding.cvCommuntiyAddpostSize.strokeColor =
                        ContextCompat.getColor(requireContext(), R.color.white)
                    vmAdd.setFilledStatus(4, true, itemDescription)
                }

                binding.cvCommunityAddpostTransactionMethod.id -> {
                    binding.tvCommunityAddpostTransactionMethod.text = itemDescription
                    vmAdd.setFilledStatus(5, true, itemDescription)
                    binding.cvCommunityAddpostTransactionMethod.strokeColor =
                        ContextCompat.getColor(requireContext(), R.color.white)
                    vmAdd.selectTransactionMethod(itemDescription)
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
            anchorView,
            R.style.ListPopupMenuWindow_CommunityAddPostOption,
            R.layout.list_popup_window_item_window_dark,
            items
        )
    }


    private fun selectTransactionChipType() {
        binding.cgCommunityAddpostMethod.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.size > 0) {
                vmAdd.setClickedOptionTM(clicked = false)
                binding.tvCommunityAddpostTransactionMethod.text = "거래 희망 방식"
                val checkedMethodType = binding.root.findViewById<Chip>(checkedIds[0])
                vmAdd.checkTransactionType(
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
                    vmAdd.setFilledStatus(8, true, "")
                    binding.tvCommunityAddpostSf.text = ""
                    binding.tvCommunityAddpostFeeInput.text =
                        getString(R.string.community_addpost_contents_detail_fourth_input)
                    vmAdd.setFeeStatus()
                    vmAdd.setShippingFee(0)
                    false
                }

                R.id.rb_community_addpost_input_exclude_fee -> {
                    vmAdd.setFilledStatus(8, false, "")
                    vmAdd.setFeeStatus()
                    true
                }

                else -> false
            }
            vmAdd.setFilledStatus(10, type, "")
        }
    }

    private fun handledAddShippingFee() {
        binding.tvCommunityAddpostFeeInput.setOnClickListener {
            showCommunityAddShippingFeeDiaglog(object : CommunityAddShippingFeeDialogListener {
                override fun onClickDone(fee: Int) {
                    vmAdd.setFilledStatus(8, status = true, "")
                    vmAdd.setShippingFee(fee)
                    vmAdd.setFeeStatus()
                    val feeText = vmAdd.getDecimalFormat(fee.toString())
                    binding.tvCommunityAddpostSf.text = feeText + "원"
                }
            }, vmAdd).show(
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
                vmAdd.setFilledStatus(1, isFilled, "")
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        binding.etCommunityAddpostPrice.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                vmAdd.setPriceInputCompleted("")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val isFilled = p0?.isNotEmpty() == true
                vmAdd.setFilledStatus(6, isFilled, "")
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.etCommunityAddpostDetail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val isFilled = p0?.isNotEmpty() == true
                vmAdd.setFilledStatus(7, isFilled, "")
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    private fun handleAfterInputPrice() {
        binding.llCommunityAddpostView.setOnClickListener {
            val inputPriceText = binding.etCommunityAddpostPrice.text.toString()
            binding.tvCommunityAddpostPrice.text =
                "₩ " + vmAdd.getDecimalFormat(inputPriceText) + "원"
            vmAdd.setPriceInputCompleted(inputPriceText)
        }
    }

    private fun handlePostcodeSetting() {
        binding.cvCommunityAddpostRegion.setOnClickListener {
            navigate(R.id.action_communityAddPostFragment_to_postcodeFragment)
            vmAdd.getToken()
        }

        vmAdd.postCode.observe(viewLifecycleOwner) { address ->
            if (vmAdd.postCodeValue.value == true) {
                binding.tvCommunityAddpostRegion.text = address
                vmAdd.setAddress(address)
                val tm = vmAdd.postValue[1].value?.let { vmAdd.conversionTypeToText(2, it) }
                if (tm != null) {
                    binding.tvCommunityAddpostTransactionMethod.text = tm
                    vmAdd.selectTransactionMethod(tm)
                    initStrokeColorIfModify()
                }
                vmAdd.setPostCodeValue(false)
            }
        }
    }

    private fun handleAddCommunityPhoto() {
        binding.ivCommunityImageContainer.setOnClickListener {
            pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
        }
    }

    private fun handleRegisterButton() {
        binding.btnCommunityAddPostRegister.setOnClickListener {
            val title = binding.etCommunityAddpostTitle.text.toString()
            val detail = binding.etCommunityAddpostDetail.text.toString()

            vmAdd.setPostTitleAndDetail(title, detail)

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

            Timber.d("modifyPost : ${vmAdd.isModifyPost.value}\nimagestaus: ${vmAdd.modifyImageStatus.value}")
            if (vmAdd.isModifyPost.value == true) {
                val imageStatus = vmAdd.modifyImageStatus.value
                if (imageStatus == false || imageStatus == null) {
                    vmAdd.modifyPost()
                } else {
                    vmAdd.modifyPostIncludeImage(imageFiles)
                }
                navigateUp()
            } else {
                vmAdd.createPost(imageFiles)
                vm.loadCommunityList()
                navigateUp()
            }


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
                if (uris != null && uris.toString() != "[]") {
                    // 몇몇 기종에서는 5개 선택 제한이 안 되는 이슈 발생 -> 5개 초과로 받아와도 5개까지만 보여주도록 추가 설정
                    val selectedUris = uris.take(5)
                    photoUris = selectedUris.map { uri -> uri.toString() }
                    vmAdd.setImageUris(photoUris!!)
                    val len = photoUris!!.size
                    if (len < 5) binding.ivCommunityImageContainer.setImageResource(R.drawable.ic_rectangle_60)
                    vmAdd.setPhotoLen(len)
                    vmAdd.setFilledStatus(0, status = true, "")
                    if (selectedUris.isNotEmpty()) {
                        for (uri in selectedUris) {
                            vmAdd.setFilledImage(len)
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


    // 글 수정으로 진입 시 초기 바인딩 요소들 처리
    @SuppressLint("ResourceAsColor")
    private fun initTextValueIfModify() {
        // 제목
        binding.etCommunityAddpostTitle.setText(vmAdd.postResponse.value?.title ?: "UnKnown")


        // 거래 방식 (나눔/판매)
        val transactionType = vmAdd.postResponse.value?.postType ?: 0
        if (transactionType == 0) {
            binding.cgCommunityAddpostMethod.check(R.id.cg_community_addpost_method_giveaway)
        } else {
            binding.cgCommunityAddpostMethod.check(R.id.cg_community_addpost_method_sale)
            val price = vmAdd.postResponse.value?.price
            if (price != null) {
                binding.etCommunityAddpostPrice.setText(price.toString())
                binding.tvCommunityAddpostPrice.text =
                    "₩ " + vmAdd.getDecimalFormat(price.toString()) + "원"
                vmAdd.setPriceInputCompleted(price.toString())
            }
        }

        // 거래 희망 방식 (직/배)
        val transactionMethod =
            vmAdd.postResponse.value?.let { vmAdd.conversionTypeToText(2, it.deliveryType) }
        binding.tvCommunityAddpostTransactionMethod.text = transactionMethod
        binding.tvCommunityAddpostTransactionMethod.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.green1
            )
        )
        if (transactionMethod != null) {
            vmAdd.selectTransactionMethod(transactionMethod)
        }

        if (vmAdd.postResponse.value!!.deliveryType == 1) {
            // 배송비 있어야 함
            val fee = vmAdd.postResponse.value?.deliveryFee
            if (fee == 0) {
                // 배송비 포함
                binding.rbCommunityAddpostInputIncludeFee.isChecked = true
                vmAdd.setShippingFee(0)
            } else {
                binding.rbCommunityAddpostInputExcludeFee.isChecked = true
                vmAdd.setFilledStatus(8, true, "")
                vmAdd.setShippingFee(fee!!)
                vmAdd.setFeeStatus()
                val feeText = vmAdd.getDecimalFormat(fee.toString())
                binding.tvCommunityAddpostSf.text = feeText + "원"
            }
        } else {
            // 지역 관련
            binding.tvCommunityAddpostRegion.text = vmAdd.postResponse.value?.address
        }


        // 상세 설명
        binding.etCommunityAddpostDetail.setText(vmAdd.postResponse.value?.detail ?: "UnKnown")

    }

    private fun initStrokeColorIfModify() {
        if (vmAdd.isFilledValue[3].value == true) {
            Timber.d("postCodeValue initStrokeColorIfModify true")
            binding.cvCommunityAddpostClothesCategory.strokeColor =
                ContextCompat.getColor(requireContext(), R.color.white)
        }
        if (vmAdd.isFilledValue[2].value == true) {
            binding.cvCommunityAddpostRecommendGender.strokeColor =
                ContextCompat.getColor(requireContext(), R.color.white)
        }
        if (vmAdd.isFilledValue[4].value == true) {
            binding.cvCommuntiyAddpostSize.strokeColor =
                ContextCompat.getColor(requireContext(), R.color.white)
        }
        if (vmAdd.isFilledValue[5].value != null) {
            binding.tvCommunityAddpostTransactionMethod.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green1
                )
            )
        }
        if(vmAdd.postResponse.value?.deliveryFee == 0) {
            binding.tvCommunityAddpostFeeInput.text = "입력"
        }
    }

    private fun observeStatus() {
        vmAdd.postResponse.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                initStrokeColorIfModify()
            }
        }
        /*vmAdd.postValue[3].observe(viewLifecycleOwner) { response ->
            if(response != null) {
                vmAdd.gaugeShoesOrAcc()
            }
        }*/
    }

    override fun onDestroy() {
        super.onDestroy()
        vmAdd.initAllStatus()
        vmAdd.initTransactionStatus()
        vmAdd.initFilledState()
    }

}