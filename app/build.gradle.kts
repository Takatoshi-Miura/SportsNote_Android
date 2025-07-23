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
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.accompanist.swiperefresh)
    implementation(libs.compose.reorderable)

    // Composeの依存関係 (API 35対応版)
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.compose.ui:ui:1.7.6")
    implementation("androidx.compose.material:material:1.7.6")
    implementation("androidx.compose.material:material-icons-core:1.7.6")
    implementation("androidx.compose.material:material-icons-extended:1.7.6")
    implementation("androidx.compose.material3:material3:1.3.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.6")
    implementation("androidx.navigation:navigation-compose:2.8.5")
    implementation("androidx.compose.foundation:foundation:1.7.6")
    // Firebase関連 (最新版)
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-firestore")
    implementation(libs.firebase.auth.ktx)
    // カレンダー
    implementation("com.kizitonwose.calendar:compose:2.6.1")
    // AdMob (最新版)
    implementation("com.google.android.gms:play-services-ads:23.6.0")
    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.espresso.core)
    implementation(libs.material3.android)

    // テスト用依存関係（オプション）
    debugImplementation("androidx.compose.ui:ui-tooling:1.7.6")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.7.6")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.7.6")
}
