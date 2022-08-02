package me.wkai.prac_character.ui.screen.home

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.*
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.flowlayout.FlowRow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.wkai.prac_character.util.CharaProvider

@Composable
fun HomeScreen(navController:NavHostController) {

	val context = LocalContext.current
	val scope = rememberCoroutineScope()

	FlowRow(
		modifier = Modifier.padding(8.dp),
		crossAxisSpacing = 0.dp,
		mainAxisSpacing = 8.dp
	) {
		Button(
			content = { Text(text = "綁定其他App服務") },
			onClick = {
				bindService(context)
			})

		Button(
			content = { Text(text = "呼叫服務") },
			onClick = {
				sendMessage()
			})

		Button(
			content = { Text(text = "本地ContentProvider取資料") },
			onClick = {
				scope.launch(Dispatchers.IO) {
					val cursor = context.contentResolver.query(
						CharaProvider.uri,
						null,null,null,null
					)
					if (cursor == null) {
						Log.i("@@@", "QQ")
						return@launch
					}
					val list = mutableListOf<String>()
					while (cursor.moveToNext()) {
						list.add(cursor.getString(0))
					}
					cursor.close()

					Log.i("@@@", list.toString())
				}
			})
	}
}

var messengerServer:Messenger? = null

//綁定處理: 當綁定服務時
val serviceConnection by lazy {
	object : ServiceConnection {
		override fun onServiceConnected(name:ComponentName?, service:IBinder?) {
			messengerServer = Messenger(service)
		}

		override fun onServiceDisconnected(name:ComponentName?) {
			messengerServer = null
		}
	}
}

//服務處理(客戶端): 當訊息傳至客戶端時
val messengerClient by lazy {
	val looper:Looper = Looper.getMainLooper()
	Messenger(object : Handler(looper) {
		override fun handleMessage(msgFromServer:Message) {
			Log.i("@@@", "服務_客戶端_handle: ${msgFromServer.data.getString("param")}")
			super.handleMessage(msgFromServer)
		}
	})
}

//綁定服務
private fun bindService(context:Context) {
	val intent = Intent().apply {
		component = ComponentName(
			"me.wkai.prac_android_compose",
			"me.wkai.prac_android_compose.util.AppService"
		)
		putExtra("param", "客戶端bind參數")
	}
	context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
}

//呼叫服務
private fun sendMessage() {
	messengerServer?.apply {
		val msgToServer = Message.obtain(null, 1)
		msgToServer.replyTo = messengerClient //設定回復對象(如果服務需要回復的話才需要)
		val bundle = Bundle()
		bundle.putString("param", "客戶端msg參數")
		msgToServer.data = bundle
		send(msgToServer)
	}
}