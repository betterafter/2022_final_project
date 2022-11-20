package com.kuroutine.kulture.recommend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.kuroutine.databinding.FragmentRecommendBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RecommendFragment : Fragment() {
    lateinit var recyclerView: RecyclerView

    private var _binding: FragmentRecommendBinding? = null
    private val binding get() = _binding!!

    private lateinit var fragments: ArrayList<Fragment>
    private lateinit var youtubeFragment: YoutubeFragment
    private lateinit var trendFragment: TrendFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        youtubeFragment = YoutubeFragment()
        trendFragment = TrendFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecommendBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@RecommendFragment
        }
        val root: View = binding.root

        initFragment()
        init()

        return root
    }

    private fun init() {
        binding.vpRecommendScreen.apply {
            adapter = RecommendViewPager2Adapter(this@RecommendFragment, fragments)
            isUserInputEnabled = false
        }

        TabLayoutMediator(binding.tlRecommendScreen, binding.vpRecommendScreen) { tab, position ->
            if (position == 0) {
                tab.text = "K-Pop"
            } else {
                tab.text = "K-News"
            }
        }.attach()
    }

    private fun initFragment() {
        fragments = ArrayList()
        fragments.add(youtubeFragment)
        fragments.add(trendFragment)
    }
}