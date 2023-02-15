package org.alquran.utils.extensions

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout

enum class CustomDialogPosition {
    BOTTOM, TOP
}

fun Modifier.customDialogModifier(pos: CustomDialogPosition) = layout { measurable, constraints ->

    val placeable = measurable.measure(constraints)
    layout(constraints.maxWidth, constraints.maxHeight) {
        when (pos) {
            CustomDialogPosition.BOTTOM -> placeable.place(0, constraints.maxHeight - placeable.height, 10f)
            CustomDialogPosition.TOP -> placeable.place(0, 0, 10f)
        }
    }
}