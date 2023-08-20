package com.evanv.sortr

import androidx.room.TypeConverter
import java.util.Stack

class Converters {
    @TypeConverter
    fun longListFromString(value: String): MutableList<Long> {
        return value.split(",").map { it.toLong() }.toMutableList()
    }

    @TypeConverter
    fun stringFromLongList(list: List<Long>): String {
        var toReturn = ""

        for (num in list) {
            toReturn = "$toReturn$num,"
        }

        toReturn = toReturn.dropLast(1)

        return toReturn
    }

    @TypeConverter
    fun pairStackFromString(value: String): Stack<Pair<Int, Int>> {
        val toReturn = Stack<Pair<Int, Int>>()

        if (value == "") {
            return toReturn
        }

        val list = value.split(",").map { it.toInt() }

        for (i in 0..list.size step 2) {
            toReturn.push(Pair(list[i], list[i + 1]))
        }

        return toReturn
    }

    @TypeConverter
    fun stringFromPairStack(stack: Stack<Pair<Int, Int>>): String {
        var toReturn = ""

        for (i in (stack.size - 1)downTo 0) {
            val first = stack[i].first
            val second = stack[i].second

            toReturn = "$toReturn$first,$second,"
        }

        toReturn = toReturn.dropLast(1)

        return toReturn
    }

    @TypeConverter
    fun pairToLong(value: Pair<Int, Int>): Long {
        return (value.first.toLong() shl 32) + value.second.toLong()
    }

    @TypeConverter
    fun longToPair(value: Long): Pair<Int, Int> {
        return Pair((value shr 32).toInt(), (value and Int.MAX_VALUE.toLong()).toInt())
    }

    @TypeConverter
    fun mutableMapFromString(value: String): MutableMap<Long, MutableSet<Long>> {
        val sets = value.split(";")

        val toReturn: MutableMap<Long, MutableSet<Long>> = mutableMapOf()

        if (sets[0] == "") {
            return toReturn
        }

        for (set in sets) {
            val index = set.split(":")[0].toLong()
            val items = set.split(":")[1].split(",")

            val thisSet: MutableSet<Long> = mutableSetOf()

            if (items[0] != "") {
                for (item in items.map { it.toLong() }) {
                    thisSet.add(item)
                }
            }

            toReturn[index] = thisSet
        }

        return toReturn
    }

    @TypeConverter
    fun stringFromMutableMap(map: MutableMap<Long, MutableSet<Long>>) : String {
        var toReturn = ""

        for (key in map.keys) {
            val set = map[key]

            toReturn = "$toReturn$key:"

            for (item in set!!) {
                toReturn = "$toReturn$item,"
            }

            if (set.size != 0) {
                toReturn = toReturn.dropLast(1)
            }
            toReturn = "$toReturn;"

        }

        toReturn = toReturn.dropLast(1)

        return toReturn
    }
}
