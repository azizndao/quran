package org.muslimapp

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

/**
 * Configure Compose-specific options
 */
internal fun Project.configureAndroidCompose(
  commonExtension: CommonExtension<*, *, *, *>,
) {
  val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

  commonExtension.apply {
    buildFeatures {
      compose = true
    }

    composeOptions {
      kotlinCompilerExtensionVersion =
        libs.findVersion("composeCompiler").get().toString()
    }

    kotlinOptions {
      jvmTarget = "11"
      freeCompilerArgs = freeCompilerArgs + listOf(
        "-opt-in=kotlin.RequiresOptIn",
        // Enable experimental coroutines APIs, including Flow
        "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
        "-opt-in=kotlinx.coroutines.FlowPreview",
        // Enable experimental kotlinx serialization APIs
        "-opt-in=kotlinx.serialization.ExperimentalSerializationApi",
        "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
        "-opt-in=androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi",
        "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
        "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
        "-opt-in=androidx.compose.animation.ExperimentalAnimationApi",
        "-opt-in=com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi",
      )
    }

    compileOptions {
      sourceCompatibility = JavaVersion.VERSION_11
      targetCompatibility = JavaVersion.VERSION_11
    }

    dependencies {
      val bom = libs.findLibrary("androidx-compose-bom").get()
      add("implementation", platform(bom))
      add("androidTestImplementation", platform(bom))
    }
  }
}

