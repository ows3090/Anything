package com.ows.gemini.anything.presentation.onboarding

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.ows.gemini.anything.R
import com.ows.gemini.anything.databinding.ItemOnboardingBinding

class OnBoardingPagerAdapter : RecyclerView.Adapter<OnBoardingPagerAdapter.PagerViewHolder>() {
    override fun getItemCount(): Int = 4

    override fun onBindViewHolder(
        holder: PagerViewHolder,
        position: Int,
    ) {
        holder.bind(position)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PagerViewHolder =
        PagerViewHolder(
            ItemOnboardingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
        )

    inner class PagerViewHolder(
        private val binding: ItemOnboardingBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(position: Int) {
            when (position) {
                0 -> {
                    binding.tvIntroduce.isVisible = true
                    binding.ivLeftIntroduce.isVisible = true
                    binding.ivRightIntroduce.isVisible = true
                    binding.tvStep.text = "Introduce"
                    binding.tvTitle.text =
                        binding.root.context.getString(R.string.onboarding_title_message)
                }

                1 -> {
                    binding.tvFirstStepAnswer.isVisible = true
                    binding.tvSecondStepAnswer.isVisible = true
                    binding.tvTitle.text =
                        binding.root.context.getString(R.string.onboarding1_title_message)
                    binding.tvStep.text = "STEP1"
                }

                2 -> {
                    binding.tvTitle.text =
                        binding.root.context.getString(R.string.onboarding2_title_message)
                    binding.tvStep.text = "STEP2"
                }

                3 -> {
                    binding.tvTitle.text =
                        binding.root.context.getString(R.string.onboarding3_title_message)
                    binding.tvStep.text = "STEP3"
                }
            }
        }
    }
}
