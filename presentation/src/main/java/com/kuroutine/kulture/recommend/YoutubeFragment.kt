package com.kuroutine.kulture.recommend

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.kuroutine.databinding.FragmentYoutubeBinding

class YoutubeFragment: Fragment() {
    private var _binding: FragmentYoutubeBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentYoutubeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.wvRecommendYoutube.apply {
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

            binding.wvRecommendYoutube.loadUrl("https://m.youtube.com/channel/UC-9-kyTW8ZkZNDHQJ6FgpwQ")
        }


        return root
    }
}