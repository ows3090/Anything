package com.ows.gemini.anything.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.ows.gemini.anything.R
import com.ows.gemini.anything.databinding.ActivityMainBinding
import com.ows.gemini.anything.presentation.base.BaseActivity
import com.ows.gemini.anything.presentation.home.HomeActivity
import com.ows.gemini.anything.presentation.onboarding.OnboardingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.checkFirstLaunch()
        lifecycleScope.launch {
            delay(1000)
            startActivity(OnboardingActivity.newIntent(this@MainActivity))
            if (viewModel.isFirst.value == true) {
                startActivity(OnboardingActivity.newIntent(this@MainActivity))
            } else {
                startActivity(HomeActivity.newIntent(this@MainActivity))
            }

            finish()
        }
    }
}
