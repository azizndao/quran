package org.muslimapp.core.audio.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import arg.quran.models.audio.AudioFile
import arg.quran.models.audio.AyaTiming
import org.muslimapp.core.audio.dao.AudioFileDao
import org.muslimapp.core.audio.dao.AyahTimingDao
import org.muslimapp.core.audio.databases.convertors.SegmentsListConvertor

@Database(
  version = 1,
  exportSchema = true,
  entities = [AyaTiming::class, AudioFile::class]
)
@TypeConverters(SegmentsListConvertor::class)
abstract class TimingDatabase : RoomDatabase() {

  abstract val timingDao: AyahTimingDao
  abstract val audioFileDao: AudioFileDao

  companion object {

    @Volatile
    private var instance: TimingDatabase? = null

    @Volatile
    private var instanceName: String? = null

    fun getInstance(context: Context, reciterId: String): TimingDatabase {
      synchronized(this) {
        if (instanceName != "$reciterId.db") {
          instanceName = "$reciterId.db"
          instance?.close()
          instance = Room
            .databaseBuilder(context, TimingDatabase::class.java, instanceName!!)
            .build()
        }
        return instance!!
      }
    }
  }
}