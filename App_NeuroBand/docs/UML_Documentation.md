# NeuroBand - UML Documentation

## System Architecture Overview

NeuroBand is a comprehensive emotional well-being application that integrates Bluetooth Low Energy (BLE) connectivity, real-time biometric monitoring, AI-powered emotional analysis, and cloud-based data storage using Supabase.

---

## 1. Use Case Diagram

### Actors
- **User**: Primary user of the NeuroBand application
- **NeuroBand Device**: Smart bracelet/wearable device
- **System**: Backend services (Supabase, AI Engine)

### Use Cases

#### User Use Cases
```
┌─────────────────────────────────────────────────────────────┐
│                        USER                                 │
└─────────────────────────────────────────────────────────────┘
                            │
        ┌───────────────────┼───────────────────┐
        │                   │                   │
        ▼                   ▼                   ▼
┌───────────────┐  ┌───────────────┐  ┌───────────────┐
│ Connect Device│  │ View Dashboard│  │ View Trends   │
└───────────────┘  └───────────────┘  └───────────────┘
        │                   │                   │
        ▼                   ▼                   ▼
┌───────────────┐  ┌───────────────┐  ┌───────────────┐
│ Monitor       │  │ Receive AI    │  │ Configure     │
│ Biometrics    │  │ Insights      │  │ Settings      │
└───────────────┘  └───────────────┘  └───────────────┘
        │                   │                   │
        ▼                   ▼                   ▼
┌───────────────┐  ┌───────────────┐  ┌───────────────┐
│ Receive       │  │ Get           │  │ Manage        │
│ Recommendations│  │ Predictions  │  │ Privacy       │
└───────────────┘  └───────────────┘  └───────────────┘
```

#### System Use Cases
```
┌─────────────────────────────────────────────────────────────┐
│                       SYSTEM                                │
└─────────────────────────────────────────────────────────────┘
                            │
        ┌───────────────────┼───────────────────┐
        │                   │                   │
        ▼                   ▼                   ▼
┌───────────────┐  ┌───────────────┐  ┌───────────────┐
│ Process       │  │ Store         │  │ Analyze       │
│ Biometric Data│  │ User Data     │  │ Emotional     │
└───────────────┘  └───────────────┘  │ Patterns      │
                                       └───────────────┘
```

---

## 2. Class Diagram

### Core Classes

#### Data Layer
```
┌─────────────────────────────────────────────────────────────┐
│                    BleManager                               │
├─────────────────────────────────────────────────────────────┤
│ - bluetoothGatt: BluetoothGatt                              │
│ - connectionState: StateFlow<ConnectionState>              │
│ - biometricData: StateFlow<BiometricData?>                  │
│ - batteryLevel: StateFlow<Int?>                            │
├─────────────────────────────────────────────────────────────┤
│ + connect(device: BluetoothDevice)                          │
│ + disconnect()                                              │
│ + sendVibrationCommand(intensity: Int)                       │
│ + getGattCallback(): BluetoothGattCallback                  │
└─────────────────────────────────────────────────────────────┘
                            │
                            │ uses
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                    BleScanner                               │
├─────────────────────────────────────────────────────────────┤
│ - bluetoothLeScanner: BluetoothLeScanner                    │
│ - isScanning: StateFlow<Boolean>                            │
│ - discoveredDevices: StateFlow<List<BluetoothDevice>>       │
├─────────────────────────────────────────────────────────────┤
│ + startScan(serviceUuid: ParcelUuid?)                       │
│ + stopScan()                                                │
│ + isBluetoothEnabled(): Boolean                             │
└─────────────────────────────────────────────────────────────┘
                            │
                            │ uses
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                    BleRepository                            │
├─────────────────────────────────────────────────────────────┤
│ - bleManager: NeuroBandBleManager                          │
│ - bleScanner: BleScanner                                   │
├─────────────────────────────────────────────────────────────┤
│ + connectionState: Flow<ConnectionState>                   │
│ + biometricData: Flow<BiometricData?>                      │
│ + batteryLevel: Flow<Int?>                                 │
│ + scanStatus: Flow<ScanStatus>                             │
│ + connectToDevice(device: BluetoothDevice)                 │
│ + disconnect()                                             │
└─────────────────────────────────────────────────────────────┘
```

#### AI Engine
```
┌─────────────────────────────────────────────────────────────┐
│                 EmotionalEngine                             │
├─────────────────────────────────────────────────────────────┤
│ - historicalData: MutableList<BiometricData>               │
│ - emotionalState: StateFlow<EmotionalAnalysis>              │
│ - recommendations: StateFlow<List<Recommendation>>         │
│ - prediction: StateFlow<EmotionalPrediction?>              │
├─────────────────────────────────────────────────────────────┤
│ + analyzeBiometricData(biometricData: BiometricData)        │
│ - performAnalysis(): EmotionalAnalysis                      │
│ - calculateStressScore(): Int                               │
│ - calculateEnergyScore(): Int                               │
│ - generateRecommendations(): List<Recommendation>          │
│ - generatePrediction(): EmotionalPrediction?                │
└─────────────────────────────────────────────────────────────┘
                            │
                            │ produces
                            ▼
┌─────────────────────────────────────────────────────────────┐
│              EmotionalAnalysis                              │
├─────────────────────────────────────────────────────────────┤
│ - emotionalState: EmotionalState                            │
│ - stressLevel: Int                                          │
│ - energyLevel: Int                                          │
│ - heartRateVariability: Int                                 │
│ - confidence: Float                                         │
│ - timestamp: Long                                           │
└─────────────────────────────────────────────────────────────┘
```

#### Services
```
┌─────────────────────────────────────────────────────────────┐
│              HapticFeedbackService                          │
├─────────────────────────────────────────────────────────────┤
│ - vibrator: Vibrator                                        │
├─────────────────────────────────────────────────────────────┤
│ + vibrate(intensity: Int)                                   │
│ + vibratePattern(pattern: HapticPattern)                    │
│ + stopVibration()                                           │
│ + hasVibrator(): Boolean                                    │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│              AudioFeedbackService                           │
├─────────────────────────────────────────────────────────────┤
│ - soundPool: SoundPool                                      │
│ - soundMap: Map<SoundType, Int>                             │
├─────────────────────────────────────────────────────────────┤
│ + playSound(soundType: SoundType, volume: Float, loop: Int)│
│ + stopSound()                                               │
│ + playBreathingGuide()                                      │
│ + playCalmMusic()                                           │
└─────────────────────────────────────────────────────────────┘
```

#### UI Layer
```
┌─────────────────────────────────────────────────────────────┐
│              MainActivity                                   │
├─────────────────────────────────────────────────────────────┤
│ + onCreate(savedInstanceState: Bundle?)                      │
└─────────────────────────────────────────────────────────────┘
                            │
                            │ contains
                            ▼
┌─────────────────────────────────────────────────────────────┐
│              NeuroBandNavigation                            │
├─────────────────────────────────────────────────────────────┤
│ + NeuroBandNavigation(navController: NavHostController)      │
└─────────────────────────────────────────────────────────────┘
                            │
                            │ navigates to
                            ▼
        ┌───────────┬───────────┬───────────┬───────────┐
        ▼           ▼           ▼           ▼           ▼
┌───────────┐ ┌───────────┐ ┌───────────┐ ┌───────────┐
│Connection │ │ Dashboard │ │  Trends   │ │  Insights │
│  Screen   │ │  Screen   │ │  Screen   │ │  Screen   │
└───────────┘ └───────────┘ └───────────┘ └───────────┘
        │           │           │           │
        ▼           ▼           ▼           ▼
┌───────────┐ ┌───────────┐ ┌───────────┐ ┌───────────┐
│Connection │ │ Dashboard │ │  Trends   │ │  Insights │
│ ViewModel │ │ ViewModel │ │ ViewModel │ │ ViewModel │
└───────────┘ └───────────┘ └───────────┘ └───────────┘
```

---

## 3. Sequence Diagram

### BLE Connection Flow
```
User          ConnectionScreen    BleRepository    BleScanner    BleManager    Device
 │                    │                  │              │              │         │
 │  Start Scan        │                  │              │              │         │
 ├───────────────────>│                  │              │              │         │
 │                    │  startScan()     │              │              │         │
 │                    ├─────────────────>│              │              │         │
 │                    │                  │  startScan() │              │         │
 │                    │                  ├─────────────>│              │         │
 │                    │                  │              │  Scan       │         │
 │                    │                  │              ├────────────>│         │
 │                    │                  │              │  Device     │         │
 │                    │                  │              │<────────────│         │
 │                    │  Device Found    │              │              │         │
 │                    │<─────────────────│              │              │         │
 │  Show Devices      │                  │              │              │         │
 │<───────────────────│                  │              │              │         │
 │  Select Device     │                  │              │              │         │
 ├───────────────────>│                  │              │              │         │
 │                    │  connectToDevice()              │              │         │
 │                    │                  │  stopScan()  │              │         │
 │                    │                  ├─────────────>│              │         │
 │                    │  connect()       │              │              │         │
 │                    ├─────────────────────────────────>│              │         │
 │                    │                  │              │  Connect     │         │
 │                    │                  │              ├────────────>│         │
 │                    │                  │              │  Connected   │         │
 │                    │                  │              │<────────────│         │
 │                    │  Connected       │              │              │         │
 │                    │<─────────────────────────────────│              │         │
 │  Navigate to       │                  │              │              │         │
 │  Dashboard          │                  │              │              │         │
 │<───────────────────│                  │              │              │         │
```

### Biometric Data Flow
```
Device        BleManager      BleRepository    EmotionalEngine    Dashboard
 │                │                 │                  │                │
 │  Biometric      │                 │                  │                │
 │  Data           │                 │                  │                │
 ├───────────────>│                 │                  │                │
 │                │  Update State   │                  │                │
 │                ├────────────────>│                  │                │
 │                │                 │  Analyze Data    │                │
 │                │                 ├─────────────────>│                │
 │                │                 │                  │  Process       │
 │                │                 │                  │  Analysis      │
 │                │                 │                  │  Generate      │
 │                │                 │                  │  Recommendations│
 │                │                 │<─────────────────│                │
 │                │  Emotional State│                  │                │
 │                ├────────────────>│                  │                │
 │                │                 │                  │                │
 │                │                 │                  │                │
 │                │                 │                  │                │
 │                │                 │                  │                │
 │                │                 │                  │                │
```

### AI Recommendation Flow
```
Dashboard    EmotionalEngine    HapticService    AudioService    User
 │                 │                  │                │           │
 │  High Stress     │                  │                │           │
 ├────────────────>│                  │                │           │
 │                 │  Generate        │                │           │
 │                 │  Recommendations  │                │           │
 │                 │  (Breathing,     │                │           │
 │                 │   Haptic Calm)   │                │           │
 │                 │                  │                │           │
 │  Show Alert      │                  │                │           │
 │<────────────────│                  │                │           │
 │                 │                  │                │           │
 │  Start           │                  │                │           │
 │  Meditation      │                  │                │           │
 ├────────────────>│                  │                │           │
 │                 │  Activate        │                │           │
 │                 │  Breathing Guide │                │           │
 │                 ├─────────────────>│                │           │
 │                 │                  │  Play          │           │
 │                 │                  │  Breathing     │           │
 │                 │                  │  Pattern       │           │
 │                 │                  ├───────────────>│           │
 │                 │                  │  Play          │           │
 │                 │                  │  Guide Sound   │           │
 │                 │                  ├───────────────>│           │
 │                 │                  │                │  Audio     │
 │                 │                  │                │  Feedback  │
 │                 │                  │                │<──────────│
 │                 │                  │                │           │
 │  Guide Active    │                  │                │           │
 │<────────────────│                  │                │           │
```

---

## 4. Component Diagram

### System Components
```
┌─────────────────────────────────────────────────────────────┐
│                    NeuroBand App                           │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │              Presentation Layer                      │   │
│  │  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌────────┐ │   │
│  │  │Dashboard │ │Connection│ │  Trends  │ │Settings│ │   │
│  │  │  Screen  │ │  Screen  │ │  Screen  │ │ Screen │ │   │
│  │  └──────────┘ └──────────┘ └──────────┘ └────────┘ │   │
│  │  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌────────┐ │   │
│  │  │ Insights  │ │ Bottom   │ │ Common   │ │ Theme  │ │   │
│  │  │  Screen   │ │  Nav     │ │ Components│ │ System │ │   │
│  │  └──────────┘ └──────────┘ └──────────┘ └────────┘ │   │
│  └─────────────────────────────────────────────────────┘   │
│                           │                                 │
│  ┌─────────────────────────────────────────────────────┐   │
│  │              Domain Layer                             │   │
│  │  ┌──────────────────────────────────────────────┐   │   │
│  │  │           EmotionalEngine                      │   │   │
│  │  │  - Analyze biometric data                     │   │   │
│  │  │  - Generate recommendations                    │   │   │
│  │  │  - Predict emotional states                    │   │   │
│  │  └──────────────────────────────────────────────┘   │   │
│  └─────────────────────────────────────────────────────┘   │
│                           │                                 │
│  ┌─────────────────────────────────────────────────────┐   │
│  │              Data Layer                              │   │
│  │  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌────────┐ │   │
│  │  │   BLE    │ │Supabase  │ │  Local   │ │ Repos  │ │   │
│  │  │ Manager  │ │  Client  │ │ Storage  │ │        │ │   │
│  │  └──────────┘ └──────────┘ └──────────┘ └────────┘ │   │
│  └─────────────────────────────────────────────────────┘   │
│                           │                                 │
│  ┌─────────────────────────────────────────────────────┐   │
│  │              Services Layer                          │   │
│  │  ┌──────────────────┐ ┌──────────────────┐           │   │
│  │  │ HapticFeedback   │ │ AudioFeedback    │           │   │
│  │  │     Service      │ │     Service      │           │   │
│  │  └──────────────────┘ └──────────────────┘           │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
└─────────────────────────────────────────────────────────────┘
                            │
                            │ communicates with
                            ▼
┌─────────────────────────────────────────────────────────────┐
│              External Systems                               │
├─────────────────────────────────────────────────────────────┤
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐     │
│  │ NeuroBand    │  │  Supabase    │  │  Android     │     │
│  │  Device      │  │  Backend     │  │  System      │     │
│  │  (BLE)       │  │  (PostgreSQL)│  │  Services   │     │
│  └──────────────┘  └──────────────┘  └──────────────┘     │
└─────────────────────────────────────────────────────────────┘
```

---

## 5. Data Flow Diagram

### Biometric Data Flow
```
┌──────────────┐
│ NeuroBand    │
│  Device      │
└──────┬───────┘
       │ BLE
       │ Biometric Data
       ▼
┌──────────────┐
│  BLE Manager │
│  (GATT)      │
└──────┬───────┘
       │ Parse & Process
       ▼
┌──────────────┐
│  Biometric   │
│  Data Object │
└──────┬───────┘
       │
       ├──────────────────┐
       │                  │
       ▼                  ▼
┌──────────────┐   ┌──────────────┐
│  Dashboard   │   │  Emotional   │
│  Display    │   │  Engine      │
└──────────────┘   └──────┬───────┘
                          │ Analyze
                          ▼
                   ┌──────────────┐
                   │  Emotional   │
                   │  Analysis    │
                   └──────┬───────┘
                          │
                          ├──────────────────┐
                          │                  │
                          ▼                  ▼
                   ┌──────────────┐   ┌──────────────┐
                   │ Recommendations│  │  Prediction  │
                   └──────┬───────┘   └──────┬───────┘
                          │                  │
                          ▼                  ▼
                   ┌──────────────┐   ┌──────────────┐
                   │  User UI     │   │  Supabase    │
                   │  Display     │   │  Storage     │
                   └──────────────┘   └──────────────┘
```

### User Interaction Flow
```
┌──────────────┐
│    User      │
└──────┬───────┘
       │
       ├──────────────────┐
       │                  │
       ▼                  ▼
┌──────────────┐   ┌──────────────┐
│  Connect     │   │  View        │
│  Device      │   │  Dashboard   │
└──────┬───────┘   └──────┬───────┘
       │                  │
       ▼                  ▼
┌──────────────┐   ┌──────────────┐
│  BLE Scan    │   │  Real-time   │
│  & Pair      │   │  Monitoring  │
└──────┬───────┘   └──────┬───────┘
       │                  │
       └────────┬─────────┘
                │
                ▼
         ┌──────────────┐
         │  Receive     │
         │  AI Insights │
         └──────┬───────┘
                │
                ├──────────────────┐
                │                  │
                ▼                  ▼
         ┌──────────────┐   ┌──────────────┐
         │  Follow      │   │  Configure   │
         │  Recs        │   │  Settings    │
         └──────┬───────┘   └──────┬───────┘
                │                  │
                └────────┬─────────┘
                         │
                         ▼
                  ┌──────────────┐
                  │  Haptic &    │
                  │  Audio       │
                  │  Feedback     │
                  └──────────────┘
```

---

## 6. Database Schema (Supabase)

### Entity Relationships
```
┌──────────────┐
│    users     │
├──────────────┤
│ id (PK)      │
│ email        │
│ password_hash│
│ first_name   │
│ last_name    │
│ ...          │
└──────┬───────┘
       │ 1
       │
       │ N
┌──────▼───────┐
│   devices    │
├──────────────┤
│ id (PK)      │
│ user_id (FK) │
│ serial_number│
│ ...          │
└──────┬───────┘
       │ 1
       │
       │ N
┌──────▼───────┐
│biometric_data│
├──────────────┤
│ id (PK)      │
│ device_id(FK)│
│ heart_rate   │
│ stress_level │
│ ...          │
└──────────────┘

┌──────────────┐
│emotional_    │
│  records     │
├──────────────┤
│ id (PK)      │
│ user_id (FK) │
│ stress_level │
│ energy_level │
│ ...          │
└──────────────┘

┌──────────────┐
│recommendation│
├──────────────┤
│ id (PK)      │
│ user_id (FK) │
│ type         │
│ title        │
│ ...          │
└──────────────┘
```

---

## 7. State Management

### State Flow Architecture
```
┌─────────────────────────────────────────────────────────────┐
│              State Flow Diagram                             │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  BleRepository                                               │
│  ├── connectionState: StateFlow<ConnectionState>           │
│  ├── biometricData: StateFlow<BiometricData?>              │
│  ├── batteryLevel: StateFlow<Int?>                          │
│  └── scanStatus: StateFlow<ScanStatus>                    │
│                                                             │
│  EmotionalEngine                                            │
│  ├── emotionalState: StateFlow<EmotionalAnalysis>          │
│  ├── recommendations: StateFlow<List<Recommendation>>       │
│  └── prediction: StateFlow<EmotionalPrediction?>            │
│                                                             │
│  ViewModels                                                 │
│  ├── ConnectionViewModel                                   │
│  │   ├── scanStatus: StateFlow<ScanStatus>                 │
│  │   └── connectionState: StateFlow<ConnectionState>       │
│  ├── DashboardViewModel                                    │
│  │   ├── biometricData: StateFlow<BiometricData?>          │
│  │   ├── batteryLevel: StateFlow<Int?>                     │
│  │   └── emotionalState: StateFlow<EmotionalState>          │
│  └── ...                                                    │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## 8. Security Architecture

### Security Layers
```
┌─────────────────────────────────────────────────────────────┐
│              Security Architecture                           │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  Application Layer                                    │   │
│  │  - Biometric Authentication                          │   │
│  │  - Session Management                                │   │
│  │  - Permission Handling                               │   │
│  └─────────────────────────────────────────────────────┘   │
│                           │                                 │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  Data Layer                                           │   │
│  │  - End-to-end Encryption                             │   │
│  │  - Secure Storage (Keystore)                         │   │
│  │  - Data Minimization                                 │   │
│  └─────────────────────────────────────────────────────┘   │
│                           │                                 │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  Network Layer                                       │   │
│  │  - TLS/SSL Encryption                                │   │
│  │  - Certificate Pinning                               │   │
│  │  - Secure API Communication                          │   │
│  └─────────────────────────────────────────────────────┘   │
│                           │                                 │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  Database Layer (Supabase)                           │   │
│  │  - Row Level Security (RLS)                          │   │
│  │  - User Authentication                               │   │
│  │  - Encrypted Storage                                 │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## 9. Technology Stack

### Technologies Used
```
Frontend:
├── Kotlin
├── Android Studio
├── Jetpack Compose
├── Material 3
├── Hilt (Dependency Injection)
└── Coroutines

Backend:
├── Supabase
├── PostgreSQL
├── Realtime API
└── Row Level Security (RLS)

Connectivity:
├── Bluetooth Low Energy (BLE)
└── Nordic BLE Library

AI/ML:
├── Custom Emotional Engine
├── Biometric Analysis
└── Predictive Models

Services:
├── Haptic Feedback
└── Audio Feedback
```

---

## 10. Deployment Architecture

### Deployment Flow
```
┌──────────────┐
│  Development │
└──────┬───────┘
       │
       ▼
┌──────────────┐
│   Testing    │
└──────┬───────┘
       │
       ▼
┌──────────────┐
│   Staging    │
└──────┬───────┘
       │
       ▼
┌──────────────┐
│  Production  │
└──────┬───────┘
       │
       ├──────────────────┐
       │                  │
       ▼                  ▼
┌──────────────┐   ┌──────────────┐
│  Google Play │   │  Supabase    │
│  Store       │   │  Cloud       │
└──────────────┘   └──────────────┘
```

---

## Summary

This UML documentation provides a comprehensive overview of the NeuroBand application architecture, including:

1. **Use Case Diagrams**: User and system interactions
2. **Class Diagrams**: Core classes and their relationships
3. **Sequence Diagrams**: Flow of operations and data
4. **Component Diagrams**: System components and their interactions
5. **Data Flow Diagrams**: How data moves through the system
6. **Database Schema**: Entity relationships in Supabase
7. **State Management**: State flow architecture
8. **Security Architecture**: Multi-layer security approach
9. **Technology Stack**: Technologies used in the project
10. **Deployment Architecture**: Deployment flow and infrastructure

The architecture follows clean architecture principles with clear separation of concerns across presentation, domain, and data layers, ensuring scalability, maintainability, and testability.
