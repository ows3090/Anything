package com.ows.gemini.anything.presentation.recommendations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ows.gemini.anything.R
import com.ows.gemini.anything.databinding.ItemRecentlyFoodBinding

data class RecentlyFood(
    val name: String,
    val isExcept: Boolean = false,
)

class RecentlyFoodAdapter(
    private val onClick: (RecentlyFood) -> Unit = {},
) : RecyclerView.Adapter<RecentlyFoodAdapter.RecentlyFoodViewHolder>() {
    private var recentlyFoods: List<RecentlyFood> = listOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecentlyFoodViewHolder =
        RecentlyFoodViewHolder(
            ItemRecentlyFoodBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
        )

    override fun onBindViewHolder(
        holder: RecentlyFoodViewHolder,
        position: Int,
    ) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = recentlyFoods.size

    inner class RecentlyFoodViewHolder(
        private val binding: ItemRecentlyFoodBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.tvName.text = recentlyFoods[position].name
            binding.tvName.setOnClickListener { onClick(recentlyFoods[position]) }
            binding.tvName.backgroundTintList =
                binding.root.context.resources.getColorStateList(
                    if (recentlyFoods[position].isExcept) {
                        R.color.white
                    } else {
                        R.color.carbongrey
                    },
                    null,
                )
        }
    }

    fun setRecentlyFoods(recentlyFoods: List<RecentlyFood>) {
        this.recentlyFoods = recentlyFoods
        notifyDataSetChanged()
    }
}
