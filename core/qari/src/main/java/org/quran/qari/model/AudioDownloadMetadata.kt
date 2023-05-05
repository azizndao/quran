package org.quran.qari.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AudioDownloadMetadata(val qariId: Int) : Parcelable
