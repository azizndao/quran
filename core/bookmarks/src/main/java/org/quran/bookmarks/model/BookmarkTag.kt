package org.quran.bookmarks.model

import kotlinx.serialization.Serializable

sealed class BookmarkTag {

  @Serializable
  object Bookmark : BookmarkTag()

  @Serializable
  object Recent : BookmarkTag()

  @Serializable
  object PopularAya : BookmarkTag()

  @Serializable
  object PopularSura : BookmarkTag()

  @Serializable
  class Custom(val name: String) : BookmarkTag()
}

