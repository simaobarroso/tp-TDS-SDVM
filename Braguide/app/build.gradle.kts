plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    kotlin("kapt")
}

android {
    namespace = "com.example.braguide"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.braguide"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        //compose = true
        dataBinding = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    //kapt("androidx.room:room-compiler:2.3.0")
    implementation(libs.androidx.appcompat)
    implementation(libs.com.google.android.material)
    implementation(libs.androidx.constraintlayout)
    //implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.recyclerview)
    //implementation(libs.play.services.maps)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //retrofit
    implementation(libs.com.google.code.gson)
    implementation(libs.com.squareup.retrofit2.retrofit)
    implementation(libs.com.squareup.retrofit2.converter.gson)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)

    //picasso
    implementation(libs.com.squareup.picasso)
    implementation(libs.com.jakewharton.picasso)

    //Room components
    //implementation(libs.android.arch.persistence.room.runtime)
    //implementation("androidx.room:room-ktx:2.3.0")
    //annotationProcessor(libs.android.arch.persistence.room.compiler)

    // Room components
    implementation ("androidx.room:room-ktx:2.3.0")
    kapt ("androidx.room:room-compiler:2.3.0")
    androidTestImplementation( "androidx.room:room-testing:2.3.0")

    //api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${libs.versions.coroutines}")
    //api("org.jetbrains.kotlinx:kotlinx-coroutines-android:${libs.versions.coroutines}")

    // lifecycle components
    implementation(libs.androidx.lifecycle.extensions)
    kapt(libs.androidx.lifecycle.compiler)
    //annotationProcessor(libs.androidx.room.compiler)
    androidTestImplementation(libs.androidx.arch.core)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    kapt("com.android.databinding:compiler:3.0.0-beta6")
}