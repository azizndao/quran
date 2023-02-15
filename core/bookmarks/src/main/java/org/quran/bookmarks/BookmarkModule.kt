package org.quran.bookmarks

import androidx.room.Room
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import org.quran.bookmarks.databases.BookmarksDatabase
import org.quran.bookmarks.repository.BookmarkRepository

val BookmarkModule = module {
    single {
        Room.databaseBuilder(get(), BookmarksDatabase::class.java, "quran.bookmarks.db").build()
    }

    factoryOf(::BookmarkRepository)
}