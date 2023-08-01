package com.example.refit.presentation.closet

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.ArrayRes
import androidx.appcompat.widget.ListPopupWindow
import androidx.recyclerview.widget.GridLayoutManager
import com.example.refit.R
import com.example.refit.data.model.closet.RegisteredClothInfoResponse
import com.example.refit.databinding.FragmentClosetBinding
import com.example.refit.presentation.closet.adapter.UserRegisteredClothesAdapter
import com.example.refit.presentation.closet.viewmodel.ClosetViewModel
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.DialogUtil.createAlertBasicDialog
import com.example.refit.presentation.common.DialogUtil.showClothItemSelectionDialog
import com.example.refit.presentation.common.DropdownMenuManager
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.dialog.AlertBasicDialogListener
import com.example.refit.presentation.dialog.closet.ClothItemSelectionDialogListener
import com.example.refit.util.EventObserver
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class ClosetFragment : BaseFragment<FragmentClosetBinding>(R.layout.fragment_closet) {

    private val closetViewModel: ClosetViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = closetViewModel
        initDefaultClothCategory(0)
        closetViewModel.getUserRegisteredClothes()
        initRegisteredCloth()
        initClosetOptionPopupMenu()
        handleClickAddClothButton()
        handleSelectedRegisteredClothItem()
    }

    private fun handleSelectedRegisteredClothItem() {
        closetViewModel.selectedRegisteredClothItem.observe(
            viewLifecycleOwner,
            EventObserver { item ->
                //TODO(이미 옷 입기가 된 아이템인지 확인 필요 -> 입었다면 옷 입기 막기 처리)
                showClothItemSelectionDialog(item, object : ClothItemSelectionDialogListener {
                    override fun onClickMainButton(isNotCompleteGoal: Boolean) {
                        //TODO(목표 달성 상태에 따라 다르게 처리)
                        navigate(R.id.action_nav_closet_to_forestFragment)
                    }

                    override fun onClickFixInfo(clothInfo: RegisteredClothInfoResponse) {
                        //TODO(옷 등록 페이지로 이동)
                    }

                    override fun onClickClothDeletion(id: Int) {
                        showClothDeletionConfirmDialog()
                    }
                }).show(requireActivity().supportFragmentManager, null)
                Timber.d("클릭한 아이템 아이디 -> $item")
            })
    }

    private fun showClothDeletionConfirmDialog() {
        createAlertBasicDialog(
            resources.getString(R.string.closet_dialog_cloth_delete_title),
            resources.getString(R.string.closet_dialog_cloth_delete_positive),
            resources.getString(R.string.closet_dialog_cloth_delete_negative),
            object : AlertBasicDialogListener {
                override fun onClickPositive() {
                    //TODO(서버 API를 통해 삭제 요청할 것)
                }

                override fun onClickNegative() {
                }

            }
        ).show(requireActivity().supportFragmentManager, null)
    }

    private fun initRegisteredCloth() {
        binding.rvClosetList.layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.rvClosetList.adapter = UserRegisteredClothesAdapter(closetViewModel).apply {
            closetViewModel.registeredClothes.observe(viewLifecycleOwner) { list ->
                submitList(list)
            }
        }
    }

    // ----------------------- 필터링 옵션 -----------------------

    private fun initDefaultClothCategory(initCategoryId: Int) {
        closetViewModel.requestRegisteredItemsByClothCategory(initCategoryId)
    }

    private fun initClosetOptionPopupMenu() {
        binding.cvClosetOptionSeason.setOnClickListener {
            val listPopupWindow = getPopupMenu(it, R.array.closet_item_search_option_season)
            setPopupItemClickListener(it.id, listPopupWindow)
            listPopupWindow.show()
        }

        binding.cvClosetOptionSort.setOnClickListener {
            val listPopupWindow = getPopupMenu(it, R.array.closet_item_search_option_sort)
            setPopupItemClickListener(it.id, listPopupWindow)
            listPopupWindow.show()
        }
    }

    private fun setPopupItemClickListener(viewId: Int, popupMenu: ListPopupWindow) {
        popupMenu.setOnItemClickListener { _, view, _, _ ->
            val itemDescription = (view as TextView).text.toString()
            when (viewId) {
                // TODO(기능 구현 때 뷰모델과 연동)
                binding.cvClosetOptionSeason.id -> {
                    closetViewModel.requestSortingBySeason(itemDescription)
                }

                binding.cvClosetOptionSort.id -> {
                    closetViewModel.requestSortingByClosetSorting(itemDescription)
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
            R.style.ListPopupMenuWindow_ClosetOption,
            R.layout.list_popup_window_item_white,
            items
        )
    }

    // ----------------------- 페이지 이동 옵션 -----------------------

    private fun handleClickAddClothButton() {
        binding.fabClosetAdd.setOnClickListener {
            navigate(R.id.action_nav_closet_to_clothRegistrationFragment)
        }
    }
}