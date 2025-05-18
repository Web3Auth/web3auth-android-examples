# Web3Auth Android Quick Start Example

This example demonstrates how to integrate Web3Auth into an Android application using the Web3Auth Android SDK. It provides a simple yet comprehensive example of implementing Web3Auth's authentication and blockchain functionality in a native Android app.

## 📝 Features
- Social login integration (Google, Facebook, Twitter, etc.)
- Ethereum wallet creation and management
- Basic blockchain interactions
- Secure key management
- Material Design UI components
- Android 6.0+ (API 23+) support

## 🔗 Live Demo
Download from Play Store: [Add Play Store Link]

## 🚀 Getting Started

### Prerequisites
- Android Studio Arctic Fox (2020.3.1) or higher
- Android SDK version 23 or higher
- Gradle 7.0.2 or higher
- JDK 11 or higher
- [Web3Auth Dashboard](https://dashboard.web3auth.io) account
- Basic understanding of Android development and Web3

### Installation

1. Clone the repository:
```bash
git clone https://github.com/Web3Auth/web3auth-mobile-examples.git
cd web3auth-mobile-examples/android/android-quick-start
```

2. Open the project in Android Studio:
   - Launch Android Studio
   - Select "Open an existing Android Studio project"
   - Navigate to the project directory
   - Click "OK"

3. Configure your project:
   - Update the `local.properties` file with your SDK path
   - Add your Web3Auth client ID in `app/src/main/res/values/strings.xml`:
   ```xml
   <string name="web3auth_client_id">YOUR-CLIENT-ID</string>
   ```

4. Configure OAuth:
   - Follow the [OAuth Configuration Guide](https://web3auth.io/docs/guides/oauth-providers)
   - Set up required OAuth providers in Web3Auth Dashboard
   - Update your OAuth credentials in the project

5. Run the application:
   - Select your target device/emulator
   - Click the "Run" button (▶️) or press Shift + F10

## 💡 Implementation Details

### Project Structure
```
app/
├── src/
│   ├── main/
│   │   ├── java/com/web3auth/example/
│   │   │   ├── MainActivity.kt           # Main activity
│   │   │   ├── Web3AuthService.kt       # Web3Auth integration
│   │   │   └── BlockchainService.kt     # Blockchain operations
│   │   ├── res/
│   │   │   ├── layout/                  # UI layouts
│   │   │   └── values/                  # Resource values
│   │   └── AndroidManifest.xml
│   └── build.gradle                     # App level build config
└── build.gradle                         # Project level build config
```

### Core Features Implementation

1. **Initialize Web3Auth**
```kotlin
val web3Auth = Web3Auth(
    context = this,
    clientId = getString(R.string.web3auth_client_id),
    network = Web3Auth.Network.TESTNET,
    redirectUrl = Uri.parse("your_app_scheme://auth")
)
```

2. **Configure Social Logins**
```kotlin
val loginConfig = LoginConfigItem(
    verifier = "your-verifier-name",
    typeOfLogin = TypeOfLogin.GOOGLE,
    clientId = "your-google-client-id"
)
web3Auth.setLoginConfig(loginConfig)
```

3. **Handle Authentication**
```kotlin
web3Auth.login(LoginParams()) { result ->
    when (result) {
        is Web3AuthResponse.Success -> {
            // Handle successful login
            val privateKey = result.data.privKey
            handleLoginSuccess(privateKey)
        }
        is Web3AuthResponse.Error -> {
            // Handle error
            Log.e("Web3Auth", "Error: ${result.error.message}")
        }
    }
}
```

4. **Blockchain Interactions**
```kotlin
// Create Web3j instance
val web3j = Web3j.build(HttpService("https://rpc.ankr.com/eth"))

// Get balance
val balance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send()

// Send transaction
val transaction = web3j.ethSendRawTransaction(signedTransaction).send()
```

## 🔒 Security Best Practices

- Use Android Keystore for sensitive data
- Implement proper certificate pinning
- Handle user data securely
- Implement proper error handling
- Use secure communication channels
- Regular security audits
- Follow Android security guidelines
- Implement proper session management

## 🛠️ Troubleshooting

### Common Issues

1. **Build Errors**
   - Update Gradle and dependencies:
     ```bash
     ./gradlew clean build
     ```
   - Check Android SDK version
   - Verify all dependencies are properly synced

2. **OAuth Configuration**
   - Verify OAuth credentials in Web3Auth Dashboard
   - Check manifest for proper URL scheme configuration
   - Validate OAuth provider setup

3. **Integration Issues**
   - Review initialization parameters
   - Check network connectivity
   - Verify callback handling
   - Debug ProGuard configuration

## 📚 Resources

- [Web3Auth Documentation](https://web3auth.io/docs)
- [Android SDK Reference](https://web3auth.io/docs/sdk/pnp/android)
- [Integration Builder](https://web3auth.io/docs/integration-builder)
- [OAuth Setup Guide](https://web3auth.io/docs/guides/oauth-providers)

## 🤝 Support

- [Discord](https://discord.gg/web3auth)
- [GitHub Issues](https://github.com/Web3Auth/web3auth-mobile-examples/issues)
- [Web3Auth Support](https://web3auth.io/docs/troubleshooting/support)

## 📄 License

This example is available under the MIT License. See the [LICENSE](../../LICENSE) file for more info.
