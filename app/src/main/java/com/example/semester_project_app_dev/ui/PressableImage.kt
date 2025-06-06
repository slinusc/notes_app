package com.example.semester_project_app_dev.ui

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun PressableImage(
    @DrawableRes imageRes: Int,
    contentDescription: String?,
    width: Dp? = null,
    height: Dp? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 1.05f else 1f,
        label = "ImageScale"
    )

    val alpha by animateFloatAsState(
        targetValue = if (isPressed) 0.6f else 1f,
        label = "ImageAlpha"
    )

    val finalModifier = modifier
        .scale(scale)
        .alpha(alpha)
        .then(
            when {
                width != null && height != null -> Modifier.size(width, height)
                width != null                  -> Modifier.width(width)
                height != null                 -> Modifier.height(height)
                else                           -> Modifier
            }
        )
        .clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = onClick
        )

    Image(
        painter = painterResource(imageRes),
        contentDescription = contentDescription,
        modifier = finalModifier
    )
}
