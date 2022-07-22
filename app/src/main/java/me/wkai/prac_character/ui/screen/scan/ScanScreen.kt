package me.wkai.prac_character.ui.screen.scan

import android.Manifest
import android.content.pm.PackageManager
import android.util.Size
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController

//掃描頁, 參考: https://github.com/aticiadem/QRCodeScanner
@Composable
fun ScanScreen(navController:NavHostController) {
	var code by remember { mutableStateOf("") }
	var hasReadCode by remember { mutableStateOf(false) }
	val context = LocalContext.current
	val lifecycleOwner = LocalLifecycleOwner.current
	val cameraProviderFeature = remember { ProcessCameraProvider.getInstance(context) }
	var hasCamPermission by remember {
		mutableStateOf(
			ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
		)
	}
	val launcher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.RequestPermission(),
		onResult = { granted -> hasCamPermission = granted }
	)
	LaunchedEffect(key1 = true) {
		launcher.launch(Manifest.permission.CAMERA)
	}

	//ui
	if (!hasCamPermission) {
		Box(modifier = Modifier.fillMaxSize()) {
			Text(
				text = "您必須同意啟用相機權限",
				modifier = Modifier.align(Alignment.Center),
				style = MaterialTheme.typography.h5
			)
		}
		return
	}
	Column(
		modifier = Modifier.fillMaxSize()
	) {
		if (hasReadCode) {
			LoadWebUrl(code)
		} else {
			AndroidView(
				factory = { context ->
					val previewView = PreviewView(context)
					val preview = Preview.Builder().build()
					val selector = CameraSelector.Builder()
						.requireLensFacing(CameraSelector.LENS_FACING_BACK)
						.build()
					preview.setSurfaceProvider(previewView.surfaceProvider)
					val imageAnalysis = ImageAnalysis.Builder()
						.setTargetResolution(Size(previewView.width, previewView.height))
						.setBackpressureStrategy(STRATEGY_KEEP_ONLY_LATEST)
						.build()
					imageAnalysis.setAnalyzer(
						ContextCompat.getMainExecutor(context),
						QRCodeAnalyzer { result ->
							code = result
							hasReadCode = true
						}
					)
					try {
						cameraProviderFeature.get().bindToLifecycle(
							lifecycleOwner,
							selector,
							preview,
							imageAnalysis
						)
					} catch (e:Exception) {
						e.printStackTrace()
					}
					previewView
				},
				modifier = Modifier.fillMaxSize()
			)
		}
	}
}

@Composable
fun LoadWebUrl(url:String) {
	val context = LocalContext.current

	AndroidView(factory = {
		WebView(context).apply {
			webViewClient = WebViewClient()
			loadUrl(url)
		}
	})
}