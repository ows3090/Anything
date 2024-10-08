package com.ows.gemini.anything.presentation.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.ows.gemini.anything.R
import com.ows.gemini.anything.databinding.ActivityOnboardingBinding
import com.ows.gemini.anything.presentation.base.BaseActivity
import com.ows.gemini.anything.presentation.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingActivity : BaseActivity<ActivityOnboardingBinding>(R.layout.activity_onboarding) {
    private val viewModel by viewModels<OnBoardingViewModel>()
    private val pagerAdapter = OnBoardingPagerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
    }

    private fun initViews() =
        with(binding) {
            viewpager.adapter = pagerAdapter
            indicator.setViewPager(viewpager)
            viewpager.registerOnPageChangeCallback(
                object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        if (position == 3) {
                            btnStart.isVisible = true
                        } else {
                            btnStart.isVisible = false
                        }
                    }
                },
            )

            btnStart.setOnClickListener {
                viewModel.launch()
                startActivity(HomeActivity.newIntent(this@OnboardingActivity))
            }
        }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, OnboardingActivity::class.java)
    }
}
