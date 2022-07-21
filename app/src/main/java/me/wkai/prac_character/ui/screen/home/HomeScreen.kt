package me.wkai.prac_character.ui.screen.home

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import me.wkai.prac_character.data.model.Chara
import me.wkai.prac_character.ui.compose.Drawer
import me.wkai.prac_character.ui.compose.LoadingCard
import me.wkai.prac_character.ui.compose.multifab.MultiFabItem
import me.wkai.prac_character.ui.compose.multifab.MultiFabState
import me.wkai.prac_character.ui.compose.multifab.MultiFloatingActionButton

@Composable
fun HomeScreen(
	viewModel:HomeViewModel = hiltViewModel()
) {
	val charaList by viewModel.charaList.collectAsState()
	val isLoading by viewModel.isLoading.collectAsState()

	val scope = rememberCoroutineScope()
	val scaffoldState = rememberScaffoldState() //鷹架state
	var fabState by remember { mutableStateOf(MultiFabState.COLLAPSED) } //浮動按鈕state

	Scaffold(
		scaffoldState = scaffoldState,
		floatingActionButton = {
			MultiFloatingActionButton(
				fabIcon = Icons.Default.Add,
				items = listOf(
					MultiFabItem(
						icon = Icons.Default.Download,
						label = "讀取",
						onClick = {
							viewModel.getCharaList_flow_unity()
							fabState = MultiFabState.COLLAPSED
						}
					),
					MultiFabItem(
						icon = Icons.Default.Delete,
						label = "刪除全部",
						onClick = {
							viewModel.clearAllChara()
							fabState = MultiFabState.COLLAPSED
						}
					)
				),
				multiFabState = fabState,
				stateChanged = {
					fabState = it
				},
				showLabels = true,
				style = MaterialTheme.typography.body2,
			)
		},
		topBar = {
			TopAppBar(
				title = { Text(text = "App Name", style = MaterialTheme.typography.h6) },
				backgroundColor = MaterialTheme.colors.primarySurface,
				navigationIcon = {
					IconButton(
						onClick = { scope.launch { scaffoldState.drawerState.open() } },
						content = { Icon(imageVector = Icons.Default.Menu, contentDescription = "Drawer") },
					)
				}
			)
		},
		drawerContent = { Drawer(drawerState = scaffoldState.drawerState) },
	) {
		Box {
			//清單
			LazyColumn {
				items(charaList) { chara:Chara ->
					CharaImageCard(chara = chara)
				}
			}
			//讀取遮罩
			AnimatedVisibility(
				visible = isLoading,
				enter = fadeIn() + slideInVertically(),
				exit = fadeOut() + slideOutVertically(),
			) {
				LoadingCard()
			}
			//浮動按鈕遮罩
			AnimatedVisibility(
				visible = fabState == MultiFabState.EXPANDED,
				enter = fadeIn(),
				exit = fadeOut(),
			) {
				Box(
					modifier = Modifier
						.fillMaxSize()
						.background(Color.Gray.copy(alpha = 0.4f))
						.clickable(enabled = false) {} //擋住後面元件點擊事件
				)
			}
		}
	}
}

@Composable
fun CharaImageCard(chara:Chara) {
	Card(
		shape = MaterialTheme.shapes.medium,
		modifier = Modifier.padding(16.dp)
	) {
		Box {
			AsyncImage(
				model = chara.image,
				contentDescription = null,
				modifier = Modifier.fillMaxWidth().height(200.dp),
				contentScale = ContentScale.FillBounds
			)
			Surface(
				color = MaterialTheme.colors.onSurface.copy(alpha = .3f),
				modifier = Modifier.align(Alignment.BottomCenter),
				contentColor = MaterialTheme.colors.surface
			) {
				Column(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
					Text(text = "Real name: ${chara.actor}")
					Text(text = "Actor name: ${chara.name}")
				}
			}
		}
	}
}