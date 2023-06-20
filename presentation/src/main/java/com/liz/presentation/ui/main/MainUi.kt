package com.liz.presentation.ui.main

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.liz.presentation.R
import com.liz.presentation.common.enumdata.Route
import com.liz.presentation.common.ui.drawer.DrawerItemData
import com.liz.presentation.common.ui.drawer.DrawerUi
import com.liz.presentation.common.ui.theme.Theme
import com.liz.presentation.graph.mainGraph

@Preview
@Composable
fun MainActivityPreview() {
    MainUi(
        menuItemList = listOf(
            DrawerItemData(
                Route.SEARCH,
                R.string.menu_search,
                R.drawable.ic_menu_gallery
            )
        )
    )
}

@Composable
fun MainUi(
    menuItemList: List<DrawerItemData>,
    defaultRoute: Route = Route.SEARCH,
    navController: NavHostController = rememberNavController(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
) {
    Theme {
        Surface {
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    DrawerUi(
                        menuItemList = menuItemList,
                        drawerState = drawerState,
                        defaultRoute = defaultRoute
                    )
                    { route ->
                        setDrawableAction(navController, route)
                    }
                }
            ) {
                NavHost(
                    navController,
                    startDestination = defaultRoute.name
                ) {
                    mainGraph(drawerState)
                }
            }
        }
    }
}

private fun setDrawableAction(navController: NavHostController, route: Route) {
    navController.navigate(route.name) {
        popUpTo(route.name)
    }
}