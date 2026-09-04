import com.google.gms.googleservices.GoogleServicesPlugin.MissingGoogleServicesStrategy

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.google.devtools.ksp)
  alias(libs.plugins.roborazzi)
  alias(libs.plugins.secrets)
  alias(libs.plugins.google.services)
}

android {
  namespace = "com.example"
  compileSdk { version = release(36) { minorApiLevel = 1 } }

  defaultConfig {
    applicationId = "com.aistudio.logistics.distrib"
    minSdk = 24
    targetSdk = 36
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

//  signingConfigs {
//    create("release") {
//      val keystorePath = System.getenv("KEYSTORE_PATH") ?: "${rootDir}/my-upload-key.jks"
//      storeFile = file(keystorePath)
//      storePassword = System.getenv("STORE_PASSWORD")
//      keyAlias = "upload"
//      keyPassword = System.getenv("KEY_PASSWORD")
//    }
//    create("debugConfig") {
//      storeFile = file("${rootDir}/debug.keystore")
//      storePassword = "android"
//      keyAlias = "androiddebugkey"
//      keyPassword = "android"
//    }
//  }
  signingConfigs {

    create("release") {

      val keystorePath =
        System.getenv("KEYSTORE_PATH")
          ?: "${rootDir}/my-upload-key.jks"

      storeFile = file(
        keystorePath
      )

      storePassword =
        System.getenv("STORE_PASSWORD")

      keyAlias = "upload"

      keyPassword =
        System.getenv("KEY_PASSWORD")
    }
  }

  buildTypes {

    debug {

      // Android automatically uses
      // the default debug keystore
    }

    release {

      isMinifyEnabled = false

      signingConfig =
        signingConfigs.getByName("release")

      proguardFiles(
        getDefaultProguardFile(
          "proguard-android-optimize.txt"
        ),
        "proguard-rules.pro"
      )
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  buildFeatures {
    compose = false
    viewBinding = true
    buildConfig = true
  }
  testOptions { unitTests { isIncludeAndroidResources = true } }
  dependenciesInfo {
    includeInApk = false
    includeInBundle = true
  }
}

// Configure the Secrets Gradle Plugin to use .env and .env.example files
// to match the convention used in Web projects.
secrets {
  propertiesFileName = ".env"
  defaultPropertiesFileName = ".env.example"
  ignoreList.add("FIREBASE_APPCHECK_DEBUG_TOKEN")
}

googleServices { missingGoogleServicesStrategy = MissingGoogleServicesStrategy.WARN }

// Some unused dependencies are commented out below instead of being removed.
// This makes it easy to add them back in the future if needed.
dependencies {
  implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
  implementation(libs.material)
  implementation(libs.androidx.constraintlayout)
  implementation(libs.androidx.recyclerview)
  implementation(libs.androidx.cardview)
  implementation(libs.androidx.fragment)
  implementation(libs.androidx.lifecycle.viewmodel)
  implementation(libs.androidx.lifecycle.livedata)
  implementation(libs.androidx.core.ktx)

  testImplementation(libs.junit)
}
