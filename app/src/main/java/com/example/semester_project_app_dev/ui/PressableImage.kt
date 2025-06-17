package com.example.semester_project_app_dev.ui

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment

enum class PressEffect {
    Standard,
    Light
}

@Composable
fun PressableImage(
    @DrawableRes imageRes: Int,
    contentDescription: String?,
    width: Dp? = null,
    height: Dp? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    pressEffect: PressEffect = PressEffect.Standard // default
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Pick alpha/scale based on pressEffect type
    val targetScale = when (pressEffect) {
        PressEffect.Standard -> if (isPressed) 1.1f else 1f
        PressEffect.Light    -> if (isPressed) 1.03f else 1f
    }

    val targetAlpha = when (pressEffect) {
        PressEffect.Standard -> if (isPressed) 0.6f else 1f
        PressEffect.Light    -> if (isPressed) 0.93f else 1f
    }

    val scale by animateFloatAsState(targetValue = targetScale, label = "ImageScale")
    val alpha by animateFloatAsState(targetValue = targetAlpha, label = "ImageAlpha")

    val sizeModifier = when {
        width != null && height != null -> Modifier.size(width, height)
        width != null                   -> Modifier.width(width)
        height != null                  -> Modifier.height(height)
        else                            -> Modifier
    }

    Box(
        modifier = modifier
            .scale(scale)
            .alpha(alpha)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = contentDescription,
            modifier = sizeModifier
        )
    }
}
