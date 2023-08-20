package com.evanv.sortr

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(version = 1, entities = [Item::class, SortrList::class])
@TypeConverters(Converters::class)
abstract class SortrDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
    abstract fun listDao(): ListDao
}