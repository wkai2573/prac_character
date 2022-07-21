package me.wkai.prac_character.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Outbox
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun Drawer(drawerState:DrawerState) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colors.background)
	) {
		Text(
			text = "Mail",
			style = MaterialTheme.typography.h5,
			modifier = Modifier.padding(start = 36.dp, top = 20.dp, bottom = 16.dp)
		)
		var drawerSelectedItemState by remember { mutableStateOf("Mail") }
		DrawerItem(
			drawerState = drawerState,
			text = "Mail",
			bubbleText = "123",
			imageVector = Icons.Outlined.Mail,
			selected = drawerSelectedItemState == "Mail",
			onClick = {
				drawerSelectedItemState = "Mail"
			}
		)
		DrawerItem(
			drawerState = drawerState,
			text = "Outbox",
			bubbleText = "",
			imageVector = Icons.Outlined.Outbox,
			selected = drawerSelectedItemState == "Outbox",
			onClick = {
				drawerSelectedItemState = "Outbox"
			}
		)
		DrawerItem(
			drawerState = drawerState,
			text = "Favorites",
			bubbleText = "",
			imageVector = Icons.Outlined.Favorite,
			selected = drawerSelectedItemState == "Favorites",
			onClick = {
				drawerSelectedItemState = "Favorites"
			}
		)
		DrawerItem(
			drawerState = drawerState,
			text = "Archive",
			bubbleText = "",
			imageVector = Icons.Outlined.Archive,
			selected = drawerSelectedItemState == "Archive",
			onClick = {
				drawerSelectedItemState = "Archive"
			}
		)
	}
}

@Composable
fun DrawerItem(
	drawerState:DrawerState,
	text:String,
	bubbleText:String = "",
	imageVector:ImageVector,
	selected:Boolean = false,
	onClick:() -> Unit = {},
) {
	val scope = rememberCoroutineScope()
	val color = if (selected) MaterialTheme.colors.surface else MaterialTheme.colors.background
	Surface(
		modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
		color = color,
		shape = RoundedCornerShape(50.dp),
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.clickable {
					scope.launch { drawerState.close() }
					onClick()
				}
				.padding(horizontal = 8.dp, vertical = 16.dp),
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Row {
				Image(
					imageVector = imageVector,
					contentDescription = null,
					modifier = Modifier.padding(start = 20.dp)
				)
				Text(
					text = text,
					modifier = Modifier.padding(start = 12.dp),
					style = MaterialTheme.typography.button
				)
			}
			Text(
				text = bubbleText,
				modifier = Modifier.padding(end = 20.dp),
				style = MaterialTheme.typography.button
			)
		}
	}
}
