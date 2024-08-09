package com.ows.gemini.anything.presentation.recommendations

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.ows.gemini.anything.R
import com.ows.gemini.anything.data.type.MealTime
import com.ows.gemini.anything.data.type.PromptStep
import com.ows.gemini.anything.databinding.ActivityRecommendationsBinding
import com.ows.gemini.anything.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecommendationsActivity : BaseActivity<ActivityRecommendationsBinding>(R.layout.activity_recommendations) {
    private val viewModel by viewModels<RecommendationsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bindViews()
        observeData()
    }

    private fun bindViews() {
        binding.btnNext.setOnClickListener {
            if (viewModel.promptStep.value == PromptStep.Step1 && viewModel.mealTime.value != null) {
                if (viewModel.mealTime.value != null) {
                    viewModel.toNextStep()
                }
            } else {
                viewModel.toNextStep()
            }
        }

        binding.btnBack.setOnClickListener {
            viewModel.toBackStep()
        }

        bindMealtimeViews()
    }

    private fun observeData() {
        observePromptStep()
        observeMealTime()
    }

    private fun observePromptStep() {
        viewModel.promptStep.observe(this) { promptStep ->
            binding.layoutMealtime.root.isVisible = promptStep == PromptStep.Step1
            binding.layoutFoodtype.root.isVisible =
                promptStep == PromptStep.Step2 ||
                promptStep == PromptStep.Step3
            binding.layoutRecentlyFood.root.isVisible = promptStep == PromptStep.Step4
            binding.layoutDetail.root.isVisible = promptStep == PromptStep.Step5

            binding.tvNumber.text = "Q.${promptStep.value}."
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

            if (promptStep != PromptStep.Step1) {
                binding.btnBack.isVisible = true
                binding.tvSkip.isVisible = true
            } else {
                binding.btnBack.isGone = true
                binding.tvSkip.isGone = true
            }

            binding.vStep2.setBackgroundColor(
                resources.getColor(
                    if (promptStep.value >= 2) R.color.sunshade else R.color.white_20,
                    null,
                ),
            )
            binding.vStep3.setBackgroundColor(
                resources.getColor(
                    if (promptStep.value >= 3) R.color.sunshade else R.color.white_20,
                    null,
                ),
            )
            binding.vStep4.setBackgroundColor(
                resources.getColor(
                    if (promptStep.value >= 4) R.color.sunshade else R.color.white_20,
                    null,
                ),
            )
            binding.vStep5.setBackgroundColor(
                resources.getColor(
                    if (promptStep.value >= 5) R.color.sunshade else R.color.white_20,
                    null,
                ),
            )
        }
    }

    private fun observeMealTime() =
        with(binding) {
            viewModel.mealTime.observe(this@RecommendationsActivity) { mealTime ->
                layoutMealtime.tvBreakfast.backgroundTintList =
                    resources.getColorStateList(
                        if (mealTime == MealTime.BreakFast) R.color.white else R.color.carbongrey,
                        null,
                    )
                layoutMealtime.tvLunch.backgroundTintList =
                    resources.getColorStateList(
                        if (mealTime == MealTime.Lunch) R.color.white else R.color.carbongrey,
                        null,
                    )
                layoutMealtime.tvDinner.backgroundTintList =
                    resources.getColorStateList(
                        if (mealTime == MealTime.Dinner) R.color.white else R.color.carbongrey,
                        null,
                    )
                layoutMealtime.tvMidnightMeal.backgroundTintList =
                    resources.getColorStateList(
                        if (mealTime == MealTime.Midnight_meal) R.color.white else R.color.carbongrey,
                        null,
                    )
            }
        }

    private fun bindMealtimeViews() =
        with(binding) {
            layoutMealtime.tvBreakfast.setOnClickListener {
                viewModel.selectMealTime(MealTime.BreakFast)
            }

            layoutMealtime.tvLunch.setOnClickListener {
                viewModel.selectMealTime(MealTime.Lunch)
            }

            layoutMealtime.tvDinner.setOnClickListener {
                viewModel.selectMealTime(MealTime.Dinner)
            }

            layoutMealtime.tvMidnightMeal.setOnClickListener {
                viewModel.selectMealTime(MealTime.Midnight_meal)
            }
        }

    companion object {
        fun newIntent(context: Context) = Intent(context, RecommendationsActivity::class.java)
    }
}
