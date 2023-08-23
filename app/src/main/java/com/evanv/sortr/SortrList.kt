package com.evanv.sortr

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.Stack

@Entity(tableName = "lists")
data class SortrList(@ColumnInfo(name = "name") val name: String,
                     @ColumnInfo(name = "img") val img: String,
                     @ColumnInfo(name = "list") var dbList: MutableList<Long> = mutableListOf(),
                     @ColumnInfo(name = "completed") var completed: Boolean = false,
                     @ColumnInfo(name = "stack") var stack: Stack<Pair<Int, Int>> = Stack(),
                     @ColumnInfo(name = "currComparison") var currComparison: Pair<Int, Int> =
                         Pair(0, 0),
                     @ColumnInfo(name = "i") var i: Int = 0,
                     @ColumnInfo(name = "lo") var lo: Int = 0,
                     @ColumnInfo(name = "hi") var hi: Int = 0,
                     @ColumnInfo(name = "j") var j: Int = 0,
                     @ColumnInfo(name = "initialized") var init: Boolean = false,
                     @ColumnInfo(name = "cache") var cache: MutableMap<Long, MutableSet<Long>>
                        = mutableMapOf(),
                     @ColumnInfo(name = "finishedAdding") var finishedAdding: Boolean = false) {

    @Ignore
    private val internalList = mutableListOf<Item>()

    val list: List<Item>
        get() {
            return internalList
        }

    @PrimaryKey(autoGenerate = true)
    var uid: Long = 0

    fun updateList(items: List<Item>) {
        for (item in items) {
            if (item.uid in dbList) {
                internalList.add(item)
            }
        }
    }

    fun addToList(item: Item) {
        internalList.add(item)
        dbList.add(item.uid)
    }

    fun swap(i: Int, j: Int) {
        val temp = internalList[i]
        internalList[i] = internalList[j]
        internalList[j] = temp

        val temp2 = dbList[i]
        dbList[i] = dbList[j]
        dbList[j] = temp2
    }

    fun copy(): SortrList {
        val list = SortrList(name, img, dbList.toMutableList())

        for (item in dbList) {
            list.cache[item] = mutableSetOf()
        }

        return list
    }
}