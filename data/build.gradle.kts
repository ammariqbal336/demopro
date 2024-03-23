plugins {
    id("com.android.library")
    alias(libs.plugins.jetbrainsKotlinAndroid)
    kotlin("kapt")
    id("com.google.dagger.hilt.android") version "2.44" apply false
    id ("dagger.hilt.android.plugin")


}

android {
    namespace = "com.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        targetSdk = 34

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    implementation(project(":domain"))

    //    dagger hilt
    implementation (libs.hilt.android)
    kapt (libs.hilt.compiler)

    //    retrofit
    implementation (libs.retrofit)
    implementation (libs.converter.gson)

    implementation(libs.kotlin.stdlib.jdk7)
    implementation (libs.kotlinx.coroutines.android)


            //    moshi
    implementation (libs.moshi)
    kapt (libs.moshi.kotlin.codegen)

    implementation (libs.kotlin.stdlib)
    implementation (libs.library)

    //chuker network monitoring tools

}

kapt {
    correctErrorTypes = true
    javacOptions {
        option("-Adagger.hilt.android.internal.disableAndroidSuperclassValidation=true")
    }
}

