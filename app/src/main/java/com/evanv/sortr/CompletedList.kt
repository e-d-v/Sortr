package com.evanv.sortr

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun CompletedList(listOfLists: List<Item>, padding: PaddingValues) {
    val convertedListOfLists = ArrayList<Pair<String, String?>>()

    for (item in listOfLists) {
        convertedListOfLists.add(Pair(item.name, item.img))
    }

    CompletedList(padding, convertedListOfLists)
}

@Composable
fun CompletedList(padding: PaddingValues, listOfLists: ArrayList<Pair<String, String?>>) {
    LazyColumn (
        Modifier.padding(
            top = padding.calculateTopPadding() + 8.dp,
            start = padding.calculateStartPadding(LocalLayoutDirection.current)
                    + 8.dp,
            end = padding.calculateEndPadding(LocalLayoutDirection.current)
                    + 8.dp,
            bottom = padding.calculateBottomPadding() + 8.dp
        )

    ) {
        items (listOfLists.size) { index ->
            val reversedIndex = listOfLists.size - index - 1

            ListItem(
                name = listOfLists[reversedIndex].first,
                photo = listOfLists[reversedIndex].second,
            )
        }
    }
}


@Composable
fun ListItem(name: String, photo: String?) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        AsyncImage(
            model = photo ?: "https://www.nbmchealth.com/wp-content/uploads/2018/04/default-placeholder.png",
            contentDescription = stringResource(R.string.list_icon_alttext_desc, name),
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(
            modifier = Modifier
                .width(8.dp)
        )
        Text(
            text = name,
            style = MaterialTheme.typography.headlineMedium
        )
    }
    Spacer(
        modifier = Modifier
            .height(8.dp)
    )
}
