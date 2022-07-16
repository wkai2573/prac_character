package me.wkai.prac_character.ui.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import me.wkai.prac_character.data.model.Character
import me.wkai.prac_character.data.repository.CharacterRepo
import javax.inject.Inject

//MVVM & StateFlow說明: https://blog.csdn.net/Androiddddd/article/details/121931065

//Hilt:
//@HiltViewModel: 有使用Hilt的ViewModel需要加 (會幫處理好ViewModelProvider.Factory的東西)
//@Inject 在class用表示此class是可被注入的
data class Foo(val text:String = "")

@HiltViewModel
class HomeViewModel @Inject constructor(
	//注入_角色存儲庫(所以在ui創vm時不用自己處理Factory的東西了)
	private val characterRepo:CharacterRepo
) : ViewModel() {

	//讀取中
	private val _isLoading = MutableStateFlow(false)
	val isLoading:StateFlow<Boolean> = _isLoading

	//角色清單(StateFlow≒LiveData)
	private val _characters = MutableStateFlow(emptyList<Character>())
	val characters:StateFlow<List<Character>> = _characters

	fun getCharacters() = viewModelScope.launch {
		// _characters.value = emptyList()
		_isLoading.value = true

		characterRepo.getCharacters().also { response ->
			if (response.isSuccessful) {
				_characters.value = response.body()!!
			} else {
				Log.i("@@@", response.message())
			}
		}
		_isLoading.value = false
	}

	//初始化
	//init {
	//	getCharacters()
	//}
}
