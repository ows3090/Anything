package com.ows.gemini.anything.presentation.recommendations

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ows.gemini.anything.R
import com.ows.gemini.anything.data.type.PromptStep
import com.ows.gemini.anything.databinding.ActivityRecommendationsBinding
import com.ows.gemini.anything.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecommendationsActivity : BaseActivity<ActivityRecommendationsBinding>(R.layout.activity_recommendations) {
    private val recommendationViewModel by viewModels<RecommendationsViewModel>()
    private lateinit var adapter: RecentlyFoodAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()
        initView()
        observeData()
    }

    private fun setup() {
        binding.lifecycleOwner = this
        binding.viewModel = recommendationViewModel
    }

    private fun initView() {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (::adapter.isInitialized.not()) {
            adapter =
                RecentlyFoodAdapter {
                    recommendationViewModel.selectExceptionFood(it)
                }
        }

        binding.layoutRecentlyFood.rvRecentlyFood.layoutManager = LinearLayoutManager(this)
        binding.layoutRecentlyFood.rvRecentlyFood.adapter = adapter
    }

    private fun observeData() {
        observePromptStep()
        observeRecentlyFood()
    }

    private fun observePromptStep() {
        recommendationViewModel.promptStep.observe(this) { promptStep ->
            binding.tvNumber.text = "Q.${promptStep.value}"
            binding.tvQuestion.text =
                resources.getString(
                    when (promptStep) {
                        PromptStep.Step1 -> R.string.recommendation_question1
                        PromptStep.Step2 -> R.string.recommendation_question2
                        PromptStep.Step3 -> R.string.recommendation_question3
                        PromptStep.Step4 -> R.string.recommendation_question4
                        else -> R.string.recommendation_question5
                    },
                )
        }
    }

    private fun observeRecentlyFood() {
        recommendationViewModel.recentlyFoods.observe(this) { foods ->
            adapter.setRecentlyFoods(recentlyFoods = foods)
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, RecommendationsActivity::class.java)
    }
}
