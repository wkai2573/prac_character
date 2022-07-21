package me.wkai.prac_character.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import me.wkai.prac_character.data.model.Chara
import me.wkai.prac_character.ui.LoadingCard

@Composable
fun HomeScreen(
	viewModel:HomeViewModel = hiltViewModel()
) {
	val charaList by viewModel.charaList.collectAsState()
	val isLoading by viewModel.isLoading.collectAsState()


	val scaffoldState = rememberScaffoldState() //鷹架state

	Scaffold(
		//鷹架state
		scaffoldState = scaffoldState,
		//浮動按鈕
		floatingActionButton = {
			FloatingActionButton(
				onClick = {
					viewModel.getCharaList_flow_unity()
				},
				backgroundColor = MaterialTheme.colors.secondary,
				content = {
					Icon(imageVector = Icons.Default.Save, contentDescription = "Save")
				}
			)
		},
	) {
		Box {
			//清單
			LazyColumn {
				items(charaList) { chara:Chara ->
					CharaImageCard(chara = chara)
				}
			}
			//讀取遮罩
			if (isLoading /*charaList.isEmpty()*/) {
				LoadingCard()
			}
		}
	}


//	Column {
//		//讀取按鈕
//		Row(
//			modifier = Modifier.fillMaxWidth().padding(4.dp),
//			horizontalArrangement = Arrangement.SpaceAround,
//		) {
//			Button(
//				onClick = { viewModel.getCharaList_request() },
//				content = { Text("Request<>") }
//			)
//			Button(
//				onClick = { viewModel.getCharaList() },
//				content = { Text("normal") }
//			)
//		}
//		Row(
//			modifier = Modifier.fillMaxWidth().padding(4.dp),
//			horizontalArrangement = Arrangement.SpaceAround,
//		) {
//			Button(
//				onClick = { viewModel.getCharaList_flow_noSave() },
//				content = { Text("Flow_noSave") }
//			)
//			Button(
//				onClick = { viewModel.getCharaList_flow() },
//				content = { Text("save") }
//			)
//			Button(
//				onClick = { viewModel.getCharaList_flow_unity() },
//				content = { Text("unity") }
//			)
//		}
//		Row(
//			modifier = Modifier.fillMaxWidth().padding(4.dp),
//			horizontalArrangement = Arrangement.SpaceAround,
//		) {
//			Button(
//				onClick = { viewModel.getData() },
//				content = { Text("test") }
//			)
//		}
//		Divider(color = Color.Gray)
//		Box {
//			//清單
//			LazyColumn {
//				items(charaList) { chara:Chara ->
//					CharaImageCard(chara = chara)
//				}
//			}
//			//讀取遮罩
//			if (isLoading /*charaList.isEmpty()*/) {
//				LoadingCard()
//			}
//		}
//	}
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