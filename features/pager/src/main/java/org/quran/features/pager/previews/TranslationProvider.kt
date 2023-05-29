package org.quran.features.pager.previews

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import arg.quran.models.quran.VerseKey
import org.quran.features.pager.uiState.TranslationPage


class TranslationProvider : PreviewParameterProvider<TranslationPage.TranslatedVerse> {
  override val values = sequenceOf(
    TranslationPage.TranslatedVerse(
      suraAyah = VerseKey(sura = 4, aya = 1),
      text = "O hommes! Craig nez votre Seigneur qui vous a créés d'un seul être, et a créé de celui-ci son épouse, et qui de ces deux là a fait répandre (sur la terre) beaucoup d'hommes et de femmes. Craignez Allah au nom duquel vous vous implorez les uns les autres, et craignez de rompre les liens du sang. Certes Allah vous observe parfaitement.",
      authorName = "Mouhamad Hamidullah",
      isPlaying = true,
    ),
    TranslationPage.TranslatedVerse(
      suraAyah = VerseKey(sura = 4, aya = 2),
      text = "Et donnez aux orphelins leurs biens; n'y substituez pas le mauvais au bon. Ne mangez pas leurs biens avec les vôtres: c'est vraiment un grand péché.",
      authorName = "Mouhamad Hamidullah",
      isBookmarked = true,
    ),
    TranslationPage.TranslatedVerse(
      suraAyah = VerseKey(sura = 4, aya = 3),
      text = "Et si vous craignez de n'être pas justes envers les orphelins,... Il est permis d'épouser deux, trois ou quatre, parmi les femmes qui vous plaisent, mais, si vous craignez de n'être pas justes avec celles-ci, alors une seule, ou des esclaves que vous possédez. Cela, afin de ne pas faire d'injustice (ou afin de ne pas aggraver votre charge de famille).",
      authorName = "Mouhamad Hamidullah"
    ),
    TranslationPage.TranslatedVerse(
      suraAyah = VerseKey(sura = 4, aya = 4),
      text = "Et donnez aux épouses leur mahr, de bonne grâce. Si de bon gré, elles vous en abandonnent quelque chose, disposez-en alors à votre aise et de bon cœur.",
      authorName = "Mouhamad Hamidullah"
    ),
    TranslationPage.TranslatedVerse(
      suraAyah = VerseKey(sura = 4, aya = 5),
      text = "Et ne confiez pas aux incapables vos biens dont Allah a fait votre subsistance. Mais prélevez-en, pour eux, nourriture et vêtement; et parlez-leur convenablement.",
      authorName = "Mouhamad Hamidullah"
    ),
    TranslationPage.TranslatedVerse(
      suraAyah = VerseKey(sura = 4, aya = 6),
      text = "Et éprouvez (la capacité) des orphelins jusqu'à ce qu'ils atteignent (l'aptitude) au mariage; et si vous ressentez en eux une bonne conduite, remettez-leur leurs biens. Ne les utilisez pas (dans votre intérêt) avec gaspillage et dissipation, avant qu'ils ne grandissent. Quiconque est aisé, qu'il s'abstienne d'en prendre lui-même. S'il est pauvre, alors qu'il en utilise raisonnablement: et lorsque vous leur remettez leurs biens, prenez des témoins à leur encontre. Mais Allah suffit pour observer et compter.",
      authorName = "Mouhamad Hamidullah"
    ),
  )
}
