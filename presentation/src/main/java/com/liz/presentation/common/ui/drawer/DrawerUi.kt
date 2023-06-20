package com.liz.presentation.common.ui.drawer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import com.liz.presentation.common.enumdata.Route
import kotlinx.coroutines.launch

@Composable
fun DrawerUi(
    menuItemList: List<DrawerItemData>,
    drawerState: DrawerState,
    defaultRoute: Route,
    onClick: (Route) -> Unit
) {
    var currentRoute by remember { mutableStateOf(defaultRoute) }
    val coroutineScope = rememberCoroutineScope()

    ModalDrawerSheet {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(menuItemList) { item ->
                        DrawerItem(item = item) { route ->
                            coroutineScope.launch {
                                drawerState.close()
                            }
                            if (currentRoute == route) {
                                return@DrawerItem
                            }

                            currentRoute = route
                            onClick(route)
                        }
                    }
                }
            }
        }
    }
}