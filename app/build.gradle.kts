import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    // Khi dùng Hilt
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.kapt)
}

/**
 * =====================================================
 * READ local.properties (KHÔNG COMMIT GIT)
 * =====================================================
 */
val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        file.inputStream().use { load(it) }
        logger.lifecycle("local.properties loaded from ${file.absolutePath}")
    } else {
        logger.lifecycle("local.properties NOT FOUND at ${file.absolutePath}")
    }
}

fun prop(key: String): String? =
    localProperties.getProperty(key)

/**
 * =====================================================
 * ANDROID CONFIG
 * =====================================================
 */
android {

    namespace = "com.xxx.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.xxx.app"
        minSdk = 24
        targetSdk = 35

        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    /**
     * =====================================================
     * SIGNING CONFIG
     * → CHỈ TẠO KHI CÓ KEYSTORE
     * =====================================================
     */
    signingConfigs {
        prop("RELEASE_STORE_FILE")?.let { storePath ->
            create("release") {
                storeFile = rootProject.file(storePath)
                storePassword = prop("RELEASE_STORE_PASSWORD")
                keyAlias = prop("RELEASE_KEY_ALIAS")
                keyPassword = prop("RELEASE_KEY_PASSWORD")
            }
        }
    }

    /**
     * =====================================================
     * BUILD TYPES
     * =====================================================
     */
    buildTypes {

        getByName("release") {
            isMinifyEnabled = true

            // ✅ CHỈ SIGN KHI SIGNING CONFIG TỒN TẠI
            signingConfigs.findByName("release")?.let {
                signingConfig = it
            }

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        getByName("debug") {
            isMinifyEnabled = false
        }
    }

    /**
     * =====================================================
     * COMPOSE
     * =====================================================
     */
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }

    /**
     * =====================================================
     * JAVA / KOTLIN
     * =====================================================
     */
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    // Khi dùng Hilt
    kapt {
        correctErrorTypes = true
    }
}

/**
 * =====================================================
 * DEPENDENCIES
 * =====================================================
 */
dependencies {

    implementation(project(":core"))
    implementation(project(":database"))
    implementation(project(":feature_auth"))
    implementation(project(":feature_user"))

    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.bundles.compose.core)
    implementation(libs.bundles.compose.material3)
    implementation(libs.bundles.compose.lifecycle)
    implementation(libs.bundles.compose.navigation)

    implementation(libs.bundles.room)

    implementation(libs.bundles.compose.preview)
    debugImplementation(libs.bundles.compose.debug)

    // ===============================
    // HILT (KHI CẦN)
    // ===============================
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    // implementation(libs.androidx.hilt.navigation.compose)
}
