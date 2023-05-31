package org.quran.core.audio.repositories

import android.content.Context
import arg.quran.models.audio.Qari
import org.quran.core.audio.R
import org.quran.core.audio.models.QariItem

class QariRepository internal constructor(
  private val context: Context
) {

  fun getQariItem(slug: String): QariItem {
    return getQari(slug).let { item ->
      QariItem(
        id = item.id,
        name = context.getString(item.nameResource),
        url = item.url,
        path = item.slug,
        db = item.db,
        imageUri = item.imageUrl,
      )
    }
  }

  fun getQari(slug: String): Qari {
    return getQariList().find { it.slug == slug }!!
  }

  /**
   * Get a list of all available qaris as [QariItem]s
   *
   * @return a list of [QariItem] representing the qaris to show.
   */
  fun getQariItemList(): List<QariItem> {
    return getQariList().map { item ->
      QariItem(
        id = item.id,
        name = context.getString(item.nameResource),
        url = item.url,
        path = item.slug,
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
        slug = "minshawi_murattal",
      ),
      Qari(
        id = 1,
        nameResource = R.string.qari_husary_gapless,
        url = "https://download.quranicaudio.com/quran/mahmood_khaleel_al-husaree",
        slug = "husary",
      ),
      Qari(
        id = 3,
        slug = "abdul_basit_mujawwad",
        imageUrl = "https://raw.githubusercontent.com/azizndao/quran_data/dev/images/2.png",
        nameResource = R.string.qari_abdulbaset_mujawwad_gapless,
        url = "https://download.quranicaudio.com/quran/abdulbaset_mujawwad",
      ),
//      Qari(
//        id = 4,
//        slug = "abdul_basit_murattal",
//        imageUrl = "https://raw.githubusercontent.com/azizndao/quran_data/dev/images/2.png",
//        nameResource = R.string.qari_abdulbaset_gapless,
//        url = "https://download.quranicaudio.com/quran/abdul_basit_murattal",
//      ),
      Qari(
        id = 5,
        slug = "mishari_alafasy",
        imageUrl = "https://raw.githubusercontent.com/azizndao/quran_data/dev/images/8.png",
        nameResource = R.string.qari_afasy_gapless,
        url = "https://download.quranicaudio.com/quran/mishaari_raashid_al_3afaasee",
      ),
//      Qari(
//        id = 6,
//        slug = "mishari_alafasy_cali",
//        imageUrl = "https://raw.githubusercontent.com/azizndao/quran_data/dev/images/8.png",
//        nameResource = R.string.qari_afasy_cali_gapless,
//        url = "https://download.quranicaudio.com/quran/mishaari_california",
//      ),
      Qari(
        id = 7,
        slug = "mishari_walk",
        imageUrl = "https://raw.githubusercontent.com/azizndao/quran_data/dev/images/8.png",
        nameResource = R.string.qari_mishari_walk_gapless,
        url = "https://download.quranicaudio.com/quran/mishaari_w_ibrahim_walk_si",
      ),
      Qari(
        id = 8,
        slug = "abdullah_matroud",
        imageUrl = "https://raw.githubusercontent.com/azizndao/quran_data/dev/images/33.png",
        nameResource = R.string.qari_abdullah_matroud_gapless,
        url = "https://download.quranicaudio.com/quran/abdullah_matroud/reencode",
      ),
      Qari(
        id = 9,
        slug = "sa3d_alghamidi",
        imageUrl = "https://raw.githubusercontent.com/azizndao/quran_data/dev/images/9.png",
        nameResource = R.string.qari_saad_al_ghamidi_gapless,
        url = "https://download.quranicaudio.com/quran/sa3d_al-ghaamidi/complete",
      ),
      Qari(
        id = 10,
        slug = "sudais_murattal",
        imageUrl = "https://raw.githubusercontent.com/azizndao/quran_data/dev/images/4.png",
        nameResource = R.string.qari_sudais_gapless,
        url = "https://download.quranicaudio.com/quran/abdurrahmaan_as-sudays",
      ),
      Qari(
        id = 11,
        slug = "shatri",
        imageUrl = "https://raw.githubusercontent.com/azizndao/quran_data/dev/images/6.png",
        nameResource = R.string.qari_shatri_gapless,
        url = "https://download.quranicaudio.com/quran/abu_bakr_ash-shaatree",
      ),
      Qari(
        id = 12,
        slug = "abdullah_basfar",
        imageUrl = "https://raw.githubusercontent.com/azizndao/quran_data/dev/images/3.png",
        nameResource = R.string.qari_basfar_gapless,
        url = "https://download.quranicaudio.com/quran/abdullaah_basfar",
      ),
      Qari(
        id = 12,
        slug = "ahmed_al3ajamy",
        imageUrl = "https://raw.githubusercontent.com/azizndao/quran_data/dev/images/7.png",
        nameResource = R.string.qari_ajamy_gapless,
        url = "https://download.quranicaudio.com/quran/ahmed_ibn_3ali_al-3ajamy",
      ),
      Qari(
        id = 14,
        slug = "hani_rifai",
        imageUrl = "https://raw.githubusercontent.com/azizndao/quran_data/dev/images/10.png",
        nameResource = R.string.qari_hani_rifai_gapless,
        url = "https://download.quranicaudio.com/quran/rifai",
      ),
      Qari(
        id = 15,
        slug = "husary",
        imageUrl = "https://raw.githubusercontent.com/azizndao/quran_data/dev/images/11.png",
        nameResource = R.string.qari_husary_gapless,
        url = "https://download.quranicaudio.com/quran/mahmood_khaleel_al-husaree",
      ),
      Qari(
        id = 16,
        slug = "ali_hudhayfi",
        imageUrl = "https://raw.githubusercontent.com/azizndao/quran_data/dev/images/13.png",
        nameResource = R.string.qari_hudhayfi_gapless,
        url = "https://download.quranicaudio.com/quran/huthayfi",
      ),
      Qari(
        id = 17,
        slug = "ibrahim_alakhdar",
        imageUrl = "https://raw.githubusercontent.com/azizndao/quran_data/dev/images/14.png",
        nameResource = R.string.qari_ibrahim_alakhdar_gapless,
        url = "https://download.quranicaudio.com/quran/ibrahim_al_akhdar",
      ),
      Qari(
        id = 18,
        slug = "muaiqly_kfgqpc",
        imageUrl = "https://raw.githubusercontent.com/azizndao/quran_data/dev/images/15.png",
        nameResource = R.string.qari_muaiqly_gapless,
        url = "https://download.quranicaudio.com/quran/maher_almu3aiqly/year1440",
      ),
      Qari(
        id = 19,
        slug = "mohammad_altablawi",
        imageUrl = "https://raw.githubusercontent.com/azizndao/quran_data/dev/images/19.png",
        nameResource = R.string.qari_mohammad_altablawi_gapless,
        url = "https://download.quranicaudio.com/quran/mohammad_altablawi",
      ),
      Qari(
        id = 20,
        slug = "muhammad_ayyoub",
        imageUrl = "https://raw.githubusercontent.com/azizndao/quran_data/dev/images/20.png",
        nameResource = R.string.qari_ayyoub_gapless,
        url = "https://download.quranicaudio.com/quran/muhammad_ayyoob",
      ),
      Qari(
        id = 21,
        slug = "mjibreel",
        imageUrl = "https://raw.githubusercontent.com/azizndao/quran_data/dev/images/21.png",
        nameResource = R.string.qari_jibreel_gapless,
        url = "https://download.quranicaudio.com/quran/muhammad_jibreel/complete",
      ),
      Qari(
        id = 22,
        slug = "salah_bukhatir",
        imageUrl = "https://raw.githubusercontent.com/azizndao/quran_data/dev/images/29.png",
        nameResource = R.string.qari_salah_bukhatir_gapless,
        url = "https://download.quranicaudio.com/quran/salaah_bukhaatir",
      ),
      Qari(
        id = 23,
        slug = "abdul_muhsin_alqasim",
        imageUrl = "https://raw.githubusercontent.com/azizndao/quran_data/dev/images/30.png",
        nameResource = R.string.qari_abdulmuhsin_qasim_gapless,
        url = "https://download.quranicaudio.com/quran/abdul_muhsin_alqasim",
      ),
      Qari(
        id = 24,
        slug = "abdullah_juhany",
        imageUrl = "https://raw.githubusercontent.com/azizndao/quran_data/dev/images/31.png",
        nameResource = R.string.qari_juhany_gapless,
        url = "https://download.quranicaudio.com/quran/abdullaah_3awwaad_al-juhaynee",
      ),
      Qari(
        id = 25,
        slug = "salah_budair",
        imageUrl = "https://raw.githubusercontent.com/azizndao/quran_data/dev/images/32.png",
        nameResource = R.string.qari_salah_budair_gapless,
        url = "https://download.quranicaudio.com/quran/salahbudair",
      ),
      Qari(
        id = 26,
        slug = "ahmad_nauina",
        imageUrl = "https://raw.githubusercontent.com/azizndao/quran_data/dev/images/34.png",
        nameResource = R.string.qari_ahmad_nauina_gapless,
        url = "https://download.quranicaudio.com/quran/ahmad_nauina",
      ),
      Qari(
        id = 28,
        slug = "khalifa_taniji",
        imageUrl = "https://raw.githubusercontent.com/azizndao/quran_data/dev/images/36.png",
        nameResource = R.string.qari_khalifa_taniji_gapless,
        url = "https://download.quranicaudio.com/quran/khalifah_taniji",
      ),
      Qari(
        id = 29,
        slug = "mahmoud_ali_albana",
        imageUrl = "https://raw.githubusercontent.com/azizndao/quran_data/dev/images/37.png",
        nameResource = R.string.qari_mahmoud_ali_albana_gapless,
        url = "https://download.quranicaudio.com/quran/mahmood_ali_albana",
      ),
      Qari(
        id = 30,
        slug = "husary_muallim",
        imageUrl = "https://raw.githubusercontent.com/azizndao/quran_data/dev/images/41.png",
        nameResource = R.string.qari_husary_mujawwad_gapless,
        url = "https://download.quranicaudio.com/quran/husary_muallim",
      ),
      Qari(
        id = 31,
        slug = "khalid_alqahtani",
        imageUrl = "https://raw.githubusercontent.com/azizndao/quran_data/dev/images/42.png",
        nameResource = R.string.qari_khalid_qahtani_gapless,
        url = "https://download.quranicaudio.com/quran/khaalid_al-qahtaanee",
      ),
      Qari(
        id = 32,
        slug = "yasser_dussary",
        imageUrl = "https://raw.githubusercontent.com/azizndao/quran_data/dev/images/43.png",
        nameResource = R.string.qari_yasser_dussary_gapless,
        url = "https://download.quranicaudio.com/quran/yasser_ad-dussary",
      ),
      Qari(
        id = 33,
        slug = "qatami",
        imageUrl = "https://raw.githubusercontent.com/azizndao/quran_data/dev/images/44.png",
        nameResource = R.string.qari_qatami_gapless,
        url = "https://download.quranicaudio.com/quran/nasser_bin_ali_alqatami",
      ),
      Qari(
        id = 34,
        slug = "ali_hajjaj_alsouasi",
        imageUrl = "https://raw.githubusercontent.com/azizndao/quran_data/dev/images/45.png",
        nameResource = R.string.qari_ali_hajjaj_alsouasi_gapless,
        url = "https://download.quranicaudio.com/quran/ali_hajjaj_alsouasi",
      ),
      Qari(
        id = 35,
        slug = "yasser_dussary",
        imageUrl = "https://raw.githubusercontent.com/azizndao/quran_data/dev/images/46.png",
        nameResource = R.string.qari_yasser_dussary_gapless,
        url = "https://download.quranicaudio.com/quran/sahl_yaaseen",
      ),
      Qari(
        id = 36,
        slug = "aziz_alili",
        imageUrl = "https://raw.githubusercontent.com/azizndao/quran_data/dev/images/49.png",
        nameResource = R.string.qari_aziz_alili_gapless,
        url = "https://download.quranicaudio.com/quran/aziz_alili",
      ),
      Qari(
        id = 37,
        slug = "akram_al_alaqmi",
        imageUrl = "https://raw.githubusercontent.com/azizndao/quran_data/dev/images/51.png",
        nameResource = R.string.qari_akram_al_alaqmi,
        url = "https://download.quranicaudio.com/quran/akram_al_alaqmi",
      ),
      Qari(
        id = 38,
        slug = "fares_abbad",
        imageUrl = "https://raw.githubusercontent.com/azizndao/quran_data/dev/images/52.png",
        nameResource = R.string.qari_fares_abbad_gapless,
        url = "https://download.quranicaudio.com/quran/fares",
      ),
      Qari(
        id = 39,
        slug = "ibrahim_walk",
        imageUrl = "https://raw.githubusercontent.com/azizndao/quran_data/dev/images/23.png",
        nameResource = R.string.qari_walk_gapless,
        url = "https://download.quranicaudio.com/quran/ibrahim_walk",
      ),
    )
  }
}
