package com.example.refit.presentation.community

import android.os.Bundle

import android.view.View
import android.widget.TextView
import androidx.annotation.ArrayRes
import androidx.appcompat.widget.ListPopupWindow
import androidx.navigation.Navigation
import com.example.refit.R
import com.example.refit.data.model.chat.CreateRoom
import com.example.refit.databinding.FragmentCommunityInfoBinding
import com.example.refit.presentation.chat.viewmodel.ChatViewModel
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
import com.example.refit.presentation.community.viewmodel.CommunityViewModel
import com.example.refit.presentation.community.viewmodel.PostReportViewModel
import com.example.refit.presentation.dialog.AlertBasicDialogListener
import com.example.refit.presentation.dialog.AlertNoIconDialogListener
import com.example.refit.util.EventObserver
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber
import java.lang.IllegalArgumentException

class CommunityInfoFragment : BaseFragment<FragmentCommunityInfoBinding>(R.layout.fragment_community_info) {

    private val chatViewModel: ChatViewModel by sharedViewModel()
    private val vm: CommunityInfoViewModel by sharedViewModel()
    private val vmAdd: CommunityAddPostViewModel by sharedViewModel()
    private val vmPr: PostReportViewModel by sharedViewModel()
    private val vmCom: CommunityViewModel by sharedViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.setImage(0)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = vm

        vm.initAllState()
        CommunityEtcMenuDropdown()
        handleFavIconClicked()
        observeStatus(view)
        initImageList(view)
        vm.clickedGetPost(vm.postId.value!!)
        vmCom.setScrollStatus(true)

        vm.deleteSuccess.observe(viewLifecycleOwner, EventObserver{
            vmCom.setScrollStatus(false)
            navigateUp()

            CustomSnackBar.make(
                binding.root,
                R.layout.custom_snack_bar_basic,
                R.anim.anim_show_snack_bar_from_bottom
            ).setTitle("글 삭제가 완료되었습니다", null).show()
        })

        Timber.d("[info] onViewCreated")
        binding.fabCommunityInfoChat.setOnClickListener {
            chatViewModel.room_create(CreateRoom(vm.postResponse.value!!.author,
                vm.postResponse.value!!.postId, vm.postResponse.value!!.postType))
        }

        chatViewModel.success.observe(viewLifecycleOwner, EventObserver{
            val action = CommunityInfoFragmentDirections.actionCommunityInfoFragmentToChatFragment(
                vm.postResponse.value!!.clickedMember, chatViewModel.roomId.value.toString(),
                vm.postResponse.value!!.author, vm.postResponse.value!!.author,
                vm.postResponse.value!!.postType.toString(), vm.postResponse.value!!.profileUrl.toString(),
                vm.postResponse.value!!.postId, vm.postResponse.value!!.postState)
            Navigation.findNavController(view).navigate(action)
        })
    }

    private fun CommunityEtcMenuDropdown() {
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
                    val postId = vm.postId.value
                    if (postId != null) {
                        vmAdd.setPostId(postId)
                    }
                    navigate(R.id.action_communityInfoFragment_to_communityAddPostFragment)
                }

                "신고하기" -> {
                    showCommunityReport()
                }

                "이 사용자의 글 보지 않기" -> {
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
            popupMenu.dismiss()
        }
    }

    private fun initImageList(view: View) {
        val imageSliderAdapter = InfoImageAdapter(vm)
        binding.vpCommunityInfoImage.adapter = imageSliderAdapter
        vm.postResponse.observe(viewLifecycleOwner) { postResponse ->
            binding.vpCommunityInfoImage.adapter = imageSliderAdapter
            imageSliderAdapter.sliderImageUrls = postResponse.imgUrls

            binding.vpCommunityInfoImage.currentItem = vm.currentImage.value!!

            imageSliderAdapter.setOnItemClickListener(object: InfoImageAdapter.OnItemClickListner {

                override fun onItemClick(v: View, data: String, pos: Int) {
                    val action = CommunityInfoFragmentDirections.actionCommunityInfoFragmentToImageFragment(data)
                    Navigation.findNavController(view).navigate(action)
                }
            })
        }
    }

    override fun onPause() {
        super.onPause()
        vm.setImage(binding.vpCommunityInfoImage.currentItem)
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
                    .setTitle("스크랩이 완료되었습니다", null).show()
            } else {
                CustomSnackBar.make(
                    binding.root,
                    R.layout.custom_snack_bar_basic,
                    R.anim.anim_show_snack_bar_from_bottom
                ).setTitle("스크랩이 취소되었습니다", null).show()
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
                }

                override fun onClickNegative() {
                }

            }
        ).show(requireActivity().supportFragmentManager, null)
    }

    private fun observeStatus(view: View) {
        vm.postResponse.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                initImageList(view)
                vm.checkIfAuthor()
                vm.classifyUserState()
                vm.setPostDate()
            }
        }

        vmAdd.updateStatus.observe(viewLifecycleOwner) {response ->
            if(response != null) {
                vm.clickedGetPost(vm.postId.value!!)
                initImageList(view)
                Timber.d("[INFO] 업데이트 상태 변경 알람")
            }
        }

        vmAdd.update.observe(viewLifecycleOwner, EventObserver{
            CustomSnackBar.make(
                binding.root,
                R.layout.custom_snack_bar_basic,
                R.anim.anim_show_snack_bar_from_bottom
            ).setTitle("정보 수정이 완료되었습니다", null).show()
        })

       vm.sliderImageUrls.observe(viewLifecycleOwner) {response ->
           if(response != null) {
               initImageList(view)
           }
       }
    }
}
