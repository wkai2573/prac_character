package me.wkai.prac_character.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

//==自訂元件==

//讀取遮罩
@Composable
fun LoadingCard() {
	Surface(
		modifier = Modifier
			.padding(10.dp)
			.fillMaxSize()
			.wrapContentSize(align = Alignment.Center)
			.size(200.dp),
		shape = RoundedCornerShape(25.dp),
		color = Color.Gray.copy(alpha = 0.6f)
	) {
		CircularProgressIndicator(
			modifier = Modifier.padding(10.dp),
			strokeWidth = 20.dp
		)
	}
}

@Preview(showBackground = true)
@Composable
fun Preview_LoadingCard() {
	Column(Modifier.background(Color.Red.copy(alpha = 0.5f))) {
		LoadingCard()
	}
}