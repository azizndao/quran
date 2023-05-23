plugins {
  id("muslimapp.android.library")
  alias(libs.plugins.ksp)
  id("kotlinx-serialization")
}

android {
  namespace = "arg.quran.model"
}

dependencies {

  implementation(libs.room.ktx)
  ksp(libs.room.compiler)

  implementation(libs.kotlinx.serialization)
}