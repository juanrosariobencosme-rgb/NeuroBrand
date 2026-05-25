# NeuroBand - Emotional Well-being Application

A premium Android application for emotional well-being, biometric monitoring, and AI-powered emotional analysis, connected to a smart bracelet (NeuroBand).

## 🌟 Features

### Core Features
- **Real-time Biometric Monitoring**: Track heart rate, respiration, stress levels, and more via BLE
- **AI Emotional Engine**: Advanced machine learning for emotional state analysis and prediction
- **Smart Recommendations**: Personalized breathing exercises, meditation, and haptic feedback
- **Emotional Trends**: Visualize emotional patterns over time with beautiful charts
- **Privacy-First**: End-to-end encryption and complete user control over data

### Key Modules
1. **Dashboard**: Real-time biometric data display with emotional state analysis
2. **BLE Connection**: Seamless Bluetooth Low Energy connection to NeuroBand device
3. **AI Insights**: Predictive emotional analysis and intelligent recommendations
4. **Trends**: Historical emotional data visualization and pattern analysis
5. **Settings**: Customizable preferences for haptic, audio, and privacy settings

## 🎨 Design Philosophy

NeuroBand embodies a futuristic minimalist aesthetic with:
- **Color Palette**: Soft blues, mint greens, lavender, warm whites
- **Style**: Glassmorphism, soft glows, floating cards, smooth animations
- **Typography**: Inter, SF Pro Display, or Satoshi
- **Experience**: Calm, human technology, positive future, security, emotional intelligence

## 🏗️ Architecture

### Technology Stack

**Frontend Mobile**
- Kotlin
- Android Studio
- Jetpack Compose
- Material 3
- Hilt (Dependency Injection)
- Coroutines

**Backend**
- Supabase
- PostgreSQL
- Realtime API
- Row Level Security (RLS)

**Connectivity**
- Bluetooth Low Energy (BLE)
- Nordic BLE Library

**AI/ML**
- Custom Emotional Engine
- Biometric Analysis
- Predictive Models

**Services**
- Haptic Feedback
- Audio Feedback

### Architecture Pattern

The application follows Clean Architecture principles with clear separation of concerns:

```
┌─────────────────────────────────────────┐
│         Presentation Layer              │
│  (UI Screens, ViewModels, Navigation)   │
└─────────────────────────────────────────┘
                    │
┌─────────────────────────────────────────┐
│           Domain Layer                   │
│      (EmotionalEngine, Use Cases)       │
└─────────────────────────────────────────┘
                    │
┌─────────────────────────────────────────┐
│            Data Layer                    │
│  (BLE, Supabase, Repositories)         │
└─────────────────────────────────────────┘
```

## 📁 Project Structure

```
App_NeuroBand/
├── app/
│   ├── src/main/
│   │   ├── java/com/neuroband/app/
│   │   │   ├── ai/
│   │   │   │   └── EmotionalEngine.kt
│   │   │   ├── data/
│   │   │   │   ├── ble/
│   │   │   │   │   ├── BleManager.kt
│   │   │   │   │   ├── BleScanner.kt
│   │   │   │   │   └── BleRepository.kt
│   │   │   │   └── supabase/
│   │   │   │       ├── SupabaseClient.kt
│   │   │   │       └── repositories/
│   │   │   ├── di/
│   │   │   │   └── AppModule.kt
│   │   │   ├── navigation/
│   │   │   │   └── NeuroBandNavigation.kt
│   │   │   ├── services/
│   │   │   │   ├── HapticFeedbackService.kt
│   │   │   │   └── AudioFeedbackService.kt
│   │   │   ├── ui/
│   │   │   │   ├── components/
│   │   │   │   ├── screens/
│   │   │   │   │   ├── connection/
│   │   │   │   │   ├── dashboard/
│   │   │   │   │   ├── trends/
│   │   │   │   │   ├── insights/
│   │   │   │   │   └── settings/
│   │   │   │   └── theme/
│   │   │   ├── MainActivity.kt
│   │   │   └── NeuroBandApplication.kt
│   │   ├── res/
│   │   └── AndroidManifest.xml
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── database/
│   └── supabase_schema.sql
├── docs/
│   └── UML_Documentation.md
├── build.gradle.kts
└── settings.gradle.kts
```

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK 34
- A physical Android device with BLE support or Android emulator
- Supabase account (for backend services)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/juanrosariobencosme-rgb/NeuroBrand.git
   cd NeuroBrand/App_NeuroBand
   ```

2. **Configure Supabase**
   - Create a new project in [Supabase](https://supabase.com)
   - Run the SQL schema from `database/supabase_schema.sql`
   - Update `SupabaseClient.kt` with your credentials:
     ```kotlin
     private val supabaseUrl = "https://your-project.supabase.co"
     private val supabaseKey = "your-anon-key"
     ```

3. **Build the project**
   ```bash
   ./gradlew build
   ```

4. **Run on device/emulator**
   ```bash
   ./gradlew installDebug
   ```

### BLE Device Configuration

Update the UUIDs in `BleManager.kt` to match your actual device specifications:

```kotlin
companion object {
    val SERVICE_UUID: UUID = UUID.fromString("YOUR_SERVICE_UUID")
    val HEART_RATE_CHARACTERISTIC_UUID: UUID = UUID.fromString("YOUR_HEART_RATE_UUID")
    // ... other characteristics
}
```

## 📊 Database Schema

The application uses PostgreSQL via Supabase with the following main tables:

- **users**: User profiles and preferences
- **devices**: Connected NeuroBand devices
- **biometric_data**: Real-time biometric readings
- **emotional_records**: Emotional state history
- **ai_predictions**: AI-generated predictions
- **recommendations**: Personalized recommendations
- **notifications**: User notifications
- **settings**: User preferences
- **sessions**: Activity sessions
- **privacy_logs**: Privacy audit logs

See `database/supabase_schema.sql` for complete schema definition.

## 🔒 Security

### Privacy Features
- End-to-end encryption for all data transmission
- Row Level Security (RLS) in Supabase
- Local encrypted storage for sensitive data
- Granular permission controls
- Complete data deletion capability
- Transparent privacy logging

### Permissions Required
- `BLUETOOTH` and `BLUETOOTH_CONNECT`: For device connection
- `BLUETOOTH_SCAN`: For device discovery
- `ACCESS_FINE_LOCATION`: Required for BLE scanning on Android
- `INTERNET`: For Supabase communication
- `VIBRATE`: For haptic feedback
- `POST_NOTIFICATIONS`: For notifications

## 🤖 AI Emotional Engine

The Emotional Engine analyzes biometric data to:

1. **Calculate Stress Score**: Based on heart rate, GSR, respiration, and historical trends
2. **Determine Emotional State**: Classifies into Calm, Focused, Relaxed, Moderate, Alert, or Stressed
3. **Generate Recommendations**: Provides personalized breathing exercises, meditation, or haptic feedback
4. **Predict Future States**: Uses historical data to predict emotional states in the next 2 hours

### Algorithm Weights
- Heart Rate: 40%
- GSR (Galvanic Skin Response): 25%
- Respiration Rate: 20%
- Historical Trend: 15%

## 🎯 Usage

### Connecting to Device
1. Open the app
2. Tap "Escanear Dispositivos"
3. Select your NeuroBand from the list
4. Wait for connection confirmation

### Monitoring Biometrics
- View real-time data on the Dashboard
- Check emotional state analysis
- Review stress and energy levels
- Track heart rate variability

### Following Recommendations
- Receive AI-powered recommendations
- Start guided breathing exercises
- Activate haptic calm mode
- Play therapeutic sounds

### Viewing Trends
- Access the Trends screen
- View weekly emotional patterns
- Analyze stress trends
- Review AI insights

## 📱 Screens

### Connection Screen
- BLE device scanning and pairing
- Connection status display
- Device battery level

### Dashboard
- Real-time biometric data
- Emotional state indicator
- Quick access to recommendations
- Bottom navigation

### AI Insights
- Neural synthesis visualization
- Biometric alerts
- Emotional projections
- AI model information

### Trends
- Weekly emotional trends
- Stress level charts
- Emotional breakdown
- AI-generated insights

### Settings
- Device configuration
- App preferences
- Privacy controls
- Theme selection

## 🧪 Testing

```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Run lint checks
./gradlew lint
```

## 📝 Documentation

- [UML Documentation](docs/UML_Documentation.md) - Complete system architecture diagrams
- [Database Schema](database/supabase_schema.sql) - Supabase database structure

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is proprietary software. All rights reserved.

## 👥 Team

- **Architecture & Design**: Senior Software Architect
- **Development**: Android Development Team
- **AI/ML**: Machine Learning Engineers
- **UX/UI**: Futurist Design Team

## 🙏 Acknowledgments

- Inspired by Apple Health, Oura, Headspace, Neuralink, Calm, Fitbit, Nothing OS, and Humane AI
- Built with Jetpack Compose and Material 3
- Powered by Supabase
- BLE connectivity with Nordic Semiconductor libraries

## 📞 Support

For support, please contact the development team or open an issue in the repository.

---

**NeuroBand** - The Future of Emotional Well-being 🧠✨
