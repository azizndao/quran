import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.kotlin
import org.muslimapp.configureKotlinAndroid

class AndroidLibraryConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      with(pluginManager) {
        apply("com.android.library")
        apply("org.jetbrains.kotlin.android")
      }

      extensions.configure<LibraryExtension> {
        configureKotlinAndroid(this)
        defaultConfig.targetSdk = 33
      }
      val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
      dependencies {

        add("testImplementation", libs.findLibrary("junit").get())
        add("androidTestImplementation", libs.findLibrary("androidx.test.ext.junit").get())
        add("androidTestImplementation", libs.findLibrary("espresso.core").get())

        add("implementation", libs.findLibrary("kotlinx.coroutines.android").get())

        add("implementation", libs.findLibrary("koin.android").get())
      }
    }
  }
}