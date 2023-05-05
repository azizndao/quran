package org.alquran.utils

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import arg.quran.models.audio.Qari

class QariListPreviewParameterProvider : PreviewParameterProvider<List<Qari>> {
  override val values = sequenceOf(QariPreviewParameterProvider().values.toList())
}

class QariPreviewParameterProvider : PreviewParameterProvider<Qari> {
  override val values: Sequence<Qari> = sequenceOf(/*
    Qari(
      id = 3,
      path = "abdul_basit_mujawwad",
      image = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/2.png",
      nameResource = R.string.qari_abdulbaset_mujawwad_gapless,
      url = "https://download.quranicaudio.com/quran/abdulbaset_mujawwad",
    ),
    Qari(
      id = 4,
      path = "abdul_basit_murattal",
      image = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/2.png",
      nameResource = R.string.qari_abdulbaset_gapless,
      url = "https://download.quranicaudio.com/quran/abdul_basit_murattal",
    ),
    Qari(
      id = 5,
      path = "mishari_alafasy",
      image = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/8.png",
      nameResource = R.string.qari_afasy_gapless,
      url = "https://download.quranicaudio.com/quran/mishaari_raashid_al_3afaasee",
    ),
    Qari(
      id = 6,
      path = "mishari_alafasy_cali",
      image = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/8.png",
      nameResource = R.string.qari_afasy_cali_gapless,
      url = "https://download.quranicaudio.com/quran/mishaari_california",
    ),
    Qari(
      id = 7,
      path = "mishari_walk",
      image = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/8.png",
      nameResource = R.string.qari_mishari_walk_gapless,
      url = "https://download.quranicaudio.com/quran/mishaari_w_ibrahim_walk_si",
    ),
    Qari(
      id = 8,
      path = "abdullah_matroud",
      image = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/33.png",
      nameResource = R.string.qari_abdullah_matroud_gapless,
      url = "https://download.quranicaudio.com/quran/abdullah_matroud/reencode",
    ),
    Qari(
      id = 9,
      path = "sa3d_alghamidi",
      image = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/9.png",
      nameResource = R.string.qari_saad_al_ghamidi_gapless,
      url = "https://download.quranicaudio.com/quran/sa3d_al-ghaamidi/complete",
    ),
  */
  )
}
