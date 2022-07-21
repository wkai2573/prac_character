package me.wkai.prac_character.ui.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import me.wkai.prac_character.data.db.CharaDao
import me.wkai.prac_character.data.model.Chara
import me.wkai.prac_character.data.repository.CharaRepository
import javax.inject.Inject

//MVVM & StateFlow說明: https://blog.csdn.net/Androiddddd/article/details/121931065

//Hilt:
//@HiltViewModel: 有使用Hilt的ViewModel需要加 (會幫處理好ViewModelProvider.Factory的東西)
//@Inject 在class用表示此class是可被注入的
@HiltViewModel
class HomeViewModel @Inject constructor(
	//注入_角色存儲庫(所以在ui創vm時不用自己處理Factory的東西了)
	private val repository:CharaRepository,
	private val dao:CharaDao,
) : ViewModel() {

	//讀取中
	private val _isLoading = MutableStateFlow(false)
	val isLoading:StateFlow<Boolean> = _isLoading

	//角色清單(StateFlow≒LiveData)
	private val _charaList = MutableStateFlow(emptyList<Chara>())
	val charaList:StateFlow<List<Chara>> = _charaList

	//==清空==
	fun clearAllChara() = viewModelScope.launch {
		_charaList.value = emptyList()
	}

	//==取得Chara==
	//請求_無包裝
	fun getCharaList() = viewModelScope.launch {
//		_charaList.value = emptyList()
		_isLoading.value = true
		runCatching {
			repository.getCharaList()
		}.onSuccess {
			_charaList.value = it
		}.onFailure {
			//處理錯誤
			Log.i("@@@", it.message ?: "unknown error")
		}
		_isLoading.value = false
	}

	//請求_包request (timeout會拋錯)
	fun getCharaList_request() = viewModelScope.launch {
//		_charaList.value = emptyList()
		_isLoading.value = true
		repository.getCharaList_request().also { response ->
			if (response.isSuccessful) {
				_charaList.value = response.body()!!
			} else {
				Log.i("@@@", response.message())
			}
		}
		_isLoading.value = false
	}

	private var job_getCharaList:Job? = null

	//請求_包Flow (可以解決重複請求問題)
	fun getCharaList_flow_noSave() {
		job_getCharaList?.cancel()
		job_getCharaList = repository.getCharaList_flow_noSave()
			.onStart {
				Log.i("@@@", "start")
				//請求前處理
				_isLoading.value = true
			}
			.onEach {
				Log.i("@@@", "onEach")
				//成功處理
				_charaList.value = it
			}
			.onCompletion {
				Log.i("@@@", "onCompletion")
				//結束處理
				_isLoading.value = false
			}
			.catch {
				//異常處理
				Log.i("@@@", it.message ?: "unknown error")
				//[API位置錯誤] HTTP 404 Not Found
				//[domain未允許非明文傳輸] CLEARTEXT communication to hp-api.herokuapp.com not permitted by network security policy
				//[無網路] Unable to resolve host "hp-api.herokuapp.com": No address associated with hostname
			}
			.launchIn(viewModelScope)
	}

	//請求_包Flow (含離線功能)
	fun getCharaList_flow() {
		job_getCharaList?.cancel()
		job_getCharaList = repository.getCharaList_flow_save()
			.onStart {
				Log.i("@@@", "start")
				//請求前處理
				_isLoading.value = true
			}
			.onEach {
				Log.i("@@@", "onEach")
				//成功處理
				_charaList.value = it
			}
			.onCompletion {
				Log.i("@@@", "onCompletion")
				//結束處理
				_isLoading.value = false
			}
			.catch {
				//==異常處理==
				//_提示連接失敗(ex:跳小吃顯示抓取資料失敗)_
				//[API位置錯誤] HTTP 404 Not Found
				//[domain未允許非明文傳輸] CLEARTEXT communication to hp-api.herokuapp.com not permitted by network security policy
				//[無網路] Unable to resolve host "hp-api.herokuapp.com": No address associated with hostname
				Log.i("@@@", it.message ?: "unknown error")
				//_改從room抓離線資料_
				dao.getCharaList().onEach { charaList ->
					_charaList.value = charaList
				}.launchIn(viewModelScope)
			}
			.launchIn(viewModelScope)
	}

	//請求_包Flow (含離線功能，邏輯寫在repository)
	fun getCharaList_flow_unity() {
		job_getCharaList?.cancel()
		job_getCharaList = repository.getCharaList_flow_unity()
			.onStart {
				_isLoading.value = true
			}
			.onEach { charaList ->
				_charaList.value = charaList
			}
			.onCompletion {
				_isLoading.value = false
			}
			.catch { error ->
				Log.i("@@@", error.message ?: "unknown error")
			}
			.launchIn(viewModelScope)
	}

	//==多來源測試==
	// 有三種抓資料方式，前面失敗 則用下一種方式
	fun getData() {
		flow {
			emit(getData1())
		}
			.catch {
				Log.i("@@@", "getData1 error: " + it.message)
				emit(getData2())
			}
			.catch {
				Log.i("@@@", "getData2 error: " + it.message)
				emit(getData3())
			}
			.onEach {
				/* _state.value = it */
				Log.i("@@@", it.toString())
			}
			.catch {
				Log.i("@@@", it.message ?: "unknown error")
			}
			.launchIn(viewModelScope)
	}

	fun getData1():Int {
		throw Exception("Error1")
		return 1
	}

	fun getData2():Int {
		throw Exception("Error2")
		return 2
	}

	fun getData3():Int {
		return 3
	}

}
