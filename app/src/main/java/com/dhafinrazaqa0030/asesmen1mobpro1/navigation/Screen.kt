package com.dhafinrazaqa0030.asesmen1mobpro1.navigation

sealed class Screen(val route: String) {
    data object Home: Screen("mainScreen")
}