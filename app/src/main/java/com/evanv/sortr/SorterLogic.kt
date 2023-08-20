package com.evanv.sortr

class SorterLogic(val list: SortrList, private val listDao: ListDao) {
    init {
        if (!list.init) {
            list.currComparison = Pair(0, list.list.size - 1)

            list.i = -1
            list.lo = 0
            list.hi = list.list.size - 1
            list.j = 0

            list.init = true
        }
    }

    // The current comparison the user needs to make
    val compare: Pair<Item, Item>
        get() {
            return Pair(list.list[list.currComparison.first], list.list[list.currComparison.second])
        }
    // True if is sorted and can be displayed
    var complete = false

    // False if user chose item1, True if user chose item2
    fun applyComparison(result: Boolean) {
        if (result) {
            // Update list.cache now that we know item2 > item1
            list.cache[list.list[list.currComparison.first].uid]?.add(list.list[list.currComparison.second].uid)

            // If item2 > item1, than every item > item2 must also be > item1
            for (item in list.cache[list.list[list.currComparison.second].uid]!!) {
                list.cache[list.list[list.currComparison.first].uid]?.add(item)
            }

            // Swap items
            list.i++
            list.swap(list.i, list.j)
        }
        else {
            // Update list.cache now that we know item1 > item2
            list.cache[list.list[list.currComparison.second].uid]?.add(list.list[list.currComparison.first].uid)

            // If item1 > item2, than every item > item1 must also be > item2
            for (item in list.cache[list.list[list.currComparison.first].uid]!!) {
                list.cache[list.list[list.currComparison.second].uid]?.add(item)
            }
        }

        list.j++

        if (list.j != list.hi) {
            list.currComparison = Pair(list.j, list.hi)
        } else {
            // Update QuickSort bookkeeping with first QuickSort recursive call
            list.i++
            list.swap(list.i, list.hi)

            // Add second QuickSort recursive call to Stack
            if (list.i + 1 < list.hi) {
                list.stack.push(Pair(list.i + 1, list.hi))
            }

            list.hi = list.i - 1

            // If first QuickSort call is out of bounds, move onto the call on the top of the list.stack
            if (list.lo >= list.hi || list.lo < 0) {
                if (list.stack.empty()) {
                    complete = true
                    list.completed = true

                    Thread {
                        listDao.update(list)
                    }.start()
                    return
                }

                val pair = list.stack.pop()

                list.lo = pair.first
                list.hi = pair.second
                list.i = list.lo - 1
                list.j = list.lo

            } else {
                list.i = list.lo - 1
                list.j = list.lo
            }

            // Update current comparison for user
            list.currComparison = Pair(list.lo, list.hi)
        }

        // Check if we can applyComparison using the cache instead of asking the user
        if (list.cache[list.list[list.currComparison.second].uid]?.contains(list.list[list.currComparison.first].uid)
            == true) {
            applyComparison(false)
        }
        else if (list.cache[list.list[list.currComparison.first].uid]
            ?.contains(list.list[list.currComparison.second].uid)
            == true) {
            applyComparison(true)
        }

        Thread {
            listDao.update(list)
        }.start()
    }
}