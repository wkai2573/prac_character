package me.wkai.prac_character.ui.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import me.wkai.prac_character.ui.screen.home.HomeScreen
import me.wkai.prac_character.ui.theme.prac_characterTheme

/**
 * 練習_使用技術
 * Jetpack Compose(Kotlin)
 * MVVM
 * Flow_異步觀察
 * Retrofit_請求
 * Hilt_注入
 *
 * 參考
 * YT:     https://www.youtube.com/watch?v=-mirqViyITY&list=PL08wuslxRZ5k7HPXQD7-Z4PXaMZrIoIg9&index=1
 * github: https://github.com/Hoodlab/retrofit-mvvm-
 * 練習API: https://hp-api.herokuapp.com/
 */

//@AndroidEntryPoint: 有使用Hilt的Activity、Fragment、View、Service、BroadcastReceiver需要加
//@HiltAndroidApp: 有使用Hilt的Application需要加 (註:繼承Application的class需在manifest.xml聲明位置)
//@HiltViewModel: 有使用Hilt的ViewModel需要加
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

	override fun onCreate(savedInstanceState:Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			prac_characterTheme {
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colors.background
				) {
					HomeScreen()
				}
			}
		}
	}

}