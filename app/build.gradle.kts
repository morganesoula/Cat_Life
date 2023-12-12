import com.android.build.api.variant.BuildConfigField
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.util.Properties

plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("org.jetbrains.kotlin.kapt")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.parcelize")
    alias(libs.plugins.ktlint)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.ksp)
}

sqldelight {
    databases {
        create("CatLifeDatabase") {
            packageName = "com.msoula"
        }
    }
}

fun getApiKey(): String {
    val propFile = rootProject.file("./local.properties")

    if (propFile.exists()) {
        val properties = Properties()
        properties.load(FileInputStream(propFile))
        return properties.getProperty("MAPS_API_KEY")
    } else {
        throw FileNotFoundException()
    }
}

androidComponents {
    onVariants {
        it.buildConfigFields.put(
            "MAPS_API_KEY", BuildConfigField(
                "String", getApiKey(), "get map key"
            )
        )
    }
}

android {
    signingConfigs {
        getByName("debug") {
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
            storeFile = file("/Users/morganesoula/.android/debug.keystore")
        }
        create("release") {
            keyAlias = System.getenv("RELEASE_CATLIFE_KEY_ALIAS")
            keyPassword = System.getenv("RELEASE_CATLIFE_KEY_PASSWORD")
            storePassword = System.getenv("RELEASE_CATLIFE_STORE_PASSWORD")
            storeFile = file(System.getenv("RELEASE_CATLIFE_STORE_FILE"))
        }
    }
    namespace = "com.msoula.catlife"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.msoula.catlife"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }

        resourceConfigurations += listOf("en", "fr")
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    buildTypes {
        debug {
            isDebuggable = true
            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
            versionNameSuffix = "debug"
            signingConfig = signingConfigs.getByName("debug")
        }

        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )

            signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
    }
}

dependencies {
    implementation(libs.test.runner)
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.ui)
    debugImplementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.kotlinx.datetime)

    // Test
    implementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    testImplementation(libs.truth)
    androidTestImplementation(libs.hilt.android.testing)
    debugImplementation(libs.androidx.ui.tooling)
    kaptTest(libs.dagger.hilt.android.compiler)
    kaptAndroidTest(libs.dagger.hilt.android.compiler)
    testImplementation(libs.turbine)

    // Android Test
    androidTestImplementation(libs.runner)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.test.manifest)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.rules)
    androidTestImplementation(libs.androidx.uiautomator)
    androidTestImplementation(libs.core.ktx)

    androidTestImplementation(libs.test.runner)
    androidTestUtil(libs.androidx.orchestrator)

    // Lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // Coroutine
    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.coroutines.android)
    testImplementation(libs.kotlinx.coroutines.test)

    // Navigation compose
    implementation(libs.androidx.navigation.compose)
    implementation(libs.accompanist.navigation.animation)

    // Compose - Integration with activities
    implementation(libs.androidx.activity.compose)
    implementation(libs.material)
    implementation(libs.androidx.material)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.runtime.livedata)

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0") {
        exclude(module = "okhttp")
    }
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.urlconnection)
    implementation(libs.okhttp3.logging.interceptor)
    implementation(libs.gson)

    // OkHTTP
    implementation(libs.okhttp3.logging.interceptor)

    // SQLDelight
    implementation(libs.primitive.adapters)
    implementation(libs.android.driver)
    implementation(libs.coroutines.extensions)
    implementation(libs.sqlite.driver)

    // Hilt -- Bug in 2.47
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler.v246)
    implementation(libs.androidx.hilt.navigation.compose)
    testImplementation(libs.hilt.android.testing.v246)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")

    // Coil
    implementation(libs.coil.compose)

    // Kalendar & Datepicker
    implementation(libs.kalendar)
    implementation(libs.core)
    implementation(libs.calendar)
    implementation(libs.clock)

    // Maps
    implementation(libs.maps.compose)
    implementation(libs.play.services.maps)
    implementation(libs.places)

    // Compose Destination
    implementation(libs.animations.core)
    ksp(libs.ksp)
}