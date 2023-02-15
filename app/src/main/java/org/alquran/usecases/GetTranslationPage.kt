package org.alquran.usecases


import android.content.Context
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapMerge
import org.alquran.audio.models.AudioState
import org.alquran.hafs.model.VerseWithTranslation
import org.alquran.hafs.repository.VerseRepository
import org.alquran.ui.uistate.QuranPageItem
import org.alquran.ui.uistate.TranslationPage
import org.alquran.ui.uistate.indexOfItem
import org.muslimapp.core.audio.PlaybackConnection
import org.quram.common.model.Sura
import org.quram.common.model.VerseKey
import org.quram.common.repositories.SurahRepository
import org.quram.common.utils.QuranDisplayData
import org.quram.common.utils.UriProvider
import org.quran.bookmarks.repository.BookmarkRepository
import org.quran.datastore.repositories.QuranPreferencesRepository

class GetTranslationPage(
    private val verseRepository: VerseRepository,
    private val playbackConnection: PlaybackConnection,
    private val bookmarkRepository: BookmarkRepository,
    private val surahRepository: SurahRepository,
    private val pageHeaderUseCase: PageHeaderUseCase,
    private val quranPreferences: QuranPreferencesRepository,
    private val quranDisplayData: QuranDisplayData,
    private val context: Context,
) {

    private fun getVerseInPage(page: Int): Flow<List<VerseWithTranslation>> {
        return quranPreferences.getFontVersion().flatMapMerge { edition ->
            verseRepository.getVersesWithTranslations(
                page,
                edition
            )
        }
    }

    operator fun invoke(
        page: Int,
        selectedVerse: VerseKey?,
        version: Int
    ): Flow<TranslationPage> {
        val keys = quranDisplayData.getAyahKeysOnPage(page)

        var pageHeader: QuranPageItem.Header? = null

        var suras: List<Sura>? = null

        var lastScrollIndex = -1

        return combine(
            verseRepository.getVersesWithTranslations(page, version),
            bookmarkRepository.getBookmarksWithKeys(keys),
            playbackConnection.currentAyah,
            playbackConnection.playingState,
        ) { verses, bookmarks, nowPlaying, audioState ->
            if (suras == null) {
                suras = surahRepository.getSurahsInPage(page)
            }

            if (pageHeader == null) {
                pageHeader = pageHeaderUseCase(page)
            }

            val data = mutableListOf<TranslationPage.Row>()

            verses.forEachIndexed { index, (verse, translations) ->
                val verseKey = verse.key
                if (verseKey.ayah == 1) {
                    val surah = suras!!.find { it.number == verseKey.sura }!!
                    data.add(TranslationPage.Surah(surah.number, surah.nameSimple))
                }

                if (index > 0 && verses[index].verse.key.ayah != 1) data.add(
                    TranslationPage.Divider("d$index")
                )

                val isPlaying =
                    verseKey.ayah == nowPlaying?.ayah && verseKey.sura == nowPlaying.sura

                val highLighted =
                    verseKey.ayah == selectedVerse?.ayah && verseKey.sura == selectedVerse.ayah

                val isBookmarked =
                    bookmarks.any { verseKey.ayah == it.key.ayah && it.key.sura == verseKey.sura }

                data.add(
                    TranslationPage.Verse(
                        verse = verse,
                        audioState = if (isPlaying) audioState else AudioState.PAUSED,
                        isBookmarked = isBookmarked,
                        highLighted = highLighted
                    )
                )

                translations.forEach { translation ->
                    data.add(
                        TranslationPage.Translation(
                            translation = translation,
                            audioState = if (isPlaying) audioState else AudioState.PAUSED,
                            isBookmarked = isBookmarked,
                            highLighted = highLighted
                        )
                    )
                }

                data.add(
                    TranslationPage.AyahToolbar(
                        verseKey = verseKey,
                        audioState = if (isPlaying) audioState else AudioState.PAUSED,
                        isBookmarked = isBookmarked,
                        highLighted = highLighted
                    )
                )
            }

            var scrollIndex = when {
                nowPlaying != null -> data.indexOfItem(nowPlaying.sura, nowPlaying.ayah)
                else -> selectedVerse?.let { data.indexOfItem(it.sura, selectedVerse.ayah) } ?: -1
            }

            if (scrollIndex == lastScrollIndex) scrollIndex = -1

            TranslationPage(
                header = pageHeader!!,
                page = page,
                items = data,
                firstItem = lastScrollIndex == -1,
                scrollIndex = scrollIndex,
                fontFamily = FontFamily(Font(UriProvider.getFontFile(context, page, version)))
            ).also { lastScrollIndex = scrollIndex }
        }
    }
}