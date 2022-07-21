package me.wkai.prac_character.ui.compose.multifab

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key.Companion.F
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun MultiFloatingActionButton(
	fabIcon:ImageVector,
	items:List<MultiFabItem>,
	multiFabState:MultiFabState,
	stateChanged:(fabState:MultiFabState) -> Unit,
	showLabels:Boolean = true,
	style:TextStyle = LocalTextStyle.current
) {
	val transition:Transition<MultiFabState> = updateTransition(targetState = multiFabState, label = "transition")
	val scale:Float by transition.animateFloat(
		label = "scale",
		targetValueByState = { state ->
			if (state == MultiFabState.EXPANDED) 40f else 0f
		}
	)
	val alpha:Float by transition.animateFloat(
		label = "alpha",
		transitionSpec = {
			tween(durationMillis = 50)
		},
		targetValueByState = { state ->
			if (state == MultiFabState.EXPANDED) 1f else 0f
		},
	)
	val rotation:Float by transition.animateFloat(
		label = "rotation",
		targetValueByState = { state ->
			if (state == MultiFabState.EXPANDED) 45f else 0f
		}
	)

	Column(
		horizontalAlignment = Alignment.End
	) {
		if (
			transition.currentState == MultiFabState.EXPANDED ||
			transition.isRunning
		) {
			items.forEach { item ->
				MiniFabItem(
					item = item,
					alpha = alpha,
					scale = scale,
					showLabel = showLabels,
					style = style
				)
				Spacer(modifier = Modifier.height(12.dp))
			}
		}
		FloatingActionButton(onClick = {
			stateChanged(
				if (transition.currentState == MultiFabState.EXPANDED) {
					MultiFabState.COLLAPSED
				} else
					MultiFabState.EXPANDED
			)
		}) {
			Icon(
				imageVector = fabIcon,
				contentDescription = null,
				modifier = Modifier.rotate(rotation)
			)
		}
	}
}

@Composable
private fun MiniFabItem(
	item:MultiFabItem,
	alpha:Float,
	scale:Float,
	showLabel:Boolean,
	style:TextStyle = LocalTextStyle.current
) {
	Row(
		modifier = Modifier
			.alpha(animateFloatAsState(alpha).value)
			.height(FabItemHeight)
			.padding(end = 6.dp),
		verticalAlignment = Alignment.CenterVertically,
	) {
		if (showLabel) {
			Surface(
				shape = RoundedCornerShape(3.dp),
				color = MaterialTheme.colors.background,
				elevation = 3.dp
			) {
				Text(
					text = item.label,
					style = style,
					modifier = Modifier.padding(start = 6.dp, end = 6.dp, top = 4.dp, bottom = 4.dp)
				)
			}
			Spacer(modifier = Modifier.width(16.dp))
		}
		Surface(
			modifier = Modifier.size(scale.dp),
			shape = CircleShape,
			color = MaterialTheme.colors.secondary,
			elevation = 3.dp
		) {
			Box(
				modifier = Modifier
					.clickable(onClick = item.onClick)
					.size(8.dp)
			) {
				Icon(
					modifier = Modifier.align(Alignment.Center),
					imageVector = item.icon,
					contentDescription = null,
				)
			}
		}
	}
}

private val FabItemHeight = 50f.dp
