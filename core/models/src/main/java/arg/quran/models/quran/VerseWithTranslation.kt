package arg.quran.models.quran

import androidx.room.Embedded
import androidx.room.Relation

data class VerseWithTranslation(
  @Embedded val verse: Verse,

  @Relation(parentColumn = "key", entityColumn = "key")
  val translation: List<VerseTranslation>
)