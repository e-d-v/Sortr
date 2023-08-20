package com.evanv.sortr

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun SorterScreen(padding: PaddingValues, logic: SorterLogic) {
    Column (
        Modifier.padding(
            top = padding.calculateTopPadding() + 8.dp,
            start = padding.calculateStartPadding(LocalLayoutDirection.current)
                    + 8.dp,
            end = padding.calculateEndPadding(LocalLayoutDirection.current)
                    + 8.dp,
            bottom = padding.calculateBottomPadding() + 8.dp
        )
    ) {
        val item1: MutableState<Item> = remember { mutableStateOf(logic.compare.first) }
        val item2: MutableState<Item> = remember { mutableStateOf(logic.compare.second) }
        val complete: MutableState<Boolean> = remember { mutableStateOf(false) }

        if (!complete.value) {
            Matchup(item1, item2, logic, complete)
        }
        else {
            val list: ArrayList<Pair<String, String?>> = ArrayList()

            for (item in logic.list.list) {
                list.add(Pair(item.name, item.img))
            }

            CompletedList(padding, list)
        }
    }
}

@Composable
fun Matchup(item1: MutableState<Item>, item2: MutableState<Item>, logic: SorterLogic, complete: MutableState<Boolean>) {
    Item(item1, item2, Modifier.fillMaxHeight(0.5f), logic, false, complete)
    Item(item1, item2, Modifier.fillMaxHeight(), logic, true, complete)
}

@Composable
fun Item(item1: MutableState<Item>, item2: MutableState<Item>, modifier: Modifier, logic: SorterLogic, first: Boolean, complete: MutableState<Boolean>) {
    val item = if (first) item2.value else item1.value
    Column (
        modifier = modifier.clickable {
            logic.applyComparison(first)

            if (!logic.complete) {
                item1.value = logic.compare.first
                item2.value = logic.compare.second
            } else {
                complete.value = true
            }
        },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (item.img != null) {
            AsyncImage(
                model = item.img,
                contentDescription = "user supplied image",
                modifier = Modifier.fillMaxWidth().weight(1f)
            )
        }
        Text(
            text = item.name,
            style = MaterialTheme.typography.headlineSmall
        )
        if (item.desc != null) {
            Text(text = item.desc!!)
        }
        Spacer(Modifier.height(4.dp))
    }
}