package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase5.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StockDao {

    @Query("SELECT * FROM stock")
    fun currentStockPricesAsFlow(): Flow<List<StockEntity>>

    @Query("SELECT * FROM stock")
    suspend fun currentStockPrices(): List<StockEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stockEntityList: List<StockEntity>)

    @Query("DELETE FROM stock")
    suspend fun clear()
}