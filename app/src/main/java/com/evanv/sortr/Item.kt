package com.evanv.sortr

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Item(@ColumnInfo(name = "name") var name: String,
                @ColumnInfo(name = "desc") var desc: String?,
                @ColumnInfo(name = "img") var img: String?) {
    @PrimaryKey(autoGenerate = true)
    var uid: Long = 0
}