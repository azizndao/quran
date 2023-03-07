package org.muslimapp.core.audio.models

private data class Reciter(
  val id: Int = -1,
  val slug: String,
  val name: String,
  val image: String,
  val language: String = "ar",
  val subfolder: String,
  val hasWordSegments: Boolean = false,
)

