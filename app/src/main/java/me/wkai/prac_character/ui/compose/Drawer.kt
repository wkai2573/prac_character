package me.wkai.prac_character.ui.compose

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.launch
import me.wkai.prac_character.ui.screen.Screen


@Composable
fun Drawer(
	drawerState:DrawerState,
	navController:NavHostController,
) {

	//導航
	val navBackStackEntry by navController.currentBackStackEntryAsState()
	val currentRoute = navBackStackEntry?.destination?.route?.run {
		if (contains("?")) substring(0, indexOf("?")) else this //移除後面參數
	} ?: ""

	//導航:防止重複堆棧
	val navOptionsBuilder:NavOptionsBuilder.() -> Unit = {
		popUpTo(navController.graph.findStartDestination().id) {
			saveState = true
		}
		launchSingleTop = true
		restoreState = true
	}

	val context = LocalContext.current
	val scroll = rememberScrollState()

	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colors.background)
			.verticalScroll(state = scroll)
	) {
		Text(
			text = "Screen",
			style = MaterialTheme.typography.h5,
			modifier = Modifier.padding(start = 32.dp, top = 20.dp, bottom = 16.dp)
		)
		Screen.Screens.forEach {
			DrawerItem(
				drawerState = drawerState,
				text = it.text,
				bubbleText = "",
				imageVector = it.icon,
				selected = currentRoute == it.route,
				onClick = {
					navController.navigate(route = it.route, builder = navOptionsBuilder)
				}
			)
		}

		Divider(Modifier.padding(horizontal = 20.dp, vertical = 10.dp))

		Text(
			text = "Mail",
			style = MaterialTheme.typography.h5,
			modifier = Modifier.padding(start = 32.dp, top = 20.dp, bottom = 16.dp)
		)
		var mailSelectedState by remember { mutableStateOf("Mail") }
		DrawerItem(
			drawerState = drawerState,
			text = "Mail",
			bubbleText = "123",
			imageVector = Icons.Outlined.Mail,
			selected = mailSelectedState == "Mail",
			onClick = {
				mailSelectedState = "Mail"
			}
		)
		DrawerItem(
			drawerState = drawerState,
			text = "Outbox",
			bubbleText = "",
			imageVector = Icons.Outlined.Outbox,
			selected = mailSelectedState == "Outbox",
			onClick = {
				mailSelectedState = "Outbox"
			}
		)
		DrawerItem(
			drawerState = drawerState,
			text = "Favorites",
			bubbleText = "",
			imageVector = Icons.Outlined.Favorite,
			selected = mailSelectedState == "Favorites",
			onClick = {
				mailSelectedState = "Favorites"
			}
		)
		DrawerItem(
			drawerState = drawerState,
			text = "Archive",
			bubbleText = "",
			imageVector = Icons.Outlined.Archive,
			selected = mailSelectedState == "Archive",
			onClick = {
				mailSelectedState = "Archive"
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
