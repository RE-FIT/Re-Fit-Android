package com.example.refit.presentation.mypage

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.refit.R
import com.example.refit.databinding.FragmentMyInfoUpdateBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.DialogUtil
import com.example.refit.presentation.common.DialogUtil.checkNickNameDialog
import com.example.refit.presentation.dialog.mypage.ProfileRegisterPhotoDialogListener
import com.example.refit.presentation.mypage.viewmodel.MyInfoViewModel
import com.example.refit.util.FileUtil
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class MyInfoUpdateFragment : BaseFragment<FragmentMyInfoUpdateBinding>(R.layout.fragment_my_info_update) {

    private val vm: MyInfoViewModel by sharedViewModel()

    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var takePicture: ActivityResultLauncher<Uri>
    private var photoUri: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinner = binding.spinnerGender

        binding.vm = vm
        binding.lifecycleOwner = this

        // 성별 선택
        spinner.adapter = ArrayAdapter.createFromResource(
            this.requireContext(),
            R.array.my_page_myInfo_gender,
            R.layout.mypage_spinner_gender
        )
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) { }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                vm.userGender.observe(viewLifecycleOwner) {
                    when (position) {
                        0 -> {
                            vm.updateGender(0)
                        }
                        1 -> {
                            vm.updateGender(1)
                        }
                    }
                }
            }
        }

        // 앨범에서 사진 가져오기
        initGalleryLauncher()
        handleAddProfilePhoto()

        // 이름(닉네임)
        binding.etNickname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 중복 확인, 수정하기 버튼 활성화 > 색깔 바뀜
                vm.updateNickname(s.toString())
            }

            override fun afterTextChanged(s: Editable?) { }
        })

        // 생일
        binding.etBirthday.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 수정하기 버튼 활성화 > 색깔 바뀜
                vm.updateBirth(s.toString())
            }
            override fun afterTextChanged(s: Editable?) { }
        })

        // 중복 확인 눌렀을 때
        binding.btnNickNameCheck.setOnClickListener {
            Timber.d("중복 확인 버튼 클릭됨")

            vm.isCheckUpdatedNickname.observe(viewLifecycleOwner) {
                vm.checkNicknameRetrofit()
            }
        }

        // 수정 하기 버튼 눌렀을 때
        // 1. 중복 확인 누름 - 사용 가능 / 이미 사용 중
        // 2. 중복 확인 안 누름 > 다이얼 로그 띄우기
        // 3. 닉네임 수정 안 해서 바로 수정 가능
        binding.btnMyInfoUpdate.setOnClickListener {
            vm.userNickname.observe(viewLifecycleOwner) {

            }

            showMyPageNickNameCheckDialog()
        }

        vm.initAllStatus()
    }

    // ----------------------- 이름(닉네임) 중복 확인 -----------------------
    private fun showMyPageNickNameCheckDialog() {
        checkNickNameDialog(
            resources.getString(R.string.my_info_nickname_check)
        ).show(requireActivity().supportFragmentManager, null)
    }

    // ----------------------- 사진 및 카메라 통한 옷 이미지 등록 -----------------------
    private fun handleAddProfilePhoto() {
        binding.cameraImage.setOnClickListener {
            DialogUtil.showProfileRegisterPhotoDialog(object : ProfileRegisterPhotoDialogListener {
                override fun onClickTakePhoto() {
                    photoUri = FileUtil.createImageFile(requireActivity())
                    takePicture.launch(photoUri)
                }

                override fun onClickGallery() {
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
            }).show(requireActivity().supportFragmentManager, "ProfileRegisterPhotoDialog")
        }
    }

    private fun initGalleryLauncher() {
        pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.photoUri = uri.toString()
                    binding.profileImage.visibility = View.VISIBLE
                } else {
                    Timber.d("선택된 사진이 없음")
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        vm.initAllStatus()
    }
}