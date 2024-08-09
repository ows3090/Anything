package com.ows.gemini.anything.presentation.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ows.gemini.anything.R
import com.ows.gemini.anything.databinding.ActivityHomeBinding
import com.ows.gemini.anything.presentation.base.BaseActivity
import com.ows.gemini.anything.presentation.recommendations.RecommendationsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>(R.layout.activity_home) {
    private lateinit var adapter: HomeImageAdapter

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
    }

    private fun initViews() {
        binding.btnMy.backgroundTintList = resources.getColorStateList(R.color.white, null)
        binding.btnOthers.backgroundTintList =
            resources.getColorStateList(R.color.transparent, null)
        binding.btnMy.setTextColor(resources.getColorStateList(R.color.black, null))
        binding.btnOthers.setTextColor(resources.getColorStateList(R.color.white, null))

        if (::adapter.isInitialized.not()) {
            adapter = HomeImageAdapter()
            binding.rvImage.adapter = adapter
            binding.rvImage.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        }
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
                startActivity(RecommendationsActivity.newIntent(this@HomeActivity))
            }
        }

    companion object {
        fun newIntent(context: Context) = Intent(context, HomeActivity::class.java)
    }
}
