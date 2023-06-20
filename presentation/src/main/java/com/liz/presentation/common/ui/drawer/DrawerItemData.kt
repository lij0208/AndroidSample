package com.liz.presentation.common.ui.drawer

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.liz.presentation.common.enumdata.Route

data class DrawerItemData(
    val route: Route,
    @StringRes val title: Int,
    @DrawableRes val drawableId: Int,
)
