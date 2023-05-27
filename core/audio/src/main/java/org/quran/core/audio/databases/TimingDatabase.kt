package org.quran.core.audio.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.quran.core.audio.models.AyahTiming
import org.quran.core.audio.models.AyahTimingDao

@Database(
  version = 1,
  exportSchema = true,
  entities = [AyahTiming::class]
)
abstract class TimingDatabase : RoomDatabase() {

  abstract val timingDao: AyahTimingDao

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