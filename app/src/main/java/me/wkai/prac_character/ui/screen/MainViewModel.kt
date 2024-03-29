package me.wkai.prac_character.ui.screen

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UpTipState(val isShow:Boolean = false, val text:String = "", val bgc:Color = Color.Gray)

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

	// ==參數==
	//網路連線
	private val _isNetworkAvailable = MutableStateFlow(true)
	val isNetworkAvailable:StateFlow<Boolean> = _isNetworkAvailable
	//上方提示
	private val _upTipState = MutableStateFlow(UpTipState())
	val upTipState:StateFlow<UpTipState> = _upTipState

	// ==ui事件流==
	private val _eventFlow = MutableSharedFlow<UiEvent>()
	val eventFlow = _eventFlow.asSharedFlow()

	// ==定義ui事件==
	sealed class UiEvent {
		data class ShowSnackbar(val message:String) : UiEvent()
	}

	// ==方法==
	// 上方提示:斷線
	private fun networkLost() {
		networkReconnectJob?.cancel()
		_upTipState.value = _upTipState.value.copy(isShow = true, text = "網路斷線啦", bgc = Color(0xFFE91E63))
	}

	// 上方提示:網路重新連線
	private var networkReconnectJob:Job? = null
	private fun networkReconnectFlow() : Flow<Boolean> = flow {
		_upTipState.value = _upTipState.value.copy(isShow = true, text = "網路已連線", bgc = Color(0xFF4CAF50))
		delay(5000)
		_upTipState.value = _upTipState.value.copy(isShow = false)
		emit(true)
	}
	private fun networkReconnect() {
		networkReconnectJob = networkReconnectFlow().launchIn(viewModelScope)
	}

	// 註冊偵測網路改變
	fun initDetectionNetwork(context:Context) {
		//初始目前網路連線, 沒連線則執行未連線fun
		_isNetworkAvailable.value = isNetworkConnected(context)
		if (!_isNetworkAvailable.value) networkLost()
		//偵測網路改變
		val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
		connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
			override fun onAvailable(network:Network) {
				viewModelScope.launch {
					if (!_isNetworkAvailable.value) {
						_isNetworkAvailable.value = true
						networkReconnect()
					}
				}
			}
			override fun onLost(network:Network) {
				viewModelScope.launch {
					_isNetworkAvailable.value = false
					networkLost()
				}
			}
		})
	}

	// 是否連接網路
	private fun isNetworkConnected(context: Context): Boolean {
		// register activity with the connectivity manager service
		val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
		// Returns a Network object corresponding to the currently active default data network.
		val network = connectivityManager.activeNetwork ?: return false
		// Representation of the capabilities of an active network.
		val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
		return when {
			// Indicates this network uses a Wi-Fi transport, or WiFi has network connectivity
			activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
			// Indicates this network uses a Cellular transport. or Cellular has network connectivity
			activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
			else -> false
		}
	}

}