# Web3Auth Flutter Firebase Example

This example demonstrates how to integrate Web3Auth with Firebase authentication in a Flutter application. It showcases a custom authentication setup using Firebase as the authentication provider with Web3Auth's blockchain functionality.

## 📝 Features
- Firebase Authentication integration (Google, Email/Password, Phone)
- Custom authentication flow
- Ethereum wallet creation and management
- Basic blockchain interactions
- Secure key management
- Cross-platform support (iOS & Android)

## 🚀 Getting Started

### Prerequisites
- Flutter 2.10.0 or higher
- Dart 2.16.0 or higher
- [Firebase Account](https://firebase.google.com)
- [Web3Auth Dashboard](https://dashboard.web3auth.io) account
- Android Studio / VS Code with Flutter extension
- For iOS development:
  - Xcode 13+
  - CocoaPods
- For Android development:
  - Android Studio
  - JDK 11+

### Installation

1. Clone the repository:
```bash
git clone https://github.com/Web3Auth/web3auth-mobile-examples.git
cd web3auth-mobile-examples/flutter/flutter-firebase-example
```

2. Install dependencies:
```bash
flutter pub get
```

3. iOS Setup (for iOS development):
```bash
cd ios && pod install && cd ..
```

### Configuration

1. Firebase Setup:
   - Create a new project in [Firebase Console](https://console.firebase.google.com)
   - Add iOS and Android apps to your project
   - Download and add the configuration files:
     - `GoogleService-Info.plist` for iOS
     - `google-services.json` for Android
   - Enable Authentication methods you want to use (Google, Email/Password, etc.)

2. Web3Auth Setup:
   - Get your Client ID from [Web3Auth Dashboard](https://dashboard.web3auth.io)
   - Create a custom verifier with Firebase configuration
   - Update configuration in `lib/main.dart`:
   ```dart
   final web3auth = Web3Auth(
     clientId: "YOUR-WEB3AUTH-CLIENT-ID",
     network: Network.testnet,
     customVerifier: "YOUR-VERIFIER-NAME",
   );
   ```

3. Update Firebase configuration (already included when you added configuration files)

### Running the App

```bash
# Run in debug mode
flutter run

# Build for release
flutter build ios  # For iOS
flutter build apk  # For Android
```

## 💡 Implementation Details

### Project Structure
```
lib/
├── main.dart                # Entry point
├── services/
│   ├── firebase_auth.dart   # Firebase Auth service
│   ├── web3auth.dart        # Web3Auth service
│   └── blockchain.dart      # Blockchain operations
├── screens/
│   ├── home.dart            # Home screen
│   └── login.dart           # Login screen
└── widgets/                 # Reusable widgets
```

### Core Features Implementation

1. **Firebase Configuration**
```dart
// Initialize Firebase
await Firebase.initializeApp(
  options: DefaultFirebaseOptions.currentPlatform,
);
```

2. **Web3Auth with Firebase**
```dart
// Initialize Web3Auth
final web3auth = Web3Auth(
  clientId: "YOUR-CLIENT-ID",
  network: Network.testnet,
  customVerifier: "YOUR-VERIFIER",
);

// Login with Firebase
Future<void> loginWithFirebase() async {
  // Get Firebase user
  final userCredential = await FirebaseAuth.instance.signInWithProvider(GoogleAuthProvider());
  final user = userCredential.user;
  
  // Get ID token
  final idToken = await user?.getIdToken();
  
  // Connect to Web3Auth
  await web3auth.login(
    loginProvider: Provider.jwt,
    extraLoginOptions: ExtraLoginOptions(
      id_token: idToken,
      verifier: "YOUR-VERIFIER",
      domain: "firebase",
    ),
  );
}
```

## 🔒 Security Considerations

- Secure storage of Firebase credentials
- JWT token handling
- Private key management
- Session management
- Firebase security rules best practices

## 🛠️ Troubleshooting

### Common Issues

1. **Firebase Configuration**
   - Verify configuration files are correctly placed
   - Check Firebase project settings
   - Validate authentication methods are enabled

2. **Web3Auth Integration**
   - Verify custom verifier setup
   - Check JWT token handling
   - Debug authentication flow

3. **Platform-Specific Issues**
   - iOS: Check Info.plist configuration
   - Android: Verify manifest settings and SHA-1 fingerprint

## 📚 Resources

- [Web3Auth Documentation](https://web3auth.io/docs)
- [Flutter SDK Reference](https://web3auth.io/docs/sdk/pnp/flutter)
- [Firebase Flutter Guide](https://firebase.google.com/docs/flutter/setup)
- [Custom Authentication Setup](https://web3auth.io/docs/guides/custom-authentication)
- [Firebase Setup Guide](https://web3auth.io/docs/guides/firebase)

## 🤝 Support

- [Discord](https://discord.gg/web3auth)
- [GitHub Issues](https://github.com/Web3Auth/web3auth-mobile-examples/issues)
- [Web3Auth Support](https://web3auth.io/docs/troubleshooting/support)

## 📄 License

This example is available under the MIT License. See the [LICENSE](../../LICENSE) file for more info.
