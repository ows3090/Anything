package com.ows.gemini.anything.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ows.gemini.anything.R
import com.ows.gemini.anything.databinding.ItemHomeImageBinding

class HomeImageAdapter : RecyclerView.Adapter<HomeImageAdapter.ImageViewHolder>() {
    private var imageUrls: List<String> = listOf("", "", "", "")

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

    override fun getItemCount(): Int = imageUrls.size

    inner class ImageViewHolder(
        private val binding: ItemHomeImageBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.ivImage.backgroundTintList =
                binding.root.context.resources
                    .getColorStateList(R.color.sunshade, null)
        }
    }

    fun setImageUrls(urls: List<String>) {
        imageUrls = urls
        notifyDataSetChanged()
    }
}
