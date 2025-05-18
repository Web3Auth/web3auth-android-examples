# Web3Auth iOS Quick Start Example

This example demonstrates how to integrate Web3Auth into an iOS application using the Web3Auth iOS SDK. It provides a simple yet comprehensive example of implementing Web3Auth's authentication and blockchain functionality in a native iOS app.

## 📝 Features
- Social login integration (Google, Facebook, Twitter, etc.)
- Ethereum wallet creation and management
- Basic blockchain interactions
- Secure key management
- Modern SwiftUI interface
- iOS 13+ support

## 🔗 Live Demo
Download from TestFlight: [Add TestFlight Link]

## 🚀 Getting Started

### Prerequisites
- Xcode 13+
- iOS 13.0+ deployment target
- Swift 5.0+
- CocoaPods or Swift Package Manager
- [Web3Auth Dashboard](https://dashboard.web3auth.io) account
- Basic understanding of iOS development and Web3

### Installation

1. Clone the repository:
```bash
git clone https://github.com/Web3Auth/web3auth-mobile-examples.git
cd web3auth-mobile-examples/ios/ios-quick-start
```

2. Install dependencies:
   - Using CocoaPods:
   ```bash
   pod install
   ```
   - Or using Swift Package Manager:
     - Open the project in Xcode
     - File > Add Packages
     - Add the Web3Auth SDK package

3. Configure your project:
   - Open `w3a-ios-quick-start.xcworkspace` (for CocoaPods) or `w3a-ios-quick-start.xcodeproj` (for SPM)
   - Update Bundle Identifier
   - Add your Web3Auth client ID in the configuration
   - Configure URL Schemes for social login callbacks

4. Configure OAuth:
   - Follow the [OAuth Configuration Guide](https://web3auth.io/docs/guides/oauth-providers)
   - Set up required OAuth providers in Web3Auth Dashboard
   - Update your OAuth credentials in the project

5. Run the application:
   - Select your target device/simulator
   - Build and run (⌘ + R)

## 💡 Implementation Details

### Project Structure
```
Web3AuthExample/
├── Sources/
│   ├── AppDelegate.swift        # Application delegate
│   ├── Web3AuthService.swift    # Web3Auth integration
│   ├── BlockchainService.swift  # Blockchain operations
│   ├── KeychainService.swift    # Secure storage
│   └── ViewControllers/         # UI controllers
├── Resources/                   # Assets and configs
└── Web3AuthExample.xcodeproj   # Xcode project file
```

### Core Features Implementation

1. **Initialize Web3Auth**
```swift
let web3Auth = Web3Auth(
    clientId: "YOUR-CLIENT-ID",
    network: .testnet,
    redirectURL: "com.example.app://auth"
)
```

2. **Configure Social Logins**
```swift
let loginConfig = LoginConfigItem(
    verifier: "your-verifier-name",
    typeOfLogin: .google,
    clientId: "your-google-client-id"
)
web3Auth.setLoginConfig(loginConfig)
```

3. **Handle Authentication**
```swift
web3Auth.login { [weak self] result in
    switch result {
    case .success(let result):
        // Handle successful login
        let privateKey = result.privKey
        self?.handleLoginSuccess(privateKey)
    case .failure(let error):
        // Handle error
        print("Error: \(error.localizedDescription)")
    }
}
```

4. **Secure Key Storage**
```swift
class KeychainService {
    static func storePrivateKey(_ key: String) throws {
        let keychain = KeychainSwift()
        keychain.accessGroup = "your.app.group"
        keychain.synchronizable = true
        keychain.set(key, forKey: "private_key")
    }
    
    static func retrievePrivateKey() -> String? {
        let keychain = KeychainSwift()
        return keychain.get("private_key")
    }
}
```

## 🔒 Security Best Practices

- Use Keychain for sensitive data storage
- Implement proper certificate pinning
- Handle user data securely
- Implement proper error handling
- Use secure communication channels
- Regular security audits
- Follow Apple's security guidelines
- Implement proper session management

## 🛠️ Troubleshooting

### Common Issues

1. **Build Errors**
   - Update CocoaPods/SPM dependencies
   - Clean build folder and rebuild
   - Check minimum iOS version requirements
   - Verify architecture settings

2. **OAuth Configuration**
   - Verify URL scheme configuration
   - Check client ID configuration
   - Ensure proper callback handling
   - Debug OAuth provider setup

3. **Integration Issues**
   - Review initialization parameters
   - Check network connectivity
   - Verify social login configurations
   - Handle background state changes

## 📚 Resources

- [Web3Auth iOS Documentation](https://web3auth.io/docs/sdk/pnp/ios)
- [iOS Integration Guide](https://web3auth.io/docs/guides/ios)
- [API Reference](https://web3auth.io/docs/sdk/pnp/ios#api-reference)
- [Web3Auth Dashboard](https://dashboard.web3auth.io)

## 🤝 Support

- [Discord](https://discord.gg/web3auth)
- [GitHub Issues](https://github.com/Web3Auth/web3auth-mobile-examples/issues)
- [Web3Auth Support](https://web3auth.io/docs/troubleshooting/support)

## 📄 License

This example is available under the MIT License. See the [LICENSE](../../LICENSE) file for more info.
