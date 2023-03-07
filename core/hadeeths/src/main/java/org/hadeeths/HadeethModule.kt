package org.hadeeths

import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import org.hadeeths.repositories.HadithsRepository
import org.hadeeths.repositories.HadithsRepositoryImpl
import org.hadeeths.services.HadithApiService
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit


val HadeethModule = module {

  factory {
    Retrofit.Builder()
      .baseUrl("https://hadeethenc.com/api/v1/")
      .addConverterFactory(Json.asConverterFactory(MediaType.get("application/json")))
      .build()
      .create(HadithApiService::class.java)
  }

  single {
    Room
      .databaseBuilder(get(), HadithsDatabase::class.java, "hadeeths.db")
      .build()
  }

  factory { get<HadithsDatabase>().bookmarkDao }

  factory { get<HadithsDatabase>().hadithDao }

  factory { HadithsRepositoryImpl(get(), get(), get()) } bind HadithsRepository::class
}