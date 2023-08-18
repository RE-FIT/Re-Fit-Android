package com.example.refit.presentation.mypage

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.refit.BuildConfig.IMAGE_URL
import com.example.refit.R
import com.example.refit.data.model.mypage.ShowMyInfoResponse
import com.example.refit.databinding.FragmentMyInfoUpdateBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.DialogUtil
import com.example.refit.presentation.common.DialogUtil.checkNickNameDialog
import com.example.refit.presentation.common.DialogUtil.createAlertBasicDialog
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.common.NavigationUtil.navigateUp
import com.example.refit.presentation.dialog.AlertBasicDialogListener
import com.example.refit.presentation.dialog.mypage.ProfileRegisterPhotoDialogListener
import com.example.refit.presentation.mypage.viewmodel.MyInfoViewModel
import com.example.refit.util.Event
import com.example.refit.util.EventObserver
import com.example.refit.util.FileUtil
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber
import java.io.File
import java.lang.Math.abs

class MyInfoUpdateFragment : BaseFragment<FragmentMyInfoUpdateBinding>(R.layout.fragment_my_info_update) {

    private val vm: MyInfoViewModel by sharedViewModel()

    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private var photoUri: Uri? = null
    var flag = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = vm
        binding.lifecycleOwner = this

        vm.showMyInfoRetrofit()

        // 앨범에서 사진 가져오기
        initGalleryLauncher()
        handleAddProfilePhoto()

        /**
         * 서버로부터 데이터가 왔을때 초기화
         */
        vm.fromServer.observe(viewLifecycleOwner, EventObserver{
            Glide.with(binding.root)
                .load(vm.prevProfileImage.value)
                .into(binding.profileImage)
            editBirth()
            selectGenderSpinner()
        })

        /**
         * 변경이 발생했을때, 만약 바뀐부분이 있다면 색상 초록색으로 변경
         */
        vm.change.observe(viewLifecycleOwner, EventObserver{
            if ((vm.profileImage.value != vm.prevProfileImage.value) || (vm.birth.value != vm.prevBirth.value) || (vm.gender.value != vm.prevGender.value)) {
                vm.isChange(true)
            } else {
                vm.isChange(false)
            }
        })

        /**
         * 수정버튼 클릭
         * */
        binding.btnMyInfoUpdate.setOnClickListener {
            handleUpdateButton()
            vm.isChange(false)
        }

        vm.isSuccess.observe(viewLifecycleOwner, EventObserver{
            vm.showMyInfoRetrofit()
        })

        showMyInfoBackPressedDialog()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        vm.isChange(false)
    }

    // ----------------------- 정보 수정 -----------------------
    private fun selectGenderSpinner() {
        // 성별 선택
        val spinner = binding.spinnerGender

        spinner.adapter = ArrayAdapter.createFromResource(
            this.requireContext(),
            R.array.my_page_myInfo_gender,
            R.layout.mypage_spinner_gender
        )

        spinner.setSelection(vm.gender.value ?: 0)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                vm.setGender(position)
                vm.changed()
            }
        }
    }

    private fun editBirth() {
        // 생일
        binding.etBirthday.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val length = binding.etBirthday.text.length

                if (length == 4 && before != 1) {
                    binding.etBirthday.setText(binding.etBirthday.text.toString()+"/")
                    binding.etBirthday.setSelection(binding.etBirthday.text.length)
                } else if (length == 7 && before != 1) {
                    binding.etBirthday.setText(binding.etBirthday.text.toString() + "/")
                    binding.etBirthday.setSelection(binding.etBirthday.text.length)
                }

                vm.setBirth(binding.etBirthday.text.toString())
                vm.changed()
            }
            override fun afterTextChanged(s: Editable?) { }
        })
    }

    // ----------------------- 사진 및 카메라 통한 옷 이미지 등록 -----------------------
    private fun handleAddProfilePhoto() {
        binding.cameraImage.setOnClickListener {
            DialogUtil.showProfileRegisterPhotoDialog(object : ProfileRegisterPhotoDialogListener {

                override fun deletePhoto() {
                    vm.setProfileImage(IMAGE_URL)
                    Glide.with(binding.root)
                        .load(IMAGE_URL)
                        .into(binding.profileImage)
                    vm.changed()
                    photoUri = null
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
                    Glide.with(binding.root)
                        .load(uri.toString())
                        .into(binding.profileImage)
                    vm.setProfileImage("profileChanged")
                    vm.changed()
                    photoUri = uri
                } else {
                    Timber.d("선택된 사진이 없음")
                }
            }
    }

    // ----------------------- 수정하기 -----------------------

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

    private fun handleUpdateButton() {
        if (photoUri != null) {
            val uri = Uri.parse(photoUri.toString())
            val copiedFile = copyFileToInternalStorage(uri)
            Timber.d("file URI 값 정상 작동되는지 확인 : $uri ================ $copiedFile")

            vm.updateMyInfoRetrofit(copiedFile!!)
        } else {
            vm.updateMyInfoRetrofit(null)
        }
    }

    private fun showMyInfoBackPressedDialog() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (vm.isChange.value == true) {
                        createAlertBasicDialog(
                            resources.getString(R.string.pw_change_delete_title),
                            resources.getString(R.string.pw_change_delete_positive),
                            resources.getString(R.string.pw_change_delete_negative),
                            object : AlertBasicDialogListener {
                                override fun onClickPositive() {
                                    navigate(R.id.action_myInfo_to_nav_my_page)
                                }

                                override fun onClickNegative() {
                                }
                            }).show(requireActivity().supportFragmentManager, null)
                    } else {
                        navigate(R.id.action_myInfo_to_nav_my_page)
                    }
                }
            })
    }
}