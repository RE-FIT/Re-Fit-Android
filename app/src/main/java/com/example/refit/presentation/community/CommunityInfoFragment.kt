package com.example.refit.presentation.community

import android.os.Bundle

import android.view.View
import android.widget.TextView
import androidx.annotation.ArrayRes
import androidx.appcompat.widget.ListPopupWindow
import com.example.refit.R
import com.example.refit.data.model.community.PostResponse
import com.example.refit.databinding.FragmentCommunityInfoBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.CustomSnackBar
import com.example.refit.presentation.common.DialogUtil.createAlertBasicDialog
import com.example.refit.presentation.common.DialogUtil.createAlertNoImageDialog
import com.example.refit.presentation.common.DialogUtil.createAlertSirenDialog
import com.example.refit.presentation.common.DropdownMenuManager
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.common.NavigationUtil.navigateUp
import com.example.refit.presentation.community.adapter.InfoImageAdapter
import com.example.refit.presentation.community.viewmodel.CommunityAddPostViewModel
import com.example.refit.presentation.community.viewmodel.CommunityInfoViewModel
import com.example.refit.presentation.community.viewmodel.PostReportViewModel
import com.example.refit.presentation.dialog.AlertBasicDialogListener
import com.example.refit.presentation.dialog.AlertNoIconDialogListener
import com.example.refit.util.EventObserver
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.lang.IllegalArgumentException

class CommunityInfoFragment : BaseFragment<FragmentCommunityInfoBinding>(R.layout.fragment_community_info) {

    private val vm: CommunityInfoViewModel by sharedViewModel()
    private val vmAdd: CommunityAddPostViewModel by sharedViewModel()
    private val vmPr: PostReportViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = vm

        vm.initAllState()
        CommunityEtcMenuDropdown()
        handleFavIconClicked()
        observeStatus()
        initImageList()
    }

    private fun CommunityEtcMenuDropdown() {
        // TODO 유저 상태에 따라 array 값 다르게 불러와야 함
        binding.cvEtcOverflow.setOnClickListener {
            val listPopupWindow = when (vm.UserStatus.value) {
                0 -> getPopupMenu( // 작성자 && (판매중 || 나눔중)
                    it,
                    R.array.community_info_overflow_ing_writer
                )
                1 -> getPopupMenu( // 작성자 && 나눔완료
                    it,
                    R.array.community_info_overflow_end_giveaway_writer
                ) // 작성자 && 판매완료
                2 -> getPopupMenu(
                    it,
                    R.array.community_info_overflow_end_sale_writer
                ) // 작성자 && 나눔완료
                3 -> getPopupMenu(it, R.array.community_info_overflow_user) // 일반 유저
                else -> throw IllegalArgumentException("Invalid Status Value")
            }
            setPopupItemClickListener(listPopupWindow)
            listPopupWindow.show()
        }
    }

    private fun getPopupMenu(
        anchorView: View,
        @ArrayRes items: Int,
    ): ListPopupWindow {
        return DropdownMenuManager.createPopupMenu(
            anchorView,
            R.style.ListPopupMenuWindow_CommunityInfoOption,
            R.layout.list_popup_window_item_white,
            items
        )
    }

    private fun setPopupItemClickListener(popupMenu: ListPopupWindow) {
        popupMenu.setOnItemClickListener { _, view, _, _ ->
            val itemDescription = (view as TextView).text.toString()
            when (itemDescription) {
                "삭제하기" -> {
                    showDeletePost()
                }

                "수정하기" -> {
                    vmAdd.setModifyOrNew(true)
                    navigate(R.id.action_communityInfoFragment_to_communityAddPostFragment)
                }

                "신고하기" -> {
                    showCommunityReport()
                }

                "이 사용자의 글 보지 않기" -> {
                    // TODO 사용자 id 불러오기
                    val username = vm.postResponse.value?.author ?: ""
                    vmPr.setUserName(username)
                    hideUserPost(username = username)
                }
                "나눔중" -> {
                    vm.changePostStatus()
                }
                "판매중" -> {
                    vm.changePostStatus()
                }
            }
            Timber.d(itemDescription)
            popupMenu.dismiss()
        }
    }

    private fun initImageList() {
        val imageSliderAdapter = InfoImageAdapter(vm)
        binding.vpCommunityInfoImage.adapter = imageSliderAdapter
        vm.postResponse.observe(viewLifecycleOwner) {
                postResponse -> imageSliderAdapter.sliderImageUrls = postResponse.imgUrls
        }
    }

    private fun handleFavIconClicked() {
        binding.tbCommunityInfoFav.setOnClickListener {
            vm.scrapPost()
            val toggleStatus = !vm.postResponse.value?.scrapFlag!!
            if (toggleStatus) {
                CustomSnackBar.make(
                    requireView(),
                    R.layout.custom_snack_bar_basic,
                    R.anim.anim_show_snack_bar_from_bottom
                )
                    .setTitle("스크랩을 완료하였습니다!", null).show()
            }
            vm.setScrapStatus(toggleStatus)
        }

        binding.fabCommunityInfoChat.setOnClickListener {
        }
    }

    private fun showCommunityReport() {
        createAlertSirenDialog(
            resources.getString(R.string.community_info_dialog_title_second),
            resources.getString(R.string.community_info_dialog_positive_second),
            resources.getString(R.string.community_info_dialog_negative),
            object : AlertBasicDialogListener {
                override fun onClickPositive() {
                    // TODO 신고
                    navigate(R.id.action_communityInfoFragment_to_postReportFragment)
                    val username = vm.postResponse.value?.author ?: ""
                    vmPr.setUserName(username)
                }

                override fun onClickNegative() {
                }

            }
        ).show(requireActivity().supportFragmentManager, null)
    }

    private fun hideUserPost(username: String) {
        val title = username + resources.getString(R.string.community_info_dialog_title_third)
        createAlertBasicDialog(
            title,
            resources.getString(R.string.community_info_dialog_positive_third),
            resources.getString(R.string.community_info_dialog_negative),
            object : AlertBasicDialogListener {
                override fun onClickPositive() {
                    // TODO 이 사용자의 글 보지 않기 (스낵바 수정하기)
                    vmPr.blockedMember()
                    CustomSnackBar.make(
                        requireView(),
                        R.layout.custom_snackbar_community_basic,
                        R.anim.anim_show_snack_bar_from_top
                    )
                        .setTitle(resources.getString(R.string.community_info_snackbar_first), null)
                        .show()

                }

                override fun onClickNegative() {
                }
            }
        ).show(requireActivity().supportFragmentManager, null)
    }

    private fun showDeletePost() {
        createAlertNoImageDialog(
            resources.getString(R.string.community_info_dialog_title),
            resources.getString(R.string.community_info_dialog_positive),
            resources.getString(R.string.community_info_dialog_negative),
            object : AlertNoIconDialogListener {
                override fun onClickPositive() {
                    // TODO 글 삭제
                    vm.deletePost()
                    navigateUp()
                }

                override fun onClickNegative() {
                }

            }
        ).show(requireActivity().supportFragmentManager, null)
    }

    private fun observeStatus() {
        vm.postResponse.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                vm.checkIfAuthor()
                vm.classifyUserState()
                vm.setPostDate()
            }
        }
    }
}
