plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.platform_education"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.platform_education"
        minSdk = 24
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
//    repositories {
//        google()
//        jcenter()
//        mavenCentral() // You may already have this, but it's good to check
//    }
}


    dependencies {
        implementation ("androidx.core:core-ktx:1.9.0")
        implementation ("androidx.appcompat:appcompat:1.6.1")
        implementation ("com.google.android.material:material:1.10.0") // Assurez-vous que vous utilisez la dernière version de Material Components
        implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
        implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")
        implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.4.0")
        implementation ("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.4.0")
        implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")
        implementation ("androidx.lifecycle:lifecycle-viewmodel:2.4.0")
        implementation ("androidx.lifecycle:lifecycle-common-java8:2.4.0")
        implementation ("androidx.room:room-runtime:2.3.0")
        //kapt ("androidx.room:room-compiler:2.3.0")
        implementation ("androidx.room:room-ktx:2.3.0")
        testImplementation ("junit:junit:4.13.2")
        androidTestImplementation ("androidx.test.ext:junit:1.1.5")
        androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
        implementation ("com.squareup.retrofit2:retrofit:2.9.0")
        implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
        implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
        implementation ("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")

    }
