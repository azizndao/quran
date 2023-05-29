@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
  id("quran.android.library")
  id("kotlinx-serialization")
  alias(libs.plugins.ksp)
}

android {
  namespace = "org.quram.common"
}

dependencies {
  implementation(project(":core:models"))

  api(libs.kotlinx.serialization)

  implementation(libs.room.ktx)
  ksp(libs.room.compiler)
}

