package com.ows.gemini.anything.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ows.gemini.anything.data.db.dao.RecentlyFoodDao
import com.ows.gemini.anything.data.db.entity.RecentlyFoodEntity

@Database(
    entities = [RecentlyFoodEntity::class],
    version = 1,
)
abstract class AnythingDatabase : RoomDatabase() {
    abstract fun recentlyFoodDao(): RecentlyFoodDao
}
