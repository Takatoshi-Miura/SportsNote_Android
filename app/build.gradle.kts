plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.org.jetbrains.kotlin.kapt)
    alias(libs.plugins.ktlint)
    id("realm-android")
    id("com.google.gms.google-services")
}

ktlint {
    filter {
        exclude { it.file.path.contains("generated") }
    }
}

android {
    namespace = "com.it6210.sportsnote"
    compileSdk = 35
    ndkVersion = "28.0.12674087"

    defaultConfig {
        applicationId = "com.it6210.sportsnote"
        minSdk = 29
        targetSdk = 35
        versionCode = 8
        versionName = "1.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            // リリースビルドでコード難読化と最適化を有効にする
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
        debug {
            // デバッグビルドではクラッシュレポートの取得を容易にするため最適化を無効化
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    // Composeの設定を追加
    buildFeatures {
        compose = true // Composeを有効化
        viewBinding = true // 既存の設定を保持
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
    // 不要なリソースやライブラリを除去する設定
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/LICENSE*"
            excludes += "DebugProbesKt.bin"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.accompanist.swiperefresh)
    implementation(libs.compose.reorderable)
    implementation(libs.material)
    implementation(libs.material3.android)

    // Compose関連
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.navigation.compose)

    // Firebase関連
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth.ktx)

    // カレンダー
    implementation(libs.calendar.compose)

    // AdMob
    implementation(libs.play.services.ads)

    // テスト用
    implementation(libs.androidx.espresso.core)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}
