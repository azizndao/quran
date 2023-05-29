package org.quran.features.pager.uiState

import arg.quran.models.quran.VerseKey
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

data class TranslationPage(
  override val page: Int = 0,
  override val header: Header,
  val items: PersistentList<Row> = persistentListOf(),
  val scrollIndex: Int = 0,
  val firstItem: Boolean = true,
) : PageItem() {
  sealed class Row(val key: String)

  data class Chapter(val number: Int, val name: String) : Row("$number")

  data class Verse(
    val suraAyah: VerseKey,
    val text: String,
    val isBookmarked: Boolean = false,
    val highLighted: Boolean = false,
    val isPlaying: Boolean = false,
  ) : Row("Verse($suraAyah)")

  data class TranslatedVerse(
    val suraAyah: VerseKey,
    val text: String,
    val authorName: String,
    val isBookmarked: Boolean = false,
    val highLighted: Boolean = false,
    val isPlaying: Boolean = false,
  ) : Row("$authorName($suraAyah)")

  data class VerseToolbar(
    val suraAyah: VerseKey,
    val isBookmarked: Boolean = false,
    val highLighted: Boolean = false,
    val isPlaying: Boolean = false,
  ) : Row("Toolbar($suraAyah)")

  class Divider(key: String) : Row(key)
}
