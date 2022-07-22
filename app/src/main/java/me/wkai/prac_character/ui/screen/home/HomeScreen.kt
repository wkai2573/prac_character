package me.wkai.prac_character.ui.screen.home

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import me.wkai.prac_character.data.model.Chara
import me.wkai.prac_character.ui.compose.LoadingCard
import me.wkai.prac_character.ui.compose.multifab.MultiFabItem
import me.wkai.prac_character.ui.compose.multifab.MultiFabState
import me.wkai.prac_character.ui.compose.multifab.MultiFloatingActionButton
import me.wkai.prac_character.ui.compose.swipe_refresh.SmartSwipeRefresh
import me.wkai.prac_character.ui.compose.swipe_refresh.SmartSwipeRefreshState

@Composable
fun HomeScreen(
	viewModel:HomeViewModel = hiltViewModel(),
	navController:NavHostController
) {
	val charaList by viewModel.charaList.collectAsState()
	val isLoading by viewModel.isLoading.collectAsState()

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
		}
	) {
		Box {
			//清單
//			val smartSwipeRefreshState = SmartSwipeRefreshState()
			SmartSwipeRefresh(
//				state = smartSwipeRefreshState,
				loadingIndicator = {
					Surface(
						modifier = Modifier.padding(vertical = 10.dp),
						shape = CircleShape,
						elevation = 5.dp,
					) {
						Icon(
							imageVector = Icons.Default.Refresh,
							contentDescription = "refresh",
//							modifier = Modifier.rotate(smartSwipeRefreshState.indicatorOffset.value)
						)
					}
				},
				onRefresh = {
					viewModel.getCharaList_flow_unity()
				},
			) {
				LazyColumn {
					itemsIndexed(charaList) { index:Int, chara:Chara ->
						CharaCard(chara = chara, isReverse = index % 2 == 1)
					}
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
fun CharaCard(chara:Chara, isReverse:Boolean = false) {
	Card(
		modifier = Modifier.padding(24.dp).height(160.dp),
		shape = RoundedCornerShape(20.dp),
		backgroundColor = MaterialTheme.colors.surface,
		elevation = 5.dp
	) {
		Row {
			if (isReverse) {
				CharaText(chara)
				CharaImage(chara)
			} else {
				CharaImage(chara)
				CharaText(chara)
			}
		}
	}
}

@Composable
fun RowScope.CharaImage(chara:Chara) {
	AsyncImage(
		modifier = Modifier.weight(1f).fillMaxHeight(),
		model = chara.image,
		contentDescription = null,
		contentScale = ContentScale.FillBounds
	)
}

@Composable
fun RowScope.CharaText(chara:Chara) {
	Column(
		modifier = Modifier.padding(12.dp).weight(1f).fillMaxHeight()
	) {
		Text(text = "Real name: ${chara.actor}", style = MaterialTheme.typography.body2, modifier = Modifier.padding(top = 8.dp))
		Text(text = "Actor name: ${chara.name}", style = MaterialTheme.typography.body2, modifier = Modifier.padding(top = 8.dp))
	}
}