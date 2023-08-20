package com.evanv.sortr

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ItemDao {
    @Query("SELECT * FROM items")
    fun getItems(): MutableList<Item>

    @Insert
    fun insert(item: Item): Long

    @Delete
    fun delete(item: Item)
}
