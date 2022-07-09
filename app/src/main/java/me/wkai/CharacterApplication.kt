package me.wkai

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

//@HiltAndroidApp: 有使用Hilt的Application需要加 (註:繼承Application的class需在manifest.xml聲明位置)
@HiltAndroidApp
class CharacterApplication : Application() {
	//可放初始化東西...
}