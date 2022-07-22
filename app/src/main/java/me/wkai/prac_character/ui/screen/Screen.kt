package me.wkai.prac_character.ui.screen

sealed class Screen(val route:String) {
	object HomeScreen : Screen("home_screen")
	object ScanScreen : Screen("scan_screen")
}