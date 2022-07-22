package me.wkai.prac_character.ui.compose.swipe_refresh

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.MutatorMutex
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.*
import kotlinx.coroutines.flow.*

//下滑刷新
// https://github.com/RugerMcCarthy/SmartSwipeRefresh

class SmartSwipeRefreshState {
	private val mutatorMutex = MutatorMutex()
	private val indicatorOffsetAnimatable = Animatable(0.dp, Dp.VectorConverter)
	val indicatorOffset get() = indicatorOffsetAnimatable.value
	private val _indicatorOffsetFlow = MutableStateFlow(0f)
	val indicatorOffsetFlow:Flow<Float> get() = _indicatorOffsetFlow
	val isSwipeInProgress by derivedStateOf { indicatorOffset != 0.dp }

	val _isRefreshing = MutableSharedFlow<Boolean>()
	val isRefreshing = _isRefreshing.asSharedFlow()

	fun updateOffsetDelta(value:Float) {
		_indicatorOffsetFlow.value = value
	}

	suspend fun snapToOffset(value:Dp) {
		mutatorMutex.mutate(MutatePriority.UserInput) {
			indicatorOffsetAnimatable.snapTo(value)
		}
	}

	suspend fun animateToOffset(value:Dp, durationMillis:Int = 1000) {
		mutatorMutex.mutate {
			indicatorOffsetAnimatable.animateTo(value, tween(durationMillis))
		}
	}
}

private class SmartSwipeRefreshNestedScrollConnection(
	val state:SmartSwipeRefreshState,
	val height:Dp
) : NestedScrollConnection {
	override fun onPreScroll(available:Offset, source:NestedScrollSource):Offset {
//		Log.d("gzz", "onPreScroll")
		if (source == NestedScrollSource.Drag && available.y < 0) {
			state.updateOffsetDelta(available.y)
			return if (state.isSwipeInProgress) Offset(x = 0f, y = available.y) else Offset.Zero
		} else {
			return Offset.Zero
		}
	}

	override fun onPostScroll(
		consumed:Offset,
		available:Offset,
		source:NestedScrollSource
	):Offset {
//		Log.d("gzz", "onPostScroll")
		return if (source == NestedScrollSource.Drag && available.y > 0) {
			state.updateOffsetDelta(available.y)
			Offset(x = 0f, y = available.y)
		} else {
			Offset.Zero
		}
	}

	override suspend fun onPreFling(available:Velocity):Velocity {
//		Log.d("gzz", "onPreFling")
		if (state.indicatorOffset > height / 2) {
			//偏上:展開並重整，動畫較快(100ms)
			state.animateToOffset(value = height, durationMillis = 100)
			state._isRefreshing.emit(true)
		} else {
			//偏下:收合恢復(1s)
			state.animateToOffset(value = 0.dp)
		}
		return super.onPreFling(available)
	}

	override suspend fun onPostFling(consumed:Velocity, available:Velocity):Velocity {
//		 Log.d("gzz", "onPostFling")
		return super.onPostFling(consumed, available)
	}
}

@Composable
private fun SubcomposeSmartSwipeRefresh(
	indicator:@Composable () -> Unit,
	content:@Composable (Dp) -> Unit
) {
	SubcomposeLayout { constraints:Constraints ->
		val indicatorPlaceable = subcompose("indicator", indicator).first().measure(constraints)
		val contentPlaceable = subcompose("content") {
			content(indicatorPlaceable.height.toDp())
		}.map {
			it.measure(constraints)
		}.first()
//		 Log.d("gzz","dp: ${indicatorPlaceable.height.toDp()}")
		layout(contentPlaceable.width, contentPlaceable.height) {
			contentPlaceable.placeRelative(0, 0)
		}
	}
}

/**
 * A smart refresh component can customize your slide refresh animation component.
 * @param onRefresh: Refreshing behavior of data when sliding down.
 * @param state: The state contains some refresh state info.
 * @param loadingIndicator: Specify the refresh animation component.
 * @param content: Some slidable components need to be included here.
 */
@Composable
fun SmartSwipeRefresh(
	onRefresh:suspend () -> Unit,
	state:SmartSwipeRefreshState = remember { SmartSwipeRefreshState() },
	loadingIndicator:@Composable () -> Unit = { CircularProgressIndicator() },
	content:@Composable () -> Unit
) {
	SubcomposeSmartSwipeRefresh(indicator = loadingIndicator) { height ->
		val smartSwipeRefreshNestedScrollConnection = remember(state, height) {
			SmartSwipeRefreshNestedScrollConnection(state, height)
		}
		Box(
			Modifier
				.nestedScroll(smartSwipeRefreshNestedScrollConnection),
			contentAlignment = Alignment.TopCenter
		) {
			Box(Modifier.offset(y = -height + state.indicatorOffset)) {
				loadingIndicator()
			}
			Box(Modifier.offset(y = state.indicatorOffset)) {
				content()
			}
		}
		val density = LocalDensity.current
		LaunchedEffect(key1 = Unit) {
			state.indicatorOffsetFlow.collect {
				val currentOffset = with(density) { state.indicatorOffset + it.toDp() }
				state.snapToOffset(currentOffset.coerceAtLeast(0.dp).coerceAtMost(height))
			}
		}
	}

	//發射效果:重整
	LaunchedEffect(key1 = true) {
		state.isRefreshing.collectLatest { isRefreshing ->
			if (isRefreshing) {
				onRefresh()
				state.animateToOffset(0.dp)
			}
		}
	}
}