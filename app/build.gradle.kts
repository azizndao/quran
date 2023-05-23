@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
  id("muslimapp.android.application")
  id("muslimapp.android.application.compose")
  id("kotlinx-serialization")
  alias(libs.plugins.ksp)
}

android {
  namespace = "org.alquran"

  defaultConfig {
    applicationId = "org.alquran"
    versionCode = 10
    versionName = "1.0.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    vectorDrawables {
      useSupportLibrary = true
    }
  }

  buildTypes {
    debug {
      applicationIdSuffix = ".debug"
      versionNameSuffix = "-DEBUG"
    }

    release {
      isShrinkResources = true
      isMinifyEnabled = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      signingConfig = signingConfigs.getByName("debug")
    }
  }

  kotlinOptions {
    freeCompilerArgs = freeCompilerArgs + listOf(
      "-opt-in=kotlin.RequiresOptIn",
      // Enable experimental coroutines APIs, including Flow
      "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
      "-opt-in=kotlinx.coroutines.FlowPreview",
      "-opt-in=kotlin.Experimental",
      // Enable experimental kotlinx serialization APIs
      "-opt-in=kotlinx.serialization.ExperimentalSerializationApi",
      "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
      "-opt-in=androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi",
      "-opt-in=androidx.lifecycle.compose.ExperimentalLifecycleComposeApi",
      "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
      "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
      "-opt-in=androidx.compose.animation.ExperimentalAnimationApi",
      "-opt-in=com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi",
      "-opt-in=androidx.compose.material.ExperimentalMaterialApi",
      "-opt-in=com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi",
    )
  }
}

dependencies {

  implementation(project(":core:datastore"))
  implementation(project(":core:design-system"))
  implementation(project(":core:audio"))
  implementation(project(":core:models"))

  implementation(project(":features:home"))
  implementation(project(":features:search"))
  implementation(project(":features:saved"))
  implementation(project(":features:share"))
  implementation(project(":features:pager"))
  implementation(project(":features:translations"))
  implementation(project(":features:verse-menu"))

  implementation(libs.androidx.compose.material)
  implementation(libs.timber)

  implementation(libs.accompanist.navigation.material)

  implementation(libs.androidx.work)

  implementation(libs.koin.android)
  implementation(libs.koin.compose)
  implementation(libs.koin.workmanager)

  implementation(libs.androidx.ktx)
  implementation(libs.activity.compose)
}