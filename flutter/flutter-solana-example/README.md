# Web3Auth Flutter Solana Example

This example demonstrates how to integrate Web3Auth in a Flutter application with Solana blockchain support. It provides a complete implementation for authenticating users and interacting with the Solana blockchain.

## 📝 Features
- Social login integration (Google, Facebook, Twitter, etc.)
- Solana wallet creation and management
- Solana blockchain interactions (balance, transfers, tokens)
- SPL token support
- Secure key management
- Cross-platform support (iOS & Android)

## 🚀 Getting Started

### Prerequisites
- Flutter 2.10.0 or higher
- Dart 2.16.0 or higher
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
cd web3auth-mobile-examples/flutter/flutter-solana-example
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

1. Web3Auth Setup:
   - Get your Client ID from [Web3Auth Dashboard](https://dashboard.web3auth.io)
   - Update configuration in `lib/main.dart`:
   ```dart
   final web3auth = Web3Auth(
     clientId: "YOUR-WEB3AUTH-CLIENT-ID",
     network: Network.testnet,
     redirectUrl: Uri.parse("com.example.app://auth"),
   );
   ```

2. Solana Configuration:
   - Configure Solana network in `lib/services/solana_service.dart`:
   ```dart
   // Use Solana mainnet or devnet
   final rpcUrl = "https://api.devnet.solana.com";
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
├── services/
│   ├── web3auth.dart      # Web3Auth service
│   └── solana_service.dart # Solana blockchain operations
├── screens/
│   ├── home.dart          # Home screen
│   └── login.dart         # Login screen
└── widgets/               # Reusable widgets
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

2. **Authentication**
```dart
// Login
final loginResponse = await web3auth.login(
  loginProvider: Provider.google,
  mfaLevel: MFALevel.NONE,
);

// Logout
await web3auth.logout();
```

3. **Solana Blockchain Interactions**
```dart
// Get private key
final privateKey = await web3auth.getPrivateKey();

// Create Solana instance
final solana = SolanaService(privateKey);

// Get wallet address
final address = await solana.getAddress();

// Get SOL balance
final balance = await solana.getBalance();

// Send SOL
final txHash = await solana.transfer(recipientAddress, amount);

// Get SPL token balance
final tokenBalance = await solana.getTokenBalance(tokenMintAddress);
```

## 🔒 Security Considerations

- Private keys are securely managed by Web3Auth
- Solana transaction signing happens locally
- Session management follows security best practices
- Secure storage implementation for persistent auth

## 🛠️ Troubleshooting

### Common Issues

1. **Solana RPC Issues**
   - Check network connectivity
   - Verify RPC endpoint status
   - Try alternative Solana RPC endpoints

2. **Transaction Failures**
   - Check account SOL balance (for fees)
   - Verify transaction parameters
   - Debug transaction construction

3. **Platform-Specific Issues**
   - iOS: Check Info.plist configuration for URL schemes
   - Android: Verify manifest settings

## 📚 Resources

- [Web3Auth Documentation](https://web3auth.io/docs)
- [Flutter SDK Reference](https://web3auth.io/docs/sdk/pnp/flutter)
- [Solana Documentation](https://docs.solana.com/)
- [Solana Web3.js](https://solana-labs.github.io/solana-web3.js/)
- [SPL Token Documentation](https://spl.solana.com/token)

## 🤝 Support

- [Discord](https://discord.gg/web3auth)
- [GitHub Issues](https://github.com/Web3Auth/web3auth-mobile-examples/issues)
- [Web3Auth Support](https://web3auth.io/docs/troubleshooting/support)

## 📄 License

This example is available under the MIT License. See the [LICENSE](../../LICENSE) file for more info.
