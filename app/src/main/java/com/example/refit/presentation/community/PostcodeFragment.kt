package com.example.refit.presentation.community


import android.os.Bundle
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.refit.BuildConfig.BASE_URL
import com.example.refit.R
import com.example.refit.databinding.FragmentPostcodeBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.NavigationUtil.navigateUp
import com.example.refit.presentation.community.viewmodel.CommunityAddPostViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class PostcodeFragment : BaseFragment<FragmentPostcodeBinding>(R.layout.fragment_postcode) {

    private val vmAdd: CommunityAddPostViewModel by sharedViewModel()

    inner class MyJavaScriptInterface {
        @JavascriptInterface
        fun processDATA(data: String?) {
            if (data != null) {
                vmAdd.setPostCode(data)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val blogspot = "${BASE_URL}address.html"
        val token = vmAdd.token.value

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
            if(vmAdd.postCodeValue.value == true) {
                navigateUp()
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
    }
}