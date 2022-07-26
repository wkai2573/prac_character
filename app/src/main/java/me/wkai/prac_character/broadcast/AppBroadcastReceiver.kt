package me.wkai.prac_character.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.widget.Toast
import dagger.hilt.android.AndroidEntryPoint

//廣播
@AndroidEntryPoint
class AppBroadcastReceiver : BroadcastReceiver() {

	//當接收廣播
	override fun onReceive(context:Context, intent:Intent) {
		val msg = """
			data:   ${intent.getStringExtra("data")}
			Action: ${intent.action}
			URI:    ${intent.toUri(Intent.URI_INTENT_SCHEME)}
		""".trimIndent()
		Toast.makeText(context, "收到廣播:\n$msg", Toast.LENGTH_LONG).show()
	}

	companion object {
		//初始化，在activity onCreate呼叫
		fun init(context: Context) {
			val br = AppBroadcastReceiver()
			val filter = IntentFilter().apply {
				//可接收的廣播
				addAction("me.wkai.NOTICE_ME_SENPAI")
			}
			context.registerReceiver(br, filter)
		}
	}
}
