package com.evanv.sortr

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import java.util.Stack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemToList(addMode: MutableState<Int>, padding: PaddingValues, list: SortrList, listDao: ListDao,
                  itemDao: ItemDao) {
    val itemUrl = remember { mutableStateOf("") }
    val itemDesc = remember { mutableStateOf("") }
    val itemName = remember { mutableStateOf("") }

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
        TextField(
            value = itemName.value,
            onValueChange = { itemName.value = it },
            label = { Text("Name of Item") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = itemDesc.value,
            onValueChange = { itemDesc.value = it },
            label = { Text("Description of Item") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = itemUrl.value,
            onValueChange = { itemUrl.value = it },
            label = { Text("URL To List Image") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = {
            val item = Item(itemName.value, itemDesc.value, itemUrl.value)
            Thread {
                val uid = itemDao.insert(item)
                item.uid = uid
                list.addToList(item)
                list.completed = false
                list.cache[uid] = mutableSetOf()
                list.init = false

                listDao.update(list)
            }.start()

            itemName.value = ""
            itemDesc.value = ""
            itemUrl.value = ""

            addMode.value = 0
        }) {
            Text("Add Item")
        }
    }
}
