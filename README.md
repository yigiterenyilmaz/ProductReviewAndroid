# Product Review Android App

A modern Android application for browsing products, reading reviews, and writing your own reviews. Built with Jetpack Compose and following Clean Architecture principles.

## Features

- **Product Listing** - Browse products in a grid layout with search and category filtering
- **Product Details** - View product information, ratings, and AI-generated review summaries
- **Reviews** - Read reviews with rating breakdown, filter by star rating, and write your own reviews
- **Wishlist** - Save favorite products locally (works offline)
- **AI Assistant** - Chat with AI about product reviews
- **Dark Mode** - Theme support with system settings integration
- **Notifications** - View app notifications

## Screenshots

<!-- Add your screenshots here -->

## Architecture

The app follows **MVVM + Clean Architecture** pattern:

```
┌─────────────────────────────────────────────────────────────┐
│                        UI Layer                             │
│  ┌──────────┐   ┌───────────┐                              │
│  │ Screens  │ → │ ViewModels│                              │
│  │(Compose) │   │           │                              │
│  └──────────┘   └───────────┘                              │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│                      Domain Layer                           │
│  ┌──────────────┐                                          │
│  │ Domain Models│                                          │
│  └──────────────┘                                          │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│                       Data Layer                            │
│  ┌────────────┐   ┌────────────┐   ┌──────────┐           │
│  │ Repository │ → │ ApiService │ → │ Retrofit │           │
│  └────────────┘   └────────────┘   └──────────┘           │
│        ↓                                                    │
│  ┌──────────┐                                              │
│  │   DAO    │ → Room Database (Local Storage)              │
│  └──────────┘                                              │
└─────────────────────────────────────────────────────────────┘
```

## Tech Stack

| Category | Technology |
|----------|------------|
| UI | Jetpack Compose, Material 3 |
| Architecture | MVVM, Clean Architecture |
| Dependency Injection | Hilt |
| Networking | Retrofit, OkHttp, Gson |
| Local Database | Room |
| Image Loading | Coil |
| Async Operations | Kotlin Coroutines, Flow, StateFlow |
| Navigation | Navigation Compose |
| Preferences | DataStore |
| Testing | JUnit, MockK, Espresso |

## Project Structure

```
app/src/main/java/com/productreview/
├── data/
│   ├── api/                 # API service interfaces
│   ├── local/               # Room database, DAOs
│   ├── model/               # API response models
│   ├── repository/          # Repository implementations
│   └── preferences/         # DataStore preferences
├── di/                      # Hilt modules (Network, Database)
├── domain/
│   └── model/               # Domain models
├── ui/
│   ├── components/          # Reusable Compose components
│   ├── screens/             # App screens
│   ├── theme/               # Theme, colors, typography
│   └── viewmodel/           # ViewModels
└── MainActivity.kt          # Entry point
```

## Requirements

- Android Studio Hedgehog (2023.1.1) or newer
- Minimum SDK: 24 (Android 7.0)
- Target SDK: 34 (Android 14)
- JDK 17

## Setup

1. Clone the repository
```bash
git clone <repository-url>
```

2. Open the project in Android Studio

3. Sync Gradle files

4. Run the app on an emulator or physical device

## Backend API

The app connects to a Spring Boot backend deployed on Heroku:
```
https://product-review-app-solarityai-a391ad53d79a.herokuapp.com/
```

### API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/products` | Get all products (with pagination) |
| GET | `/api/products/{id}` | Get product by ID |
| GET | `/api/products/{id}/reviews` | Get reviews for a product |
| POST | `/api/products/{id}/reviews` | Add a new review |
| PUT | `/api/products/reviews/{id}/helpful` | Mark review as helpful |
| POST | `/api/products/{id}/chat` | Chat with AI about product |

## Key Features Explained

### Optimistic UI
When submitting a review, the app immediately shows the review in the list without waiting for the server response. If the server request fails, the review is removed and an error message is shown.

### Wishlist (Offline Support)
Wishlist data is stored locally using Room database. Users can add/remove products from wishlist and access them without internet connection.

### AI Summary
Each product has an AI-generated summary of its reviews, highlighting common praise and complaints.

## Building APK

1. In Android Studio: `Build → Generate Signed Bundle / APK → APK`
2. Or for debug APK: `Build → Build Bundle(s) / APK(s) → Build APK(s)`

The APK will be generated at: `app/build/outputs/apk/debug/app-debug.apk`

## Testing

Run unit tests:
```bash
./gradlew test
```

Run instrumented tests:
```bash
./gradlew connectedAndroidTest
```

## Future Improvements

- User Authentication (JWT)
- Image Upload for reviews
- Push Notifications
- E-commerce Redirection
- Multi-language Support
- AI-Powered Price Prediction

## License

This project is part of an internship program.
