package me.wkai.prac_character.ui.compose

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
	val shadow:Dp by transition.animateDp(
		label = "shadow",
		transitionSpec = {
			tween(durationMillis = 50)
		},
		targetValueByState = { state ->
			if (state == MultiFabState.EXPANDED) 2.dp else 0.dp
		}
	)
	val rotation:Float by transition.animateFloat(
		label = "rotation",
		targetValueByState = { state ->
			if (state == MultiFabState.EXPANDED) 45f else 0f
		}
	)

	Column(horizontalAlignment = Alignment.End) {
		if (
			transition.currentState == MultiFabState.EXPANDED ||
			transition.isRunning
		) {
			items.forEach { item ->
				MiniFabItem(
					item = item,
					alpha = alpha,
					shadow = shadow,
					scale = scale,
					showLabel = showLabels,
					style = style
				)
				Spacer(modifier = Modifier.height(15.dp))
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
	shadow:Dp,
	scale:Float,
	showLabel:Boolean,
	style:TextStyle = LocalTextStyle.current
) {
	val fabColor = MaterialTheme.colors.secondary
	val maxHeight = 50f

	Row(
		modifier = Modifier.height(maxHeight.dp).padding(end = 6.dp),
		verticalAlignment = Alignment.CenterVertically,
	) {
		if (showLabel) {
			Text(
				text = item.label,
				style = style,
				modifier = Modifier.alpha(animateFloatAsState(alpha).value)
					.shadow(animateDpAsState(shadow).value)
					.background(color = MaterialTheme.colors.surface)
					.padding(start = 6.dp, end = 6.dp, top = 4.dp, bottom = 4.dp)
			)
			Spacer(modifier = Modifier.width(16.dp))
		}
		Surface(
			modifier = Modifier.size(scale.dp),
			shape = CircleShape,
			color = fabColor,
		) {
			Image(
				modifier = Modifier.clickable(onClick = item.onClick),
				imageVector = item.icon,
				contentDescription = null,
				contentScale = ContentScale.None,
			)
		}
	}
}