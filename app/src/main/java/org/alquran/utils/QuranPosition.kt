package org.alquran.utils

import arg.quran.models.quran.VerseKey
import org.quran.datastore.QuranPosition


val QuranPosition.verseKey get() = VerseKey(sura, ayah)
