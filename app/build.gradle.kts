plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.gunmedia"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.gunmedia"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.cardview)
    implementation(libs.navigation.fragment.v277)
    implementation(libs.navigation.ui.v277)
}

dependencies {
    implementation(libs.firebase.database)// Основные зависимости AndroidX
    implementation(libs.appcompat.v161)
    implementation(libs.constraintlayout.v214)
    implementation(libs.core.ktx)
    implementation(libs.fragment.ktx)

    // Навигация
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)

    // Material Design
    implementation(libs.material.v1100)

    // Firebase
    implementation(libs.firebase.auth)
    implementation(platform(libs.firebase.bom.v3280))
    implementation(libs.google.firebase.analytics)
    implementation(libs.google.firebase.auth)
    implementation(libs.fragment)
    implementation(libs.glide)
    annotationProcessor(libs.compiler)

}
