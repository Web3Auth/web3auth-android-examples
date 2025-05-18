# Web3Auth Flutter Auth0 Example

This example demonstrates how to integrate Web3Auth with Auth0 authentication in a Flutter application. It showcases a custom authentication setup using Auth0 as the authentication provider with Web3Auth's blockchain functionality.

## 📝 Features
- Auth0 social login integration
- Custom authentication flow
- Ethereum wallet creation and management
- Basic blockchain interactions
- Secure key management
- Cross-platform support (iOS & Android)

## 🚀 Getting Started

### Prerequisites
- Flutter 2.10.0 or higher
- Dart 2.16.0 or higher
- [Auth0 Account](https://auth0.com)
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
cd web3auth-mobile-examples/flutter/flutter-auth0-example
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

1. Auth0 Setup:
   - Create a new application in [Auth0 Dashboard](https://manage.auth0.com)
   - Configure callback URLs:
     ```
     com.example.app://login-callback
     ```
   - Note down Domain and Client ID

2. Web3Auth Setup:
   - Get your Client ID from [Web3Auth Dashboard](https://dashboard.web3auth.io)
   - Create a custom verifier with Auth0 configuration
   - Update configuration in `lib/main.dart`:
   ```dart
   final web3auth = Web3Auth(
     clientId: "YOUR-WEB3AUTH-CLIENT-ID",
     network: Network.testnet,
     customVerifier: "YOUR-VERIFIER-NAME",
   );
   ```

3. Update Auth0 credentials in `lib/config/auth0_config.dart`:
```dart
class Auth0Config {
  static const String domain = "YOUR-AUTH0-DOMAIN";
  static const String clientId = "YOUR-AUTH0-CLIENT-ID";
  static const String redirectUri = "com.example.app://login-callback";
}
```

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
├── config/
│   └── auth0_config.dart  # Auth0 configuration
├── services/
│   ├── auth0_service.dart # Auth0 service
│   ├── web3auth.dart      # Web3Auth service
│   └── blockchain.dart    # Blockchain operations
├── screens/
│   ├── home.dart          # Home screen
│   └── login.dart         # Login screen
└── widgets/               # Reusable widgets
```

### Core Features Implementation

1. **Auth0 Configuration**
```dart
// Initialize Auth0
final auth0 = Auth0(
  Auth0Config.domain,
  Auth0Config.clientId,
);
```

2. **Web3Auth with Auth0**
```dart
// Initialize Web3Auth
final web3auth = Web3Auth(
  clientId: "YOUR-CLIENT-ID",
  network: Network.testnet,
  customVerifier: "YOUR-VERIFIER",
);

// Login with Auth0
Future<void> loginWithAuth0() async {
  // Get Auth0 credentials
  final credentials = await auth0.webAuthentication().login(
    audience: 'https://your-audience/',
    scopes: {'openid', 'profile', 'email'},
  );
  
  // Get ID token
  final idToken = credentials.idToken;
  
  // Connect to Web3Auth
  await web3auth.login(
    loginProvider: Provider.jwt,
    extraLoginOptions: ExtraLoginOptions(
      id_token: idToken,
      verifier: "YOUR-VERIFIER",
      domain: "auth0.com",
    ),
  );
}
```

## 🔒 Security Considerations

- Secure storage of Auth0 credentials
- JWT token handling
- Private key management
- Session management
- OAuth 2.0 best practices

## 🛠️ Troubleshooting

### Common Issues

1. **Auth0 Configuration**
   - Verify callback URLs
   - Check Auth0 application settings
   - Validate domain and client ID

2. **Web3Auth Integration**
   - Verify custom verifier setup
   - Check JWT token handling
   - Debug authentication flow

3. **Platform-Specific Issues**
   - iOS: URL scheme configuration
   - Android: Intent filters setup

## 📚 Resources

- [Web3Auth Documentation](https://web3auth.io/docs)
- [Flutter SDK Reference](https://web3auth.io/docs/sdk/pnp/flutter)
- [Auth0 Flutter Integration](https://auth0.com/docs/quickstart/native/flutter)
- [Custom Authentication Setup](https://web3auth.io/docs/guides/custom-authentication)
- [Auth0 Setup Guide](https://web3auth.io/docs/guides/auth0)

## 🤝 Support

- [Discord](https://discord.gg/web3auth)
- [GitHub Issues](https://github.com/Web3Auth/web3auth-mobile-examples/issues)
- [Web3Auth Support](https://web3auth.io/docs/troubleshooting/support)

## 📄 License

This example is available under the MIT License. See the [LICENSE](../../LICENSE) file for more info.
