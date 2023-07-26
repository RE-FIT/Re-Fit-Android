package com.example.refit.presentation.community

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.refit.R
import com.example.refit.databinding.FragmentPostcodeBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.NavigationUtil.navigateUp
import com.example.refit.presentation.community.viewmodel.CommunityAddPostViewModel
import com.example.refit.presentation.community.viewmodel.CommunityInfoViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber


class PostcodeFragment : BaseFragment<FragmentPostcodeBinding>(R.layout.fragment_postcode) {

    private val vm: CommunityAddPostViewModel by sharedViewModel()
    private var isAddressSelected = false


    inner class MyJavaScriptInterface {
        @JavascriptInterface
        fun processDATA(data: String?) {
            if (data != null) {
                // vm.setPostCode(data)
                Timber.d("우편번호 API 조회 중 ... $data")
                // navigateUp()
            }
        }

        @JavascriptInterface
        fun processAddress(address: String?) {
            if (isAddressSelected && address != null) {
                vm.setPostCode(address)
                Timber.d("상세주소: $address")
                navigateUp()
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 위에서 작성한 블로거 페이지의 url
        val blogspot = "https://billcoreatech.blogspot.com/2022/06/blog-post.html"

        binding.wvPostcode.settings.javaScriptEnabled = true
        binding.wvPostcode.addJavascriptInterface(MyJavaScriptInterface(), "Android")
        binding.wvPostcode.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                view?.loadUrl("javascript:sample2_execDaumPostcode();")
            }
            /*override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null && url.startsWith("android-app://")) {
                    // 앱으로 전달할 데이터를 추출합니다.
                    val data = Uri.parse(url).getQueryParameter("data")
                    isAddressSelected = true
                    // 상세주소를 가져온 뒤 앱으로 데이터를 전달합니다.
                    view?.loadUrl("javascript:window.Android.processAddress('$data');")
                    return true
                }
                return false
            }*/
        }

        binding.wvPostcode.loadUrl(blogspot)
    }
}