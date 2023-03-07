package arg.quran.models.audio

import androidx.annotation.StringRes
import arg.quran.model.R

data class Qari(
  val id: Int,
  @StringRes val nameId: Int,
  val style: RecitationStyle? = null,
  val slug: String,
  val image: String,
  val subfolder: String,
)

enum class RecitationStyle(val label: Int, val description: Int) {
  murattal(R.string.murattal, R.string.murattal_description),
  mujawwad(R.string.mujawwad, R.string.mujawwad_description),
  muallim(R.string.muallim, R.string.muallim_description)
}