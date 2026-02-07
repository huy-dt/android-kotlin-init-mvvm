plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)

    // alias(libs.plugins.hilt)
    // alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "com.huydt.uikit"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    // Có ui nhớ
    buildFeatures {
        compose = true
    }
    
    composeOptions {
        // Phù hợp Kotlin 1.9.22
        kotlinCompilerExtensionVersion = "1.5.10"
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    // kapt {
    //     correctErrorTypes = true
    // }
}

dependencies {
    
    implementation("javax.inject:javax.inject:1")

    // ⭐ BẮT BUỘC: Compose BOMdependencies {
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.bundles.core)
    implementation(libs.bundles.compose.core)
    implementation(libs.bundles.compose.lifecycle)
    implementation(libs.bundles.compose.material3)


    // implementation(libs.hilt.android)
    // kapt(libs.hilt.compiler)
    // implementation(libs.androidx.hilt.navigation.compose)

}

