package com.ows.gemini.anything.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.ows.gemini.anything.R
import com.ows.gemini.anything.data.model.RecentlyFoodModel
import com.ows.gemini.anything.databinding.ItemHomeImageBinding
import timber.log.Timber

class HomeImageAdapter : RecyclerView.Adapter<HomeImageAdapter.ImageViewHolder>() {
    private var foodModels: List<RecentlyFoodModel> = listOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ImageViewHolder =
        ImageViewHolder(
            ItemHomeImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
        )

    override fun onBindViewHolder(
        holder: ImageViewHolder,
        position: Int,
    ) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = foodModels.size

    inner class ImageViewHolder(
        private val binding: ItemHomeImageBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.tvMeta.text = foodModels[position].meta
            binding.tvName.text = foodModels[position].name

            Glide
                .with(binding.root)
                .load(foodModels[position].imageUrl)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
                .placeholder(R.drawable.bg_all_round_corner_carbongrey_radius_20dp)
                .into(binding.ivImage)
        }
    }

    fun setFoodModels(models: List<RecentlyFoodModel>) {
        Timber.d("local food images url: $models")
        foodModels = models
        notifyDataSetChanged()
    }
}
