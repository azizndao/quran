package org.quran.features.pager.uiState

import arg.quran.models.quran.VerseKey
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

data class TranslationPage(
  override val page: Int = 0,
  override val header: Header,
  val items: PersistentList<Row> = persistentListOf(),
  val scrollIndex: Int,
  val firstItem: Boolean,
) : QuranPageItem() {
  sealed class Row(open val key: Any)

  data class Chapter(val number: Int, val name: String) : Row(number)

  data class Verse(
    val suraAyah: VerseKey,
    val text: String,
    val isBookmarked: Boolean,
    val highLighted: Boolean,
    val isPlaying: Boolean,
  ) : Row("Verse($suraAyah)")

  data class TranslatedVerse(
    val suraAyah: VerseKey,
    val text: String,
    val authorName: String,
    val isBookmarked: Boolean,
    val highLighted: Boolean,
    val isPlaying: Boolean,
  ) : Row("$authorName($suraAyah)")

  data class VerseToolbar(
    val suraAyah: VerseKey,
    val isBookmarked: Boolean,
    val highLighted: Boolean,
    val isPlaying: Boolean,
  ) : Row("Toolbar($suraAyah)")

  class Divider(key: Any) : Row(key)
}
