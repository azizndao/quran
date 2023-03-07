package org.hadeeths

import androidx.room.RenameColumn
import androidx.room.migration.AutoMigrationSpec

internal class DatabaseMigrations {

  @RenameColumn(
    tableName = "hadith_bookmarks",
    fromColumnName = "id",
    toColumnName = "bookmark_id"
  )
  class Migration2To3 : AutoMigrationSpec

  @RenameColumn(
    tableName = "categories",
    fromColumnName = "title",
    toColumnName = "name"
  )
  class Migration3To4 : AutoMigrationSpec

  @RenameColumn(
    tableName = "languages",
    fromColumnName = "code",
    toColumnName = "isoCode"
  )
  @RenameColumn(
    tableName = "languages",
    fromColumnName = "native",
    toColumnName = "nativeName"
  )
  class Migration4To5 : AutoMigrationSpec
}