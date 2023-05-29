package org.quran.features.share.utils

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import org.quran.features.share.ShareAyahUiState


class ShareAyahPreviewParameterProvider : PreviewParameterProvider<ShareAyahUiState.Success> {
  override val values: Sequence<ShareAyahUiState.Success> = sequenceOf(
    ShareAyahUiState.Success(
      sura = 4,
      ayah = 1,
      surahName = "An-Nisa",
      text = "ﭑ ﭒ ﭓ ﭔ ﭕ ﭖ ﭗ ﭘ ﭙ ﭚ ﭛ ﭜ ﭝ ﭞ ﭟ ﭠ ﭡ ﭢ ﭣ ﭤ ﭥ ﭦ ﭧ ﭨ ﭩ ﭪ ﭫ ﭬ ﭭ ﭮ ﭯ",
      translation = "O hommes! Craig nez votre Seigneur qui vous a créés d'un seul être, et a créé de celui-ci son épouse, et qui de ces deux là a fait répandre (sur la terre) beaucoup d'hommes et de femmes. Craignez Allah au nom duquel vous vous implorez les uns les autres, et craignez de rompre les liens du sang. Certes Allah vous observe parfaitement.",
      page = 77,
    ),
    ShareAyahUiState.Success(
      sura = 4,
      ayah = 2,
      text = "ﭰ ﭱ ﭲ ﭳ ﭴ ﭵ ﭶ ﭷ ﭸ ﭹ ﭺ ﭻ ﭼ ﭽ ﭾ ﭿ ﮀ ﮁ ﮂ ﮃ",
      translation = "Et donnez aux orphelins leurs biens; n'y substituez pas le mauvais au bon. Ne mangez pas leurs biens avec les vôtres: c'est vraiment un grand péché.",
      surahName = "An-Nisa",
      page = 77,
    ),
    ShareAyahUiState.Success(
      sura = 4,
      ayah = 3,
      text = "ﮄ ﮅ ﮆ ﮇ ﮈ ﮉ ﮊ ﮋ ﮌ ﮍ ﮎ ﮏ ﮐ ﮑ ﮒ ﮓ ﮔ ﮕ ﮖ ﮗ ﮘ ﮙ ﮚ ﮛ ﮜ ﮝ ﮞ ﮟ ﮠ ﮡ ﮢ",
      translation = "Et si vous craignez de n'être pas justes envers les orphelins,... Il est permis d'épouser deux, trois ou quatre, parmi les femmes qui vous plaisent, mais, si vous craignez de n'être pas justes avec celles-ci, alors une seule, ou des esclaves que vous possédez. Cela, afin de ne pas faire d'injustice (ou afin de ne pas aggraver votre charge de famille).",
      surahName = "An-Nisa",
      page = 77,
    ),
    ShareAyahUiState.Success(
      sura = 4,
      ayah = 4,
      text = "ﮣ ﮤ ﮥ ﮦ ﮧ ﮨ ﮩ ﮪ ﮫ ﮬ ﮭ ﮮ ﮯ ﮰ ﮱ ﯓ",
      translation = "Et donnez aux épouses leur mahr, de bonne grâce. Si de bon gré, elles vous en abandonnent quelque chose, disposez-en alors à votre aise et de bon cœur.",
      surahName = "An-Nisa",
      page = 77,
    ),
    ShareAyahUiState.Success(
      sura = 4,
      ayah = 5,
      text = "ﯔ ﯕ ﯖ ﯗ ﯘ ﯙ ﯚ ﯛ ﯜ ﯝ ﯞ ﯟ ﯠ ﯡ ﯢ ﯣ ﯤ",
      translation = "Et ne confiez pas aux incapables vos biens dont Allah a fait votre subsistance. Mais prélevez-en, pour eux, nourriture et vêtement; et parlez-leur convenablement.",
      surahName = "An-Nisa",
      page = 77,
    ),
    ShareAyahUiState.Success(
      sura = 4,
      ayah = 6,
      text = "ﯥ ﯦ ﯧ ﯨ ﯩ ﯪ ﯫ ﯬ ﯭ ﯮ ﯯ ﯰ ﯱ ﯲ ﯳ ﯴ ﯵ ﯶ ﯷ ﯸ ﯹ ﯺ ﯻ ﯼ ﯽ ﯾ ﯿ ﰀ ﰁ ﰂ ﰃ ﰄ ﰅ ﰆ ﰇ ﰈ ﰉ ﰊ ﰋ ﰌ ﰍ ﰎ ﰏ",
      translation = "Et éprouvez (la capacité) des orphelins jusqu'à ce qu'ils atteignent (l'aptitude) au mariage; et si vous ressentez en eux une bonne conduite, remettez-leur leurs biens. Ne les utilisez pas (dans votre intérêt) avec gaspillage et dissipation, avant qu'ils ne grandissent. Quiconque est aisé, qu'il s'abstienne d'en prendre lui-même. S'il est pauvre, alors qu'il en utilise raisonnablement: et lorsque vous leur remettez leurs biens, prenez des témoins à leur encontre. Mais Allah suffit pour observer et compter.",
      surahName = "An-Nisa",
      page = 77,
    ),
  )
}
