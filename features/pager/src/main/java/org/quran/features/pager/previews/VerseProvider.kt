package org.quran.features.pager.previews

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import arg.quran.models.quran.VerseKey
import org.quran.features.pager.uiState.TranslationPage


class VerseProvider : PreviewParameterProvider<TranslationPage.Verse> {
  override val values = sequenceOf(
    TranslationPage.Verse(
      suraAyah = VerseKey(sura = 4, aya = 1),
      text = "ﭑ ﭒ ﭓ ﭔ ﭕ ﭖ ﭗ ﭘ ﭙ ﭚ ﭛ ﭜ ﭝ ﭞ ﭟ ﭠ ﭡ ﭢ ﭣ ﭤ ﭥ ﭦ ﭧ ﭨ ﭩ ﭪ ﭫ ﭬ ﭭ ﭮ ﭯ",
      isPlaying = true,
    ),
    TranslationPage.Verse(
      suraAyah = VerseKey(sura = 4, aya = 2),
      text = "ﭰ ﭱ ﭲ ﭳ ﭴ ﭵ ﭶ ﭷ ﭸ ﭹ ﭺ ﭻ ﭼ ﭽ ﭾ ﭿ ﮀ ﮁ ﮂ ﮃ",
      isBookmarked = true,
    ),
    TranslationPage.Verse(
      suraAyah = VerseKey(sura = 4, aya = 3),
      text = "ﮄ ﮅ ﮆ ﮇ ﮈ ﮉ ﮊ ﮋ ﮌ ﮍ ﮎ ﮏ ﮐ ﮑ ﮒ ﮓ ﮔ ﮕ ﮖ ﮗ ﮘ ﮙ ﮚ ﮛ ﮜ ﮝ ﮞ ﮟ ﮠ ﮡ ﮢ",
    ),
    TranslationPage.Verse(
      suraAyah = VerseKey(sura = 4, aya = 4),
      text = "ﮣ ﮤ ﮥ ﮦ ﮧ ﮨ ﮩ ﮪ ﮫ ﮬ ﮭ ﮮ ﮯ ﮰ ﮱ ﯓ",
    ),
    TranslationPage.Verse(
      suraAyah = VerseKey(sura = 4, aya = 5),
      text = "ﯔ ﯕ ﯖ ﯗ ﯘ ﯙ ﯚ ﯛ ﯜ ﯝ ﯞ ﯟ ﯠ ﯡ ﯢ ﯣ ﯤ",
    ),
    TranslationPage.Verse(
      suraAyah = VerseKey(sura = 4, aya = 6),
      text = "ﯥ ﯦ ﯧ ﯨ ﯩ ﯪ ﯫ ﯬ ﯭ ﯮ ﯯ ﯰ ﯱ ﯲ ﯳ ﯴ ﯵ ﯶ ﯷ ﯸ ﯹ ﯺ ﯻ ﯼ ﯽ ﯾ ﯿ ﰀ ﰁ ﰂ ﰃ ﰄ ﰅ ﰆ ﰇ ﰈ ﰉ ﰊ ﰋ ﰌ ﰍ ﰎ ﰏ",
    ),
  )
}
