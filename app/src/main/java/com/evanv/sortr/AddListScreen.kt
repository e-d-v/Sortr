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

@Composable
fun AddListScreen(addMode: MutableState<Int>, listOfLists: MutableList<SortrList>,
                  padding: PaddingValues, listDao: ListDao, itemDao: ItemDao) {
    val imageUrl = remember { mutableStateOf("") }
    val listName = remember { mutableStateOf("") }

    if (addMode.value == 1) {
        Mode1(imageUrl, listName, addMode, padding, listOfLists, listDao)
    }
    else if (addMode.value == 2) {
        Mode2(
            addMode,
            padding,
            getList(listName.value, listOfLists)!!,
            listDao,
            itemDao,
            null
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Mode1(imageUrl: MutableState<String>, listName: MutableState<String>, addMode: MutableState<Int>,
          padding: PaddingValues, listOfLists: MutableList<SortrList>, listDao: ListDao) {
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
            value = listName.value,
            onValueChange = { listName.value = it },
            label = { Text("Name of List") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = imageUrl.value,
            onValueChange = { imageUrl.value = it },
            label = { Text("URL To List Image") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = {
            addMode.value = 2

            val list = SortrList(listName.value, imageUrl.value)
            Thread {
                listOfLists.add(list)
                val uid = listDao.insert(list)
                list.uid = uid
            }.start()
        }) {
            Text("Create List")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Mode2(
    addMode: MutableState<Int>, padding: PaddingValues, list: SortrList, listDao: ListDao,
    itemDao: ItemDao, activeList: MutableState<String?>?) {
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

        Row {
            Button(onClick = {
                val item = Item(itemName.value, itemDesc.value, itemUrl.value)
                Thread {
                    val uid = itemDao.insert(item)
                    item.uid = uid
                    list.addToList(item)
                    list.cache[item.uid] = mutableSetOf()
                    listDao.update(list)
                }.start()

                itemName.value = ""
                itemDesc.value = ""
                itemUrl.value = ""
            }) {
                Text("Continue Adding")
            }

            Button(onClick = {
                val item = Item(itemName.value, itemDesc.value, itemUrl.value)
                Thread {
                    val uid = itemDao.insert(item)
                    item.uid = uid
                    list.addToList(item)
                    list.cache[item.uid] = mutableSetOf()
                    list.finishedAdding = true

                    listDao.update(list)
                }.start()

                itemName.value = ""
                itemDesc.value = ""
                itemUrl.value = ""

                addMode.value = 0

                if (activeList != null) {
                    activeList.value = null
                }
            }) {
                Text("Finish Adding")
            }
        }
    }
}
