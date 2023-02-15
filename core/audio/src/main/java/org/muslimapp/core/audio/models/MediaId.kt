package org.muslimapp.core.audio.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class MediaId(val reciter: String, val sura: Int, val ayah: Int) {

    override fun toString() = Json.encodeToString(this)

    companion object {
        fun fromString(value: String): MediaId = Json.decodeFromString(value)
    }
}

