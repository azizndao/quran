package org.quran.ui.utils

import androidx.annotation.FloatRange
import kotlin.math.roundToInt

/**
 * Linearly interpolate between two values
 */
fun lerp(
  startValue: Float,
  endValue: Float,
  @FloatRange(from = 0.0, fromInclusive = true, to = 1.0, toInclusive = true) fraction: Float
): Float {
  return startValue + fraction * (endValue - startValue)
}

/**
 * Linearly interpolate between two values
 */
fun lerp(
  startValue: Int,
  endValue: Int,
  @FloatRange(from = 0.0, fromInclusive = true, to = 1.0, toInclusive = true) fraction: Float
): Int {
  return (startValue + fraction * (endValue - startValue)).roundToInt()
}

/**
 * Linearly interpolate between two values when the fraction is in a given range.
 */
fun lerp(
  startValue: Float,
  endValue: Float,
  @FloatRange(
    from = 0.0,
    fromInclusive = true,
    to = 1.0,
    toInclusive = true
  ) startFraction: Float,
  @FloatRange(from = 0.0, fromInclusive = true, to = 1.0, toInclusive = true) endFraction: Float,
  @FloatRange(from = 0.0, fromInclusive = true, to = 1.0, toInclusive = true) fraction: Float
): Float {
  if (fraction < startFraction) return startValue
  if (fraction > endFraction) return endValue

  return lerp(startValue, endValue, (fraction - startFraction) / (endFraction - startFraction))
}

/**
 * Linearly interpolate between two values when the fraction is in a given range.
 */
fun lerp(
  startValue: Int,
  endValue: Int,
  @FloatRange(
    from = 0.0,
    fromInclusive = true,
    to = 1.0,
    toInclusive = true
  ) startFraction: Float,
  @FloatRange(from = 0.0, fromInclusive = true, to = 1.0, toInclusive = true) endFraction: Float,
  @FloatRange(from = 0.0, fromInclusive = true, to = 1.0, toInclusive = true) fraction: Float
): Int {
  if (fraction < startFraction) return startValue
  if (fraction > endFraction) return endValue

  return lerp(startValue, endValue, (fraction - startFraction) / (endFraction - startFraction))
}

