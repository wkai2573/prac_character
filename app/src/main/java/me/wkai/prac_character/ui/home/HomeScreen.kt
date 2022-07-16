package me.wkai.prac_character.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import me.wkai.prac_character.data.model.Character
import me.wkai.prac_character.ui.LoadingCard

@Composable
fun HomeScreen(
	viewModel:HomeViewModel = hiltViewModel()
) {
	val characters by viewModel.characters.collectAsState()
	val isLoading by viewModel.isLoading.collectAsState()

	Column {
		//讀取按鈕
		Button(
			modifier = Modifier.fillMaxWidth().padding(10.dp),
			onClick = { viewModel.getCharacters() },
			content = { Text("getCharacters", fontSize = 30.sp) }
		)
		Divider(color = Color.Gray)
		Box {
			//清單
			LazyColumn {
				items(characters) { character:Character ->
					CharacterImageCard(character = character)
				}
			}
			//讀取遮罩
			if (isLoading /*characters.isEmpty()*/) {
				LoadingCard()
			}
		}
	}
}

@Composable
fun CharacterImageCard(character:Character) {
	Card(
		shape = MaterialTheme.shapes.medium,
		modifier = Modifier.padding(16.dp)
	) {
		Box {
			AsyncImage(
				model = character.image,
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
					Text(text = "Real name: ${character.actor}")
					Text(text = "Actor name: ${character.name}")
				}
			}
		}
	}
}
