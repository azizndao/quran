package org.quran.ui.theme

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Shapes

val shapes = Shapes(
//    large = RoundedCornerShape(24.dp),
//    medium = RoundedCornerShape(16.dp)
)

val Shapes.bottomSheet
  get() = extraLarge.copy(
    bottomEnd = CornerSize(0),
    bottomStart = CornerSize(0)
  )