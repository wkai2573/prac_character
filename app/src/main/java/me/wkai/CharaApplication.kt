package me.wkai

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

//@HiltAndroidApp: 有使用Hilt的Application需要加 (註:繼承Application的class需在manifest.xml聲明位置)
@HiltAndroidApp
class CharaApplication : Application() {
	//可放初始化東西...
}