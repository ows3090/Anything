package com.ows.gemini.anything.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.ows.gemini.anything.data.db.entity.RecentlyFoodEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface RecentlyFoodDao {
    @Query("SELECT * FROM RecentlyFoodEntity ORDER BY time DESC")
    fun getAll(): Flowable<List<RecentlyFoodEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: RecentlyFoodEntity): Long

    @Delete
    fun delete(entity: RecentlyFoodEntity): Completable

    @Query("SELECT * FROM RecentlyFoodEntity WHERE time=(SELECT min(time) FROM RecentlyFoodEntity)")
    fun findOldest(): Flowable<RecentlyFoodEntity>

    @Transaction
    fun insertTransaction(entity: RecentlyFoodEntity): Long {
        val allList = getAll().blockingFirst()
        if (allList.size >= 10) {
            delete(findOldest().blockingFirst()).subscribe()
        }
        return insert(entity)
    }
}
