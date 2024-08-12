package com.ows.gemini.anything.presentation.result

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.ai.client.generativeai.GenerativeModel
import com.google.firebase.storage.FirebaseStorage
import com.ows.gemini.anything.BuildConfig
import com.ows.gemini.anything.R
import com.ows.gemini.anything.databinding.ActivityResultBinding
import com.ows.gemini.anything.presentation.base.BaseActivity
import com.ows.gemini.anything.presentation.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.ByteArrayOutputStream

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
    private var result: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()
        initViews()
        bindViews()
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
        binding.gEmpty.isVisible = true
        binding.gResult.isGone = true
        lifecycleScope.launch {
            val response = genertativeModel.generateContent(prompt)
            result = response.text?.filter { it.isLetter() } ?: ""
            Timber.d("gemini response: $result")
            withContext(Dispatchers.Main) {
                binding.tvName.text = result
                binding.tvTitle.text =
                    String.format(getString(R.string.result_title_format), result)
            }
            viewModel.generateImage("$result on a clean background, realistic reflection of reality")
        }
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.tvMealtime.text = mealtime
    }

    private fun bindViews() {
        binding.btnHome.setOnClickListener {
            startActivity(
                HomeActivity.newIntent(this).apply {
                    addFlags(FLAG_ACTIVITY_CLEAR_TOP)
                },
            )
        }
    }

    private fun observeImage() {
        viewModel.foodImage.observe(this) { imageUrl ->
            Glide
                .with(this)
                .asBitmap()
                .load(imageUrl)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(24)))
                .into(
                    object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?,
                        ) {
                            binding.ivFood.setImageBitmap(resource)
                            binding.gEmpty.isGone = true
                            binding.gResult.isVisible = true
                            uploadImageToFirebase(bitmap = resource)
                            viewModel.saveRecommendation(result, mealtime)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                        }
                    },
                )
        }
    }

    private fun uploadImageToFirebase(bitmap: Bitmap) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val imageRef = storageRef.child("images/$result")

        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val bytes = stream.toByteArray()
        val task = imageRef.putBytes(bytes)
        task
            .addOnSuccessListener {
                Timber.d("success")
            }.addOnFailureListener {
                Timber.e("fail")
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
