# CLAUDE.md

このファイルは、このリポジトリでコードを操作するときのClaude Code (claude.ai/code) へのガイダンスを提供します。

## 言語設定
このプロジェクトは日本語プロジェクトです。Claude Codeは日本語で回答してください。

## プロジェクト概要

SportsNoteは、KotlinとJetpack Composeで構築されたAndroidスポーツノートアプリケーションです。
ユーザーが練習セッション、大会パフォーマンスの記録、目標設定、スポーツ活動に関連するタスク管理を行うことができます。

## 開発コマンド

### ビルドコマンド
```bash
# デバッグAPKをビルド
./gradlew assembleDebug

# リリースAPKをビルド
./gradlew assembleRelease

# 接続されたデバイスにデバッグAPKをインストール
./gradlew installDebug

# クリーンビルド
./gradlew clean
```

### テストコマンド
```bash
# ユニットテストを実行
./gradlew test

# インストルメンテーションテストを実行
./gradlew connectedAndroidTest
```

### コード品質
```bash
# ktlintフォーマットを実行
./gradlew ktlintFormat

# ktlintの問題をチェック
./gradlew ktlintCheck
```

### Firebase Hosting デプロイ
```bash
# プライバシーポリシーをデプロイ
cd hosting/privacyPolicy && firebase deploy

# 利用規約をデプロイ
cd hosting/termsOfService && firebase deploy

# 両方を一度にデプロイ（プロジェクトルートから実行）
cd hosting/privacyPolicy && firebase deploy && cd ../termsOfService && firebase deploy
```

**デプロイ後のURL:**
- プライバシーポリシー: https://sportsnote-privacy-policy.web.app
- 利用規約: https://sportsnote-terms-of-service.web.app

## プロジェクトアーキテクチャ

### 技術スタック
- **UIフレームワーク**: Jetpack Compose（メイン） + 従来のViews（レガシー）
- **アーキテクチャ**: Repositoryパターンを使ったMVVM
- **データベース**: Realm（ローカル） + Firebase Firestore（クラウド同期）
- **認証**: Firebase Auth
- **ナビゲーション**: Jetpack Navigation Compose
- **ビルドシステム**: Gradle with Kotlin DSL

### パッケージ構造
```
com.it6210.sportsnote/
├── model/               # RealmObjectを継承するデータモデル
│   └── manager/         # データ管理（RealmManager、FirebaseManagerなど）
├── ui/                  # UIコンポーネントと画面
│   ├── components/      # 再利用可能なComposeコンポーネント
│   ├── group/          # グループ管理画面
│   ├── note/           # ノート記録画面（フリー、練習、大会）
│   ├── task/           # タスク管理画面
│   ├── target/         # 目標設定画面
│   ├── measures/       # 対策画面
│   └── setting/        # 設定画面
├── viewModel/          # MVVMパターンのViewModel
└── utils/              # ユーティリティクラス
```

### 主要なアーキテクチャパターン
- **オフラインファースト**: RealmによるローカルストレージとFirebase同期
- **論理削除**: データ安全性のためのソフト削除パターン
- **自動保存**: 自動データ永続化
- **ハイブリッドUI**: 従来のViewsからComposeへの移行中

### データモデル
すべてのデータモデルは`RealmObject`を継承し、Firebase同期のために`Syncable`インターフェースを実装しています。主要エンティティ：
- `Group`: 組織コンテナ
- `Note`: 3種類（自由、練習、大会）
- `TaskData`: タスク管理
- `Target`: 目標追跡
- `Measures`: パフォーマンス指標
- `Memo`: 一般メモ

### データ管理
- **RealmManager**: ローカルデータベース操作
- **FirebaseManager**: クラウド同期と認証
- **SyncManager**: オフライン/オンラインデータ同期の調整
- **PreferencesManager**: アプリ設定と設定値

## アプリ設定
- **アプリケーションID**: `com.it6210.sportsnote`
- **最小SDK**: 29 (Android 10)
- **ターゲットSDK**: 35 (Android 15)
- **現在のバージョン**: 1.0.2 (versionCode: 11)
- **向き**: 縦向きのみ
- **言語**: 日本語（デフォルト）、英語

## Firebaseサービス
- Firestore（データベース）
- Authentication（認証）
- Analytics（分析）
- AdMob統合

## ビルド設定
- **Debug**: 難読化なし、デバッグが容易
- **Release**: 難読化 + リソース縮小有効
- **KtLint**: コードフォーマット強制
- **ProGuard**: リリースビルドで難読化ルール適用

## 開発上の注意点
- Composeを使用したシングルアクティビティアーキテクチャ
- 従来のViewsからComposeへの移行が進行中
- 依存関係管理にバージョンカタログ（libs.versions.toml）を使用
- ストア資産は`ストア申請用/`ディレクトリに配置
- 日本語と英語の両方をサポート