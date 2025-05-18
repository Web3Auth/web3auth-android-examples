# Web3Auth Android Firebase Example

This example demonstrates how to integrate Web3Auth with Firebase authentication in an Android application. It showcases a custom authentication setup using Firebase as the authentication provider with Web3Auth's blockchain functionality.

## 📝 Features
- Firebase Authentication integration (Google, Email/Password, Phone)
- Custom authentication flow
- Ethereum wallet creation and management
- Basic blockchain interactions
- Secure key management
- Material Design UI components
- Android 6.0+ (API 23+) support

## 🚀 Getting Started

### Prerequisites
- Android Studio Arctic Fox (2020.3.1) or higher
- Android SDK version 23 or higher
- Gradle 7.0.2 or higher
- JDK 11 or higher
- [Firebase Account](https://firebase.google.com)
- [Web3Auth Dashboard](https://dashboard.web3auth.io) account
- Basic understanding of Android development and Web3

### Installation

1. Clone the repository:
```bash
git clone https://github.com/Web3Auth/web3auth-mobile-examples.git
cd web3auth-mobile-examples/android/android-firebase-example
```

2. Open the project in Android Studio:
   - Launch Android Studio
   - Select "Open an existing Android Studio project"
   - Navigate to the project directory
   - Click "OK"

### Configuration

1. Firebase Setup:
   - Create a new project in [Firebase Console](https://console.firebase.google.com)
   - Add an Android app to your project
   - Download and add the `google-services.json` file to the app directory
   - Enable Authentication methods you want to use (Google, Email/Password, etc.)
   - Set up Firebase Cloud Functions for authentication (if using custom tokens)

2. Web3Auth Setup:
   - Get your Client ID from [Web3Auth Dashboard](https://dashboard.web3auth.io)
   - Create a custom verifier with Firebase configuration
   - Update configuration in `app/src/main/res/values/strings.xml`:
   ```xml
   <string name="web3auth_client_id">YOUR-WEB3AUTH-CLIENT-ID</string>
   <string name="web3auth_verifier">YOUR-VERIFIER-NAME</string>
   ```

3. Run the application:
   - Select your target device/emulator
   - Click the "Run" button (▶️) or press Shift + F10

## 💡 Implementation Details

### Project Structure
```
app/
├── src/
│   ├── main/
│   │   ├── java/com/web3auth/firebase/example/
│   │   │   ├── MainActivity.kt           # Main activity
│   │   │   ├── FirebaseAuthService.kt    # Firebase authentication
│   │   │   ├── Web3AuthService.kt        # Web3Auth integration
│   │   │   └── BlockchainService.kt      # Blockchain operations
│   │   ├── res/
│   │   │   ├── layout/                   # UI layouts
│   │   │   └── values/                   # Resource values
│   │   └── AndroidManifest.xml
│   └── build.gradle                      # App level build config
└── build.gradle                          # Project level build config
```

### Core Features Implementation

1. **Firebase Configuration**
```kotlin
// Initialize Firebase
FirebaseApp.initializeApp(this)
val auth = FirebaseAuth.getInstance()
```

2. **Web3Auth with Firebase**
```kotlin
// Initialize Web3Auth
val web3Auth = Web3Auth(
    context = this,
    clientId = getString(R.string.web3auth_client_id),
    network = Web3Auth.Network.TESTNET,
    redirectUrl = Uri.parse("YOUR-APP-SCHEME://auth")
)

// Login with Firebase
private fun loginWithFirebase() {
    // Configure Firebase Auth UI
    startActivityForResult(
        AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build(),
        RC_SIGN_IN
    )
}

// Handle Firebase result
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == RC_SIGN_IN) {
        // Get Firebase user
        val user = FirebaseAuth.getInstance().currentUser
        
        // Get ID token
        user?.getIdToken(true)?.addOnSuccessListener { result ->
            val idToken = result.token
            
            // Connect to Web3Auth
            web3Auth.login(LoginParams().apply {
                this.loginType = LoginType.JWT
                this.jwtParams = JwtParams().apply {
                    this.verifier = getString(R.string.web3auth_verifier)
                    this.idToken = idToken
                }
            }, ::handleLoginResult)
        }
    }
}
```

## 🔒 Security Considerations

- Secure storage of Firebase credentials
- JWT token handling
- Private key management
- Firebase security rules best practices
- Secure network communications

## 🛠️ Troubleshooting

### Common Issues

1. **Firebase Configuration**
   - Verify `google-services.json` is correctly placed
   - Check Firebase project settings
   - Validate authentication methods are enabled

2. **Web3Auth Integration**
   - Verify custom verifier setup
   - Check JWT token handling
   - Debug authentication flow

3. **Android-Specific Issues**
   - Check manifest permissions
   - Verify SHA-1 fingerprint configuration
   - Debug intent resolution

## 📚 Resources

- [Web3Auth Documentation](https://web3auth.io/docs)
- [Android SDK Reference](https://web3auth.io/docs/sdk/pnp/android)
- [Firebase Android Guide](https://firebase.google.com/docs/android/setup)
- [Custom Authentication Setup](https://web3auth.io/docs/guides/custom-authentication)
- [Firebase Setup Guide](https://web3auth.io/docs/guides/firebase)

## 🤝 Support

- [Discord](https://discord.gg/web3auth)
- [GitHub Issues](https://github.com/Web3Auth/web3auth-mobile-examples/issues)
- [Web3Auth Support](https://web3auth.io/docs/troubleshooting/support)

## 📄 License

This example is available under the MIT License. See the [LICENSE](../../LICENSE) file for more info.
