plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)

    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "com.xxx.app.feature_user"
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

    kapt {
        correctErrorTypes = true
    }
}

dependencies {

    implementation(project(":core"))
    implementation(project(":database"))
    implementation(project(":uikit"))

    // ⭐ BẮT BUỘC: Compose BOM
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.core.ktx)

    implementation(libs.bundles.core)
    implementation(libs.bundles.compose.core)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // vì bạn dùng Material3
    implementation(libs.bundles.compose.material3)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

}

