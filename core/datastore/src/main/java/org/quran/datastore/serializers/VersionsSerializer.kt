package org.quran.datastore.serializers

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import org.quran.datastore.VersionsPreferences
import java.io.InputStream
import java.io.OutputStream

class VersionsSerializer : Serializer<VersionsPreferences> {
    override val defaultValue: VersionsPreferences = VersionsPreferences.newBuilder()
        .setHafs(8)
        .setQuranImage(1)
        .setReciters(2)
        .build()

    override suspend fun readFrom(input: InputStream): VersionsPreferences = try {
        VersionsPreferences.parseFrom(input)
    } catch (exception: InvalidProtocolBufferException) {
        throw CorruptionException("Cannot read proto.", exception)
    }

    override suspend fun writeTo(t: VersionsPreferences, output: OutputStream) {
        t.writeTo(output)
    }
}