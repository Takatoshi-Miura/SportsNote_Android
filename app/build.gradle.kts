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
    compileSdk = 34

    defaultConfig {
        applicationId = "com.it6210.sportsnote"
        minSdk = 29
        targetSdk = 34
        versionCode = 7
        versionName = "1.0.0"

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

    // Composeの依存関係
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation("androidx.compose.ui:ui:1.6.0")
    implementation("androidx.compose.material:material:1.6.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.6.0")
    implementation("androidx.navigation:navigation-compose:2.7.3")
    implementation("androidx.compose.foundation:foundation:1.6.0-alpha01")
    // Firebase関連
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-firestore")
    implementation(libs.firebase.auth.ktx)
    // カレンダー
    implementation("com.kizitonwose.calendar:compose:2.6.1")
    // AdMob
    implementation("com.google.android.gms:play-services-ads:22.6.0")
    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.espresso.core)

    // テスト用依存関係（オプション）
    debugImplementation("androidx.compose.ui:ui-tooling:1.6.0")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.6.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.6.0")
}
