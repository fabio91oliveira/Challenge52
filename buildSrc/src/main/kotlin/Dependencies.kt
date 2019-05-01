@file:Suppress("unused")

import org.gradle.api.JavaVersion

object Config {
    val javaVersion = JavaVersion.VERSION_1_8

    const val sdk_tools_linux = "4333796"
    const val minSdk = 21
    const val compileSdk = 28
    const val targetSdk = 28
    const val versionCode = 1
    const val versionName = "1.0"
    const val buildTools = "28.0.3"
    const val applicationId = "oliveira.fabio.challenge52"

    const val runner = "androidx.test.runner.AndroidJUnitRunner"
    const val orchestrator = "ANDROIDX_TEST_ORCHESTRATOR"
}

object Versions {
    const val kotlin = "1.3.21"
    const val android_gradle_plugin = "3.4.0"

    const val retrofit = "2.4.0"
    const val retrofit_coroutines_adapter = "0.9.2"
    const val okhttp_logging_interceptor = "3.13.1"

    const val support = "1.0.2"
    const val material = "1.0.0"
    const val recyclerview = "1.0.0"
    const val constraint_layout = "1.1.3"

    const val lifecycle = "2.0.0"

    const val coroutines = "1.1.1"
    const val resultcore = "2.1.0"
    const val resultcoroutines = "2.0.0"
    const val room = "2.0.0"

    const val koin = "1.0.2"
}

object Deps {
    const val tools_android_gradle_plugin = "com.android.tools.build:gradle:${Versions.android_gradle_plugin}"
    const val tools_kotlin_plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"

    const val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    const val retrofit_runtime = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val retrofit_gson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    const val retrofit_coroutines =
        "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:${Versions.retrofit_coroutines_adapter}"
    const val okhttp_logging_interceptor =
        "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp_logging_interceptor}"

    const val support_app_compat = "androidx.appcompat:appcompat:${Versions.support}"
    const val support_material = "com.google.android.material:material:${Versions.material}"
    const val support_recyclerview = "androidx.recyclerview:recyclerview:${Versions.recyclerview}"
    const val support_constraint_layout = "androidx.constraintlayout:constraintlayout:${Versions.constraint_layout}"

    const val lifecycle_extensions = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"
    const val lifecycle_runtime = "androidx.lifecycle:lifecycle-runtime:${Versions.lifecycle}"
    const val lifecycle_compiler = "androidx.lifecycle:lifecycle-compiler:${Versions.lifecycle}"

    const val coroutines_core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val coroutines_android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"

    const val result_core = "com.github.kittinunf.result:result:${Versions.resultcore}"
    const val result_coroutines = "com.github.kittinunf.result:result-coroutines:${Versions.resultcoroutines}"

    const val room_runtime = "androidx.room:room-runtime:${Versions.room}"
    const val room_compiler = "androidx.room:room-compiler:${Versions.room}"

    const val koin_android = "org.koin:koin-android:${Versions.koin}"
    const val koin_lifecycle = "org.koin:koin-android-scope:${Versions.koin}"
    const val koin_viewmodel = "org.koin:koin-android-viewmodel:${Versions.koin}"

    // Test
    // koin.test = "org.koin:koin-test:$versions.koin"
}

object Repo {
    const val jitpack = "https://jitpack.io"
}