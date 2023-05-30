package org.alquran.utils

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import arg.quran.models.audio.Qari

class QariListPreviewParameterProvider : PreviewParameterProvider<List<Qari>> {
  override val values = sequenceOf(QariPreviewParameterProvider().values.toList())
}

class QariPreviewParameterProvider : PreviewParameterProvider<Qari> {
  override val values: Sequence<Qari> = sequenceOf(
    Qari(
      id = 0,
      nameResource = org.quran.core.audio.R.string.qari_minshawi_murattal_gapless,
      url = "https://download.quranicaudio.com/quran/muhammad_siddeeq_al-minshaawee",
      slug = "minshawi_murattal",
    ),
    Qari(
      id = 1,
      nameResource = org.quran.core.audio.R.string.qari_husary_gapless,
      url = "https://download.quranicaudio.com/quran/mahmood_khaleel_al-husaree",
      slug = "husary",
    ),
    Qari(
      id = 3,
      slug = "abdul_basit_mujawwad",
      imageUrl = "https://raw.githubusercontent.com/azizndao/quran_data/dev/images/2.png",
      nameResource = org.quran.core.audio.R.string.qari_abdulbaset_mujawwad_gapless,
      url = "https://download.quranicaudio.com/quran/abdulbaset_mujawwad",
    ),
    Qari(
      id = 4,
      slug = "abdul_basit_murattal",
      imageUrl = "https://raw.githubusercontent.com/azizndao/quran_data/dev/images/2.png",
      nameResource = org.quran.core.audio.R.string.qari_abdulbaset_gapless,
      url = "https://download.quranicaudio.com/quran/abdul_basit_murattal",
    ),
    Qari(
      id = 5,
      slug = "mishari_alafasy",
      imageUrl = "https://raw.githubusercontent.com/azizndao/quran_data/dev/images/8.png",
      nameResource = org.quran.core.audio.R.string.qari_afasy_gapless,
      url = "https://download.quranicaudio.com/quran/mishaari_raashid_al_3afaasee",
    ),
    Qari(
      id = 6,
      slug = "mishari_alafasy_cali",
      imageUrl = "https://raw.githubusercontent.com/azizndao/quran_data/dev/images/8.png",
      nameResource = org.quran.core.audio.R.string.qari_afasy_cali_gapless,
      url = "https://download.quranicaudio.com/quran/mishaari_california",
    ),
  )
}
