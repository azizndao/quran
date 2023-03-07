package arg.quran.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
enum class Direction(val value: String) {
  @SerialName("ltr")
  LTR("ltr"),
  @SerialName("rtl")
  RTL("rtl");
}
