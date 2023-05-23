@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
  id("muslimapp.android.library")
  id("kotlinx-serialization")
  alias(libs.plugins.ksp)
}

android {
  namespace = "org.quram.common"
}

dependencies {
  implementation(project(":core:models"))

  api(libs.kotlinx.serialization)

  api(libs.koin.android)

  api(libs.timber)

  implementation(libs.room.ktx)
  ksp(libs.room.compiler)
}

