@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
  id("quran.android.library")
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