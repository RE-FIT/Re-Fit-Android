package com.example.refit.presentation.community

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import com.example.refit.BuildConfig.BASE_URL
import com.example.refit.R
import com.example.refit.data.datastore.TokenStore
import com.example.refit.databinding.FragmentPostcodeBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.NavigationUtil.navigateUp
import com.example.refit.presentation.community.viewmodel.CommunityAddPostViewModel
import com.example.refit.presentation.community.viewmodel.CommunityInfoViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber
import java.io.InputStream


class PostcodeFragment : BaseFragment<FragmentPostcodeBinding>(R.layout.fragment_postcode) {

    private val vmAdd: CommunityAddPostViewModel by sharedViewModel()


    inner class MyJavaScriptInterface {
        @JavascriptInterface
        fun processDATA(data: String?) {
            if (data != null) {
                // 메인 스레드에서 작업을 실행하기 위해 launch 사용

                vmAdd.setPostCode(data) // LiveData 값을 변경하므로 메인 스레드에서 실행되어야 함
                // onBackPressed() 호출

                Timber.d("우편번호 API 조회 중 ... $data")
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val blogspot = "${BASE_URL}address.html"
        Timber.d("blogspot: $blogspot")
        val token = vmAdd.token.value
        Timber.d("Token : $token")

        binding.wvPostcode.settings.javaScriptEnabled = true
        binding.wvPostcode.addJavascriptInterface(MyJavaScriptInterface(), "Android")
        binding.wvPostcode.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                view?.loadUrl("javascript:execKakaoPostcode();")
            }
        }
        binding.wvPostcode.loadUrl(blogspot)

        handlePostcodeView()
    }

    fun handlePostcodeView() {
        vmAdd.postCodeValue.observe(viewLifecycleOwner) { _ ->
            Timber.d("HANDLEPOSTCODEVALUE : ${vmAdd.postCodeValue.value.toString()}")
            if(vmAdd.postCodeValue.value == true) {
                navigateUp()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}