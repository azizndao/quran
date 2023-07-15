@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
  id("quran.android.application")
  id("quran.android.application.compose")
  id("kotlinx-serialization")
  alias(libs.plugins.ksp)
}

android {
  namespace = "org.alquran"

  buildFeatures {
    buildConfig = true
  }

  defaultConfig {
    applicationId = "org.alquran"
    versionCode = 18
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
      // Enable experimental kotlinx serialization APIs
      "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
      "-opt-in=androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi",
      "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
      "-opt-in=androidx.compose.animation.ExperimentalAnimationApi",
    )
  }
}

dependencies {

  implementation(project(":core:common"))
  implementation(project(":core:datastore"))
  implementation(project(":core:design-system"))
  implementation(project(":core:audio"))
  implementation(project(":core:models"))

  implementation(project(":features:audio"))
  implementation(project(":features:home"))
  implementation(project(":features:search"))
  implementation(project(":features:share"))
  implementation(project(":features:pager"))
  implementation(project(":features:translations"))
  implementation(project(":features:settings"))
  implementation(project(":features:qari"))

  implementation(libs.navigation.compose)

  implementation(libs.timber)

  implementation(libs.androidx.work)

  implementation(libs.koin.android)
  implementation(libs.koin.compose)
  implementation(libs.koin.workmanager)

  implementation(libs.androidx.ktx)
  implementation(libs.activity.compose)
}