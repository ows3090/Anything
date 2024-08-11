package com.ows.gemini.anything.presentation.result

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.ai.client.generativeai.GenerativeModel
import com.ows.gemini.anything.BuildConfig
import com.ows.gemini.anything.R
import com.ows.gemini.anything.databinding.ActivityResultBinding
import com.ows.gemini.anything.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@AndroidEntryPoint
class ResultActivity : BaseActivity<ActivityResultBinding>(R.layout.activity_result) {
    private val viewModel by viewModels<ResultViewModel>()
    private val genertativeModel =
        GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = BuildConfig.GEMINI_KEY,
        )

    private var prompt: String = ""
    private var mealtime: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()
        initViews()
        observeImage()
    }

    private fun setup() {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        mealtime = intent.getStringExtra(MEALTIME) ?: ""
        prompt = intent.getStringExtra(PROMPT) ?: ""
        Timber.d("Final prompt\n$prompt")
    }

    private fun initViews() {
        lifecycleScope.launch {
            val response = genertativeModel.generateContent(prompt)
            val result = response.text?.filter { it.isLetter() } ?: ""
            Timber.d("gemini response: $result")
            withContext(Dispatchers.Main) {
                binding.tvName.text = result
                binding.tvTitle.text =
                    String.format(getString(R.string.result_title_format), result)
            }
            // viewModel.generateImage("only $result image")
        }
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.tvMealtime.text = mealtime
    }

    private fun observeImage() {
        viewModel.foodImage.observe(this) { imageUrl ->
            Glide
                .with(this)
                .load(imageUrl)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(24)))
                .into(binding.ivFood)
        }
    }

    companion object {
        const val PROMPT = "PROMPT"
        const val MEALTIME = "MEALTIME"

        fun newIntent(
            context: Context,
            mealTime: String,
            prompt: String,
        ): Intent =
            Intent(context, ResultActivity::class.java).apply {
                putExtra(MEALTIME, mealTime)
                putExtra(PROMPT, prompt)
            }
    }
}
