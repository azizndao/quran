package org.muslimapp.core.audio.models

data class Reciter(
  val id: String,
  val name: String,
  val image: String,
  val language: String = "ar",
  val subfolder: String,
)

