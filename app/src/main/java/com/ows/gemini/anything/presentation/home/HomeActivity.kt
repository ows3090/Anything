package com.ows.gemini.anything.presentation.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.ows.gemini.anything.R
import com.ows.gemini.anything.databinding.ActivityHomeBinding
import com.ows.gemini.anything.presentation.base.BaseActivity
import com.ows.gemini.anything.presentation.recommendations.RecommendationsActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>(R.layout.activity_home) {
    private val viewModel by viewModels<HomeViewModel>()
    private val adapter = HomeImageAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initViews()
        bindViews()
        observeData()
    }

    private fun initViews() {
        binding.btnMy.backgroundTintList = resources.getColorStateList(R.color.white, null)
        binding.btnOthers.backgroundTintList =
            resources.getColorStateList(R.color.transparent, null)
        binding.btnMy.setTextColor(resources.getColorStateList(R.color.black, null))
        binding.btnOthers.setTextColor(resources.getColorStateList(R.color.white, null))
        binding.rvImage.adapter = adapter
        binding.rvImage.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun bindViews() =
        with(binding) {
            btnMy.setOnClickListener {
                btnMy.backgroundTintList = resources.getColorStateList(R.color.white, null)
                btnOthers.backgroundTintList =
                    resources.getColorStateList(R.color.transparent, null)
                btnMy.setTextColor(resources.getColorStateList(R.color.black, null))
                btnOthers.setTextColor(resources.getColorStateList(R.color.white, null))
            }

            btnOthers.setOnClickListener {
                btnMy.backgroundTintList =
                    resources.getColorStateList(R.color.transparent, null)
                btnOthers.backgroundTintList =
                    resources.getColorStateList(R.color.white, null)
                btnMy.setTextColor(resources.getColorStateList(R.color.white, null))
                btnOthers.setTextColor(resources.getColorStateList(R.color.black, null))
            }

            btnRecommendations.setOnClickListener {
                // val dialog = HomeDialogFragment()
//                HomeDialogFragment(
//                    title = getString(R.string.dialog_no_more_message_title),
//                    subTitle = getString(R.string.ialog_no_more_message_subtitle),
//                ).show(supportFragmentManager, HomeDialogFragment.TAG)
                startActivity(RecommendationsActivity.newIntent(this@HomeActivity))
            }
        }

    private fun observeData() {
        viewModel.getMyRecentlyFoodImages()
        viewModel.recentlyFoodModels.observe(this) { foodModels ->
            if (foodModels == null) return@observe
            Timber.d("recentlyFoodModels $foodModels")
            binding.progress.isVisible = false
            binding.rvImage.isInvisible = foodModels.isEmpty()
            binding.ivEmpty.isInvisible = foodModels.isNotEmpty()
            adapter.setFoodModels(foodModels)
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, HomeActivity::class.java)
    }
}
