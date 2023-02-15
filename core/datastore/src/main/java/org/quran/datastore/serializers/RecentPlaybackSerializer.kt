package org.quran.datastore.serializers

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import org.quran.datastore.RecentPlayback
import java.io.InputStream
import java.io.OutputStream

internal class RecentPlaybackSerializer : Serializer<RecentPlayback> {
    override val defaultValue: RecentPlayback = RecentPlayback.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): RecentPlayback = try {
        (RecentPlayback.parseFrom(input))
    } catch (exception: InvalidProtocolBufferException) {
        throw CorruptionException("Cannot read proto.", exception)
    }

    override suspend fun writeTo(t: RecentPlayback, output: OutputStream) {
        t.writeTo(output)
    }
}