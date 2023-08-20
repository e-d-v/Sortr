package com.evanv.sortr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.room.Room
import com.evanv.sortr.ui.theme.SortrTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(applicationContext, SortrDatabase::class.java,
            "sortr-db").build()

        var listOfLists: MutableList<SortrList>? = null
        var listDao: ListDao? = null
        var itemDao: ItemDao? = null

        setContent {
            SortrTheme {
                // should show loading screen
                val isLoading: MutableState<Boolean> = remember { mutableStateOf(true) }

                if (isLoading.value) {
                    Thread {
                        listDao = db.listDao()
                        itemDao = db.itemDao()
                        listOfLists = listDao!!.getLists()
                        val items = itemDao!!.getItems()

                        for (list in listOfLists!!) {
                            list.updateList(items)
                        }

                        isLoading.value = false
                    }.start()
                }

                // A surface container using the 'background' color from the theme
                val activeList: MutableState<String?> = remember { mutableStateOf(null) }
                // If addMode = 0, not adding, if 1, setting name and such, if 2, adding to list
                val addMode: MutableState<Int> = remember { mutableStateOf(0) }

                if (!isLoading.value) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    if (addMode.value != 0) {
                                        Text(text = "Add New List")
                                    } else if (activeList.value == null) {
                                        Text(text = "Sortr")
                                    } else {
                                        Text(text = activeList.value!!)
                                    }
                                },
                                navigationIcon = {
                                    if (activeList.value != null) {
                                        IconButton(onClick = {
                                            activeList.value = null
                                        }) {
                                            Icon(
                                                Icons.Filled.ArrowBack,
                                                "backIcon",
                                                tint = Color.White
                                            )
                                        }
                                    }
                                },
                                colors = TopAppBarDefaults.smallTopAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    titleContentColor = Color.White,
                                ),
                                actions = {
                                    IconButton(onClick = {
                                        if (activeList.value == null || getList(activeList.value!!,
                                                listOfLists!!)!!.completed) {
                                            addMode.value = 1
                                        }
                                    }) {
                                        Icon(Icons.Filled.Add, "add", tint = Color.White)
                                    }
                                }
                            )
                        }, content = { padding ->
                            if (addMode.value != 0 && activeList.value == null) {
                                AddListScreen(addMode, listOfLists!!, padding, listDao!!, itemDao!!)
                            } else if (addMode.value != 0) {
                                if (getList(activeList.value!!, listOfLists!!)!!.finishedAdding) {
                                    AddItemToList(addMode, padding,
                                        getList(activeList.value!!, listOfLists!!)!!, listDao!!,
                                        itemDao!!
                                    )
                                }
                                else {
                                    Mode2(addMode, padding,
                                        getList(activeList.value!!, listOfLists!!)!!, listDao!!,
                                        itemDao!!, activeList)
                                }
                            } else if (activeList.value == null) {
                                ListOfListsScreen(padding, listOfLists!!, activeList)
                            } else if ("duplicate::" in activeList.value!!) {
                                val toDuplicate = activeList.value!!.replace("duplicate::", "")
                                activeList.value = null

                                ListOfListsScreen(padding, listOfLists!!, activeList)

                                Thread {
                                    val newList = getList(toDuplicate, listOfLists!!)!!.copy()
                                    val uid = listDao?.insert(newList)

                                    newList.uid = uid!!

                                    listOfLists!!.add(newList)

                                    activeList.value = null
                                }.start()
                            } else {
                                if (getList(activeList.value!!, listOfLists!!)!!.completed) {
                                    CompletedList(
                                        getList(activeList.value!!, listOfLists!!)!!.list,
                                        padding
                                    )
                                }
                                else if (!getList(activeList.value!!, listOfLists!!)!!.finishedAdding) {
                                    addMode.value = 2
                                }
                                else {
                                    val logic = remember {
                                        SorterLogic(
                                            getList(activeList.value!!, listOfLists!!)!!,
                                            listDao!!
                                        )
                                    }

                                    SorterScreen(padding, logic)
                                }
                            }
                        }
                    )
                }
                else {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

fun getList(name: String, listOfLists: MutableList<SortrList>): SortrList? {
    for (list in listOfLists) {
        if (list.name == name) {
            return list
        }
    }

    return null
}
