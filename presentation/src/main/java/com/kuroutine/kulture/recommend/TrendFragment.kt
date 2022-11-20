package com.kuroutine.kulture.recommend

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.kuroutine.databinding.FragmentTrendBinding

class TrendFragment: Fragment() {
    private var _binding: FragmentTrendBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTrendBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.wvRecommendTrend.apply {
            webViewClient =  WebViewClient()  // 새 창 띄우기 않기
            webChromeClient = WebChromeClient()

            settings.apply {
                loadWithOverviewMode = true
                useWideViewPort = true
                setSupportZoom(false)
                builtInZoomControls = false

                javaScriptEnabled = true
                javaScriptCanOpenWindowsAutomatically = true
                setSupportMultipleWindows(true)

                domStorageEnabled = true
            }

            binding.wvRecommendTrend.loadUrl("https://trends.google.com/trends/trendingsearches/daily?geo=KR")
        }


        return root
    }
}