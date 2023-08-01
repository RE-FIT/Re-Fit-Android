package com.example.refit.presentation.community

import android.icu.text.DecimalFormat
import android.net.Uri
import android.os.Bundle
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
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.content.ContextCompat
import com.example.refit.R
import com.example.refit.databinding.FragmentCommunityAddPostBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.DialogUtil
import com.example.refit.presentation.common.DialogUtil.showCommunityAddShippingFeeDiaglog
import com.example.refit.presentation.common.DropdownMenuManager
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.community.viewmodel.CommunityAddPostViewModel
import com.example.refit.presentation.dialog.community.CommunityAddShippingFeeDialogListener
import com.example.refit.util.FileUtil
import com.google.android.material.chip.Chip
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber


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
                    binding.cvCommunityAddpostRecommendGender.strokeColor = ContextCompat.getColor(requireContext(), R.color.white)
                    communityAddPostViewModel.setFilledStatus(2, true)
                }
                binding.cvCommunityAddpostClothesCategory.id -> {
                    binding.tvCommunityAddpostClothesCategory.text = itemDescription
                    binding.cvCommunityAddpostClothesCategory.strokeColor = ContextCompat.getColor(requireContext(), R.color.white)
                    communityAddPostViewModel.setFilledStatus(3, true)
                }
                binding.cvCommuntiyAddpostSize.id -> {
                    binding.tvCommuntiyAddpostSize.text = itemDescription
                    binding.cvCommuntiyAddpostSize.strokeColor = ContextCompat.getColor(requireContext(), R.color.white)
                    communityAddPostViewModel.setFilledStatus(4, true)
                }
                binding.cvCommunityAddpostTransactionMethod.id -> {
                    binding.tvCommunityAddpostTransactionMethod.text = itemDescription
                    communityAddPostViewModel.setFilledStatus(5, true)
                    binding.cvCommunityAddpostTransactionMethod.strokeColor = ContextCompat.getColor(requireContext(), R.color.white)
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
                    binding.tvCommunityAddpostSf.text = ""
                    binding.tvCommunityAddpostFeeInput.text = getString(R.string.community_addpost_contents_detail_fourth_input)
                    false
                }
                R.id.rb_community_addpost_input_exclude_fee -> true
                else -> false
            }
             communityAddPostViewModel.setFilledStatus(10, type)
        }
    }

    private fun handledAddShippingFee() {
        binding.tvCommunityAddpostFeeInput.setOnClickListener {
            showCommunityAddShippingFeeDiaglog(object : CommunityAddShippingFeeDialogListener {
                override fun onClickDone(fee: Int) {
                    communityAddPostViewModel.setFilledStatus(8, status = true)
                    communityAddPostViewModel.setShippingFee(fee)
                    val feeText = communityAddPostViewModel.getDecimalFormat(fee.toString())
                    binding.tvCommunityAddpostSf.text = feeText + "원"
                }
            }, communityAddPostViewModel).show(requireActivity().supportFragmentManager, "CommunityAddShippingFeeDialog")
        }
    }

    private fun observeEditTextChanges() {
        binding.etCommunityAddpostPrice.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                communityAddPostViewModel.setPriceInputCompleted("")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val isFilled = s?.isNotEmpty() == true
                communityAddPostViewModel.setFilledStatus(7, isFilled)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun handleAfterInputPrice() {
        binding.llCommunityAddpostView.setOnClickListener {
            val inputPriceText = binding.etCommunityAddpostPrice.text.toString()
            binding.tvCommunityAddpostPrice.text = "₩ " + communityAddPostViewModel.getDecimalFormat(inputPriceText) + "원"
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

    private fun initGalleryLauncher() {
        pickMultipleMedia = registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) { uris ->
            photoUris = null
            if (uris != null) {
                // 몇몇 기종에서는 5개 선택 제한이 안 되는 이슈 발생 -> 5개 초과로 받아와도 5개까지만 보여주도록 추가 설정
                val selectedUris = uris.take(5)
                photoUris = selectedUris.map { uri -> uri.toString() }
                val len = photoUris!!.size
                binding.photolen = "$len/5"
                communityAddPostViewModel.setFilledStatus(0, status = true)
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

    override fun onDestroy() {
        super.onDestroy()
        communityAddPostViewModel.initAllStatus()
        communityAddPostViewModel.initTransactionStatus()
        communityAddPostViewModel.initFilledState()
    }

}