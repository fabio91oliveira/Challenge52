@file:Suppress("unused")

import org.gradle.api.JavaVersion

object Config {
    val javaVersion = JavaVersion.VERSION_1_8

    const val minSdk = 21
    const val compileSdk = 28
    const val targetSdk = 28
    const val versionCode = 1
    const val versionName = "1.0"
    const val buildTools = "28.0.3"
    const val applicationId = "oliveira.fabio.challenge52"

    const val srcDirMain = "src/main/kotlin"
    const val srcDirTest = "src/test/kotlin"
    const val srcDirAndroidTest = "src/androidTest/kotlin"

    const val runner = "androidx.test.runner.AndroidJUnitRunner"
    const val orchestrator = "ANDROIDX_TEST_ORCHESTRATOR"
}

object Versions {
    const val kotlin = "1.3.50"
    const val android_gradle_plugin = "3.4.0"

    const val support = "1.1.0"
    const val material = "1.0.0"
    const val recyclerview = "1.0.0"
    const val constraint_layout = "1.1.3"
    const val viewpager2 = "1.0.0"

    const val lifecycle = "2.2.0"

    const val coroutines = "1.3.2"
    const val resultcore = "2.2.0"
    const val resultcoroutines = "2.2.0"
    const val room = "2.2.2"

    const val koin = "2.0.1"
    const val timber = "4.7.1"
}

object Deps {
    const val tools_android_gradle_plugin =
        "com.android.tools.build:gradle:${Versions.android_gradle_plugin}"
    const val tools_kotlin_plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"

    const val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"

    const val support_app_compat = "androidx.appcompat:appcompat:${Versions.support}"
    const val support_material = "com.google.android.material:material:${Versions.material}"
    const val support_recyclerview = "androidx.recyclerview:recyclerview:${Versions.recyclerview}"
    const val support_constraint_layout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraint_layout}"
    const val viewpager2 = "androidx.viewpager2:viewpager2:${Versions.viewpager2}"

    const val lifecycle_viewmodel =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val lifecycle_livedata = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
    const val lifecycle_compiler = "androidx.lifecycle:lifecycle-compiler:${Versions.lifecycle}"

    const val coroutines_core =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val coroutines_android =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"

    const val result_core = "com.github.kittinunf.result:result:${Versions.resultcore}"
    const val result_coroutines =
        "com.github.kittinunf.result:result-coroutines:${Versions.resultcoroutines}"

    const val room_runtime = "androidx.room:room-runtime:${Versions.room}"
    const val room_compiler = "androidx.room:room-compiler:${Versions.room}"

    const val koin_android = "org.koin:koin-android:${Versions.koin}"
    const val koin_lifecycle = "org.koin:koin-android-scope:${Versions.koin}"
    const val koin_viewmodel = "org.koin:koin-android-viewmodel:${Versions.koin}"

    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"

    // Test
    // koin.test = "org.koin:koin-test:$versions.koin"
}

object Repo {
    const val jitpack = "https://jitpack.io"
}