package com.evanv.sortr

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ListOfListsScreen(padding: PaddingValues, listOfLists: MutableList<SortrList>,
                      activeList: MutableState<String?>
) {
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
            ListItem(
                name = listOfLists[index].name,
                photo = listOfLists[index].img,
                activeList = activeList
            )
        }
    }
}


@Composable
fun ListItem(name: String, photo: String, activeList: MutableState<String?>) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable {
                    activeList.value = name
                }
                .fillMaxWidth().weight(1f)
        ) {
            AsyncImage(
                model = photo,
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
                style = MaterialTheme.typography.headlineMedium,
            )
        }
        IconButton(onClick = {
            activeList.value = "duplicate::$name"
        }) {
            Icon(Icons.Outlined.AddCircle, "duplicate", tint = Color.Black)
        }
    }
    Spacer(
        modifier = Modifier
            .height(8.dp)
    )
}