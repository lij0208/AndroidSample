package com.liz.presentation.graph

import androidx.compose.material3.DrawerState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.liz.presentation.common.enumdata.Route
import com.liz.presentation.ui.search.SearchUi
import com.liz.presentation.ui.test.TestUi

fun NavGraphBuilder.mainGraph(drawerState: DrawerState) {
    composable(Route.SEARCH.name) {
        SearchUi(
            drawerState = drawerState,
        )
    }
    composable(Route.TEST.name) {
        TestUi(drawerState)
    }
}