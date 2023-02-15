package org.alquran.utils

import org.quran.datastore.QuranPosition
import org.quram.common.model.VerseKey


val QuranPosition.verseKey get() = VerseKey(sura, ayah)
