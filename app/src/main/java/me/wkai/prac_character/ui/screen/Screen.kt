package me.wkai.prac_character.ui.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Scanner
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
	val route:String,
	val text:String,
	val icon:ImageVector
) {
	object HomeScreen : Screen("home_screen", "Home", Icons.Outlined.Home)
	object CharaScreen : Screen("chara_screen", "Chara", Icons.Outlined.List)
	object ScanScreen : Screen("scan_screen", "Scan", Icons.Outlined.Scanner)

	companion object {
		val Screens = listOf(HomeScreen, CharaScreen, ScanScreen)
	}
}