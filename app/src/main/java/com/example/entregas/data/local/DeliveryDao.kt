package com.example.entregas.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.entregas.domain.model.Delivery
import kotlinx.coroutines.flow.Flow

@Dao
interface DeliveryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(delivery: DeliveryEntity)

    @Update
    suspend fun update(delivery: DeliveryEntity)

    @Delete
    suspend fun delete(delivery: DeliveryEntity)

    @Query("SELECT * FROM delivery")
    fun getAll(): Flow<List<DeliveryEntity>>


}