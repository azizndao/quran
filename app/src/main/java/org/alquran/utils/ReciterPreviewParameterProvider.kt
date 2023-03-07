package org.alquran.utils

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import arg.quran.models.audio.Qari
import arg.quran.models.audio.RecitationStyle
import org.quran.audio.R

class QariListPreviewParameterProvider : PreviewParameterProvider<List<Qari>> {
  override val values = sequenceOf(QariPreviewParameterProvider().values.toList())
}

class QariPreviewParameterProvider : PreviewParameterProvider<Qari> {
  override val values: Sequence<Qari> = sequenceOf(
    Qari(
      id = 1,
      slug = "abdul_baset.mujawwad",
      image = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/2.png",
      nameId = R.string.adbul_basit,
      subfolder = "https://download.quranicaudio.com/qdc/abdul_baset/mujawwad",
      style = RecitationStyle.mujawwad
    ),
    Qari(
      id = 2,
      slug = "abdulbasit.murattal",
      image = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/2.png",
      nameId = R.string.adbul_basit,
      subfolder = "https://download.quranicaudio.com/qdc/abdul_baset/murattal",
      style = RecitationStyle.murattal
    ),
    Qari(
      id = 3,
      slug = "sudais.murattal",
      image = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/4.png",
      nameId = R.string.sudais,
      subfolder = "https://download.quranicaudio.com/qdc/abdurrahmaan_as_sudais/murattal",
      style = RecitationStyle.murattal,
    ),
    Qari(
      id = 4,
      slug = "abu_bakr_shatri.murattal",
      image = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/6.png",
      nameId = R.string.shaatri,
      subfolder = "https://download.quranicaudio.com/qdc/abu_bakr_shatri/murattal",
      style = RecitationStyle.murattal,
    ),
    Qari(
      id = 5,
      slug = "hani_ar_rifai.murattal",
      image = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/10.png",
      nameId = R.string.hani_rifai,
      subfolder = "https://download.quranicaudio.com/qdc/hani_ar_rifai/murattal",
      style = RecitationStyle.murattal,
    ),
    Qari(
      id = 6,
      slug = "husary.murattal",
      image = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/11.png",
      nameId = R.string.hasary,
      subfolder = "https://download.quranicaudio.com/qdc/khalil_al_husary/murattal",
      style = RecitationStyle.murattal,
    ),
    Qari(
      id = 7,
      slug = "mishari_al_afasy.murattal",
      image = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/8.png",
      nameId = R.string.alafasy,
      subfolder = "https://download.quranicaudio.com/qdc/mishari_al_afasy/murattal",
      style = RecitationStyle.murattal,
    ),
    Qari(
      id = 8,
      slug = "minshawi.mujawwad",
      image = "",
      nameId = R.string.minshawi,
      subfolder = "https://download.quranicaudio.com/qdc/siddiq_al-minshawi/mujawwad",
      style = RecitationStyle.mujawwad,
    ),
    Qari(
      id = 9,
      image = "",
      slug = "minshawi.murattal",
      nameId = R.string.minshawi,
      subfolder = "https://download.quranicaudio.com/qdc/siddiq_minshawi/murattal",
      style = RecitationStyle.murattal
    ),
    Qari(
      id = 10,
      slug = "saud_ash-shuraym.murattal",
      image = "",
      nameId = R.string.saud_ash_shuraym,
      subfolder = "https://download.quranicaudio.com/qdc/saud_ash-shuraym/murattal",
      style = RecitationStyle.murattal
    ),
    Qari(
      id = 11,
      slug = "saud_ash-shuraym.murattal",
      image = "",
      nameId = R.string.abdul_muhsin_alqasim,
      subfolder = "https://download.quranicaudio.com/quran/abdul_muhsin_alqasim",
    ),
    Qari(
      id = 12,
      slug = "khalil_al_husary.muallim",
      image = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/11.png",
      nameId = R.string.hasary,
      subfolder = "https://download.quranicaudio.com/qdc/khalil_al_husary/muallim",
      style = RecitationStyle.muallim,
    ),
  )
}
