package org.quran.translation.local.model


enum class TextDirection(val value: String) {
  LTR("ltr"),
  RTL("rtl");

  override fun toString(): String = value

  companion object {

    fun parse(value: String): TextDirection {
      return when (value) {
        LTR.value -> LTR
        else -> RTL
      }
    }
  }
}
