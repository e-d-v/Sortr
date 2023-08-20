package com.evanv.sortr

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ListDao {
    @Query("SELECT * FROM lists")
    fun getLists(): MutableList<SortrList>

    @Insert
    fun insert(list: SortrList): Long

    @Delete
    fun delete(list: SortrList)

    @Update
    fun update(list: SortrList)
}
