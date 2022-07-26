package me.wkai.prac_character.ui.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.wkai.prac_character.broadcast.AppBroadcastReceiver
import me.wkai.prac_character.ui.compose.Drawer
import me.wkai.prac_character.ui.screen.home.HomeScreen
import me.wkai.prac_character.ui.screen.scan.ScanScreen
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

		//註冊廣播
		AppBroadcastReceiver.init(this)

		setContent {
			prac_characterTheme {
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colors.background
				) {
					val scope = rememberCoroutineScope()
					val scaffoldState = rememberScaffoldState() //鷹架state

					//nav
					val navController = rememberNavController()

					Scaffold(
						scaffoldState = scaffoldState,
						topBar = {
							TopAppBar(
								title = { Text(text = "Character 角色", style = MaterialTheme.typography.h6) },
								backgroundColor = MaterialTheme.colors.primary,
								navigationIcon = {
									IconButton(
										onClick = { scope.launch { scaffoldState.drawerState.open() } },
										content = { Icon(imageVector = Icons.Default.Menu, contentDescription = "Drawer") },
									)
								}
							)
						},
						drawerContent = {
							Drawer(
								drawerState = scaffoldState.drawerState,
								navController = navController,
							)
						},
					) {
						NavHost(navController = navController, startDestination = Screen.HomeScreen.route) {
							//第一頁
							composable(route = Screen.HomeScreen.route) {
								HomeScreen(navController = navController)
							}
							//第二頁 (傳參數範例)
							composable(
								route = Screen.ScanScreen.route + "?fooIndex={fooIndex}&fooColor={fooColor}",
								arguments = listOf(
									navArgument(name = "fooIndex") {
										type = NavType.IntType
										defaultValue = -1
									},
									navArgument(name = "fooColor") {
										type = NavType.IntType
										defaultValue = -1
									}
								)
							) {
								val color = it.arguments?.getInt("fooColor") ?: -1
								ScanScreen(navController = navController)
							}
						}
					}

				}
			}
		}
	}

}