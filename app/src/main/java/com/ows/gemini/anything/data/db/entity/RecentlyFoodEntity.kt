package com.ows.gemini.anything.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ows.gemini.anything.data.model.RecentlyFoodModel

@Entity
data class RecentlyFoodEntity(
    @PrimaryKey val name: String,
    val time: Long,
    val meta: String,
) {
    fun toModel() =
        RecentlyFoodModel(
            name = name,
            time = time,
            meta = meta,
        )
}
