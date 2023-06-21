package com.liz.presentation.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.liz.presentation.R
import com.liz.presentation.common.enumdata.Route
import com.liz.presentation.common.ui.drawer.DrawerItemData
import com.liz.presentation.ui.main.MainUi
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainUi(
                menuItemList = listOf(
                    DrawerItemData(
                        Route.SEARCH,
                        R.string.menu_search,
                        R.drawable.reading_glasses
                    ),
                    DrawerItemData(
                        Route.TEST,
                        R.string.menu_test,
                        R.drawable.ic_menu_gallery
                    )
                )
            )
        }
    }
}