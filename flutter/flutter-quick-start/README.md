# Web3Auth Flutter Quick Start Example

This example demonstrates how to integrate Web3Auth into a Flutter application using the Web3Auth Flutter SDK. It provides a simple yet comprehensive example of implementing Web3Auth's authentication and blockchain functionality in a cross-platform Flutter app.

## 📝 Features
- Social login integration (Google, Facebook, Twitter, etc.)
- Ethereum wallet creation and management
- Basic blockchain interactions
- Secure key management
- Cross-platform support (iOS & Android)
- Material Design UI components

## 🔗 Live Demo
- Android: [Play Store Link]
- iOS: [TestFlight Link]

## 🚀 Getting Started

### Prerequisites
- Flutter 2.10.0 or higher
- Dart 2.16.0 or higher
- Android Studio / VS Code with Flutter extension
- For iOS development:
  - Xcode 13+
  - CocoaPods
- For Android development:
  - Android Studio
  - JDK 11+
- [Web3Auth Dashboard](https://dashboard.web3auth.io) account

### Installation

1. Clone the repository:
```bash
git clone https://github.com/Web3Auth/web3auth-mobile-examples.git
cd web3auth-mobile-examples/flutter/flutter-quick-start
```

2. Install dependencies:
```bash
flutter pub get
```

3. iOS Setup (for iOS development):
```bash
cd ios && pod install && cd ..
```

4. Configure Web3Auth:
   - Get your Client ID from the [Web3Auth Dashboard](https://dashboard.web3auth.io)
   - Update the Client ID in `lib/main.dart`:
   ```dart
   final web3auth = Web3Auth(
     clientId: "YOUR-CLIENT-ID",
     network: Network.testnet,
   );
   ```

5. Configure OAuth (for social logins):
   - Follow the [OAuth Configuration Guide](https://web3auth.io/docs/guides/oauth-providers)
   - Set up required OAuth providers in Web3Auth Dashboard
   - Update your OAuth credentials in the project

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
├── main.dart              # Entry point
├── services/
│   ├── web3auth.dart      # Web3Auth service
│   └── blockchain.dart    # Blockchain operations
├── screens/
│   ├── home.dart          # Home screen
│   └── login.dart         # Login screen
└── widgets/              # Reusable widgets
```

### Core Features Implementation

1. **Initialize Web3Auth**
```dart
final web3auth = Web3Auth(
  clientId: "YOUR-CLIENT-ID",
  network: Network.testnet,
);

await web3auth.initialize();
```

2. **Implement Authentication**
```dart
// Login
final UserInfo? userInfo = await web3auth.login(
  loginProvider: Provider.google,
  mfaLevel: MFALevel.NONE,
);

// Logout
await web3auth.logout();
```

3. **Blockchain Interactions**
```dart
// Get user info
final userInfo = await web3auth.getUserInfo();

// Get private key
final privateKey = await web3auth.getPrivateKey();

// Create Web3 instance
final web3 = Web3(privateKey);
final address = await web3.getAddress();
```

## 🔒 Security Considerations

- Private keys are securely managed by Web3Auth
- Social login credentials are handled through OAuth
- Secure storage implementation for persistent auth
- Platform-specific security best practices

## 🛠️ Troubleshooting

### Common Issues

1. **Build Errors**
   - Update Flutter and dependencies:
     ```bash
     flutter upgrade
     flutter pub upgrade
     ```
   - Clean and rebuild:
     ```bash
     flutter clean
     flutter pub get
     cd ios && pod install && cd ..  # For iOS
     ```

2. **OAuth Configuration**
   - Verify OAuth credentials in Web3Auth Dashboard
   - Check platform-specific configurations
   - Validate OAuth provider setup

3. **Platform-Specific Issues**
   - iOS:
     - Check URL scheme configuration
     - Verify signing capabilities
   - Android:
     - Check manifest permissions
     - Verify SHA-1 configuration

## 📚 Resources

- [Web3Auth Documentation](https://web3auth.io/docs)
- [Flutter SDK Reference](https://web3auth.io/docs/sdk/pnp/flutter)
- [Integration Builder](https://web3auth.io/docs/integration-builder)
- [OAuth Setup Guide](https://web3auth.io/docs/guides/oauth-providers)

## 🤝 Support

- [Discord](https://discord.gg/web3auth)
- [GitHub Issues](https://github.com/Web3Auth/web3auth-mobile-examples/issues)
- [Web3Auth Support](https://web3auth.io/docs/troubleshooting/support)

## 📄 License

This example is available under the MIT License. See the [LICENSE](../../LICENSE) file for more info.
