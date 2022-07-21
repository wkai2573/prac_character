package me.wkai.prac_character.ui.compose.multifab

import androidx.compose.ui.graphics.vector.ImageVector

class MultiFabItem(
	val icon:ImageVector,
	val label:String,
	val onClick:() -> Unit
)