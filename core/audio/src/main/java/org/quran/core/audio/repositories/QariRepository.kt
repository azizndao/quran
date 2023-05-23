package org.muslimapp.core.audio.repositories

import android.content.Context
import arg.quran.models.audio.Qari
import org.quran.core.audio.models.QariItem
import org.quran.core.audio.R

class QariRepository internal constructor(
  private val context: Context
) {

  fun getQariItem(slug: String): QariItem {
    return getQari(slug).let { item ->
      QariItem(
        id = item.id,
        name = context.getString(item.nameResource),
        url = item.url,
        path = item.path,
        db = item.db
      )
    }
  }

  fun getQari(slug: String): Qari {
    return getQariList().find { it.path == slug }!!
  }

  /**
   * Get a list of all available qaris as [QariItem]s
   *
   * @param context the current context
   * @return a list of [QariItem] representing the qaris to show.
   */
  fun getQariItemList(): List<QariItem> {
    return getQariList().map { item ->
      QariItem(
        id = item.id,
        name = context.getString(item.nameResource),
        url = item.url,
        path = item.path,
        db = item.db
      )
    }
  }

  /**
   * Get a list of all available qaris as [Qari].
   *
   * Unlike the method with the context parameter, this version does not have
   * the actual qari  nameResource = . It only has the resource id for the qari.
   */
  fun getQariList(): List<Qari> {
    return listOf(
      Qari(
        id = 0,
        nameResource = R.string.qari_minshawi_murattal_gapless,
        url = "https://download.quranicaudio.com/quran/muhammad_siddeeq_al-minshaawee",
        path = "minshawi_murattal",
      ),
      Qari(
        id = 1,
        nameResource = R.string.qari_husary_gapless,
        url = "https://download.quranicaudio.com/quran/mahmood_khaleel_al-husaree",
        path = "husary",
      ),
      Qari(
        id = 3,
        path = "abdul_basit_mujawwad",
        imageUrl = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/2.png",
        nameResource = R.string.qari_abdulbaset_mujawwad_gapless,
        url = "https://download.quranicaudio.com/quran/abdulbaset_mujawwad",
      ),
      Qari(
        id = 4,
        path = "abdul_basit_murattal",
        imageUrl = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/2.png",
        nameResource = R.string.qari_abdulbaset_gapless,
        url = "https://download.quranicaudio.com/quran/abdul_basit_murattal",
      ),
      Qari(
        id = 5,
        path = "mishari_alafasy",
        imageUrl = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/8.png",
        nameResource = R.string.qari_afasy_gapless,
        url = "https://download.quranicaudio.com/quran/mishaari_raashid_al_3afaasee",
      ),
      Qari(
        id = 6,
        path = "mishari_alafasy_cali",
        imageUrl = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/8.png",
        nameResource = R.string.qari_afasy_cali_gapless,
        url = "https://download.quranicaudio.com/quran/mishaari_california",
      ),
      Qari(
        id = 7,
        path = "mishari_walk",
        imageUrl = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/8.png",
        nameResource = R.string.qari_mishari_walk_gapless,
        url = "https://download.quranicaudio.com/quran/mishaari_w_ibrahim_walk_si",
      ),
      Qari(
        id = 8,
        path = "abdullah_matroud",
        imageUrl = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/33.png",
        nameResource = R.string.qari_abdullah_matroud_gapless,
        url = "https://download.quranicaudio.com/quran/abdullah_matroud/reencode",
      ),
      Qari(
        id = 9,
        path = "sa3d_alghamidi",
        imageUrl = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/9.png",
        nameResource = R.string.qari_saad_al_ghamidi_gapless,
        url = "https://download.quranicaudio.com/quran/sa3d_al-ghaamidi/complete",
      ),
      Qari(
        id = 10,
        path = "sudais_murattal",
        imageUrl = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/4.png",
        nameResource = R.string.qari_sudais_gapless,
        url = "https://download.quranicaudio.com/quran/abdurrahmaan_as-sudays",
      ),
      Qari(
        id = 11,
        path = "shatri",
        imageUrl = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/6.png",
        nameResource = R.string.qari_shatri_gapless,
        url = "https://download.quranicaudio.com/quran/abu_bakr_ash-shaatree",
      ),
      Qari(
        id = 12,
        path = "abdullah_basfar",
        imageUrl = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/3.png",
        nameResource = R.string.qari_basfar_gapless,
        url = "https://download.quranicaudio.com/quran/abdullaah_basfar",
      ),
      Qari(
        id = 12,
        path = "ahmed_al3ajamy",
        imageUrl = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/7.png",
        nameResource = R.string.qari_ajamy_gapless,
        url = "https://download.quranicaudio.com/quran/ahmed_ibn_3ali_al-3ajamy",
      ),
      Qari(
        id = 14,
        path = "hani_rifai",
        imageUrl = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/10.png",
        nameResource = R.string.qari_hani_rifai_gapless,
        url = "https://download.quranicaudio.com/quran/rifai",
      ),
      Qari(
        id = 15,
        path = "husary",
        imageUrl = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/11.png",
        nameResource = R.string.qari_husary_gapless,
        url = "https://download.quranicaudio.com/quran/mahmood_khaleel_al-husaree",
      ),
      Qari(
        id = 16,
        path = "ali_hudhayfi",
        imageUrl = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/13.png",
        nameResource = R.string.qari_hudhayfi_gapless,
        url = "https://download.quranicaudio.com/quran/huthayfi",
      ),
      Qari(
        id = 17,
        path = "ibrahim_alakhdar",
        imageUrl = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/14.png",
        nameResource = R.string.qari_ibrahim_alakhdar_gapless,
        url = "https://download.quranicaudio.com/quran/ibrahim_al_akhdar",
      ),
      Qari(
        id = 18,
        path = "muaiqly_kfgqpc",
        imageUrl = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/15.png",
        nameResource = R.string.qari_muaiqly_gapless,
        url = "https://download.quranicaudio.com/quran/maher_almu3aiqly/year1440",
      ),
      Qari(
        id = 19,
        path = "mohammad_altablawi",
        imageUrl = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/19.png",
        nameResource = R.string.qari_mohammad_altablawi_gapless,
        url = "https://download.quranicaudio.com/quran/mohammad_altablawi",
      ),
      Qari(
        id = 20,
        path = "muhammad_ayyoub",
        imageUrl = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/20.png",
        nameResource = R.string.qari_ayyoub_gapless,
        url = "https://download.quranicaudio.com/quran/muhammad_ayyoob",
      ),
      Qari(
        id = 21,
        path = "mjibreel",
        imageUrl = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/21.png",
        nameResource = R.string.qari_jibreel_gapless,
        url = "https://download.quranicaudio.com/quran/muhammad_jibreel/complete",
      ),
      Qari(
        id = 22,
        path = "salah_bukhatir",
        imageUrl = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/29.png",
        nameResource = R.string.qari_salah_bukhatir_gapless,
        url = "https://download.quranicaudio.com/quran/salaah_bukhaatir",
      ),
      Qari(
        id = 23,
        path = "abdul_muhsin_alqasim",
        imageUrl = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/30.png",
        nameResource = R.string.qari_abdulmuhsin_qasim_gapless,
        url = "https://download.quranicaudio.com/quran/abdul_muhsin_alqasim",
      ),
      Qari(
        id = 24,
        path = "abdullah_juhany",
        imageUrl = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/31.png",
        nameResource = R.string.qari_juhany_gapless,
        url = "https://download.quranicaudio.com/quran/abdullaah_3awwaad_al-juhaynee",
      ),
      Qari(
        id = 25,
        path = "salah_budair",
        imageUrl = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/32.png",
        nameResource = R.string.qari_salah_budair_gapless,
        url = "https://download.quranicaudio.com/quran/salahbudair",
      ),
      Qari(
        id = 26,
        path = "ahmad_nauina",
        imageUrl = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/34.png",
        nameResource = R.string.qari_ahmad_nauina_gapless,
        url = "https://download.quranicaudio.com/quran/ahmad_nauina",
      ),
      Qari(
        id = 28,
        path = "khalifa_taniji",
        imageUrl = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/36.png",
        nameResource = R.string.qari_khalifa_taniji_gapless,
        url = "https://download.quranicaudio.com/quran/khalifah_taniji",
      ),
      Qari(
        id = 29,
        path = "mahmoud_ali_albana",
        imageUrl = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/37.png",
        nameResource = R.string.qari_mahmoud_ali_albana_gapless,
        url = "https://download.quranicaudio.com/quran/mahmood_ali_albana",
      ),
      Qari(
        id = 30,
        path = "husary_muallim",
        imageUrl = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/41.png",
        nameResource = R.string.qari_husary_mujawwad_gapless,
        url = "https://download.quranicaudio.com/quran/husary_muallim",
      ),
      Qari(
        id = 31,
        path = "khalid_alqahtani",
        imageUrl = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/42.png",
        nameResource = R.string.qari_khalid_qahtani_gapless,
        url = "https://download.quranicaudio.com/quran/khaalid_al-qahtaanee",
      ),
      Qari(
        id = 32,
        path = "yasser_dussary",
        imageUrl = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/43.png",
        nameResource = R.string.qari_yasser_dussary_gapless,
        url = "https://download.quranicaudio.com/quran/yasser_ad-dussary",
      ),
      Qari(
        id = 33,
        path = "qatami",
        imageUrl = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/44.png",
        nameResource = R.string.qari_qatami_gapless,
        url = "https://download.quranicaudio.com/quran/nasser_bin_ali_alqatami",
      ),
      Qari(
        id = 34,
        path = "ali_hajjaj_alsouasi",
        imageUrl = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/45.png",
        nameResource = R.string.qari_ali_hajjaj_alsouasi_gapless,
        url = "https://download.quranicaudio.com/quran/ali_hajjaj_alsouasi",
      ),
      Qari(
        id = 35,
        path = "yasser_dussary",
        imageUrl = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/46.png",
        nameResource = R.string.qari_yasser_dussary_gapless,
        url = "https://download.quranicaudio.com/quran/sahl_yaaseen",
      ),
      Qari(
        id = 36,
        path = "aziz_alili",
        imageUrl = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/49.png",
        nameResource = R.string.qari_aziz_alili_gapless,
        url = "https://download.quranicaudio.com/quran/aziz_alili",
      ),
      Qari(
        id = 37,
        path = "akram_al_alaqmi",
        imageUrl = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/51.png",
        nameResource = R.string.qari_akram_al_alaqmi,
        url = "https://download.quranicaudio.com/quran/akram_al_alaqmi",
      ),
      Qari(
        id = 38,
        path = "fares_abbad",
        imageUrl = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/52.png",
        nameResource = R.string.qari_fares_abbad_gapless,
        url = "https://download.quranicaudio.com/quran/fares",
      ),
      Qari(
        id = 39,
        path = "ibrahim_walk",
        imageUrl = "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/images/reciters/23.png",
        nameResource = R.string.qari_walk_gapless,
        url = "https://download.quranicaudio.com/quran/ibrahim_walk",
      ),
    )
  }
}
