# Web3Auth iOS Auth0 Example

[![Web3Auth](https://img.shields.io/badge/Web3Auth-SDK-blue)](https://web3auth.io/docs/sdk/pnp/ios)
[![Web3Auth](https://img.shields.io/badge/Web3Auth-Community-cyan)](https://community.web3auth.io)

This example demonstrates how to integrate Web3Auth with Auth0 authentication in an iOS application, providing a secure and customizable authentication flow for your mobile dApp users.

## Features

- 📱 Native iOS integration
- 🔐 Auth0 authentication integration
- 🎨 Custom UI implementation
- 🔑 JWT-based authentication flow
- ⛓️ Blockchain integration
- 🔄 Session management
- 🛡️ Secure token handling
- 🎨 Customizable authentication flow

## Prerequisites

- Xcode 13.0 or higher
- iOS 13.0+ deployment target
- Swift 5.0 or higher
- CocoaPods or Swift Package Manager
- Auth0 account and application setup
- Web3Auth account and client ID (get one at [Web3Auth Dashboard](https://dashboard.web3auth.io))
- Basic understanding of JWT and OAuth flows

## Tech Stack

- **Language**: Swift
- **Package Manager**: CocoaPods/SPM
- **Authentication**: 
  - `Auth0.swift`: Auth0 SDK for iOS
  - `JWTDecode`: JWT handling
- **Web3 Libraries**: 
  - `Web3Auth`: Core Web3Auth functionality
  - `Web3`: Ethereum interaction library
  - `TorusUtils`: Underlying key management

## Installation

1. Clone the repository:
```bash
npx degit Web3Auth/web3auth-pnp-examples/ios/ios-auth0-example w3a-ios-auth0
```

2. Install dependencies:
   - Using CocoaPods:
   ```bash
   cd w3a-ios-auth0
   pod install
   ```
   - Or using Swift Package Manager:
     - Open the project in Xcode
     - File > Add Packages
     - Add the required SDK packages

3. Configure your project:
   - Open `w3a-ios-auth0.xcworkspace` (for CocoaPods) or `w3a-ios-auth0.xcodeproj` (for SPM)
   - Update Bundle Identifier
   - Add your Web3Auth client ID in the configuration
   - Configure Auth0 credentials and settings
   - Set up URL schemes for OAuth callbacks

4. Run the application:
   - Select your target device/simulator
   - Build and run (⌘ + R)

## Project Structure

```
Web3AuthAuth0/
├── Sources/
│   ├── AppDelegate.swift          # Application delegate
│   ├── Web3AuthService.swift      # Web3Auth integration
│   ├── Auth0Service.swift         # Auth0 operations
│   ├── TokenService.swift         # JWT token operations
│   └── ViewControllers/           # UI controllers
├── Resources/                     # Assets and configs
└── Web3AuthAuth0.xcodeproj       # Xcode project file
```

## Implementation Guide

### 1. Configure Auth0 Integration

```swift
Auth0.swift.configureClient(
    clientId: "YOUR_AUTH0_CLIENT_ID",
    domain: "YOUR_AUTH0_DOMAIN"
)
```

### 2. Initialize Web3Auth with Custom Verifier

```swift
let web3Auth = Web3Auth(
    clientId: "YOUR_WEB3AUTH_CLIENT_ID",
    network: .testnet,
    redirectURL: "com.example.app://auth"
)

web3Auth.setCustomVerifier("auth0-verifier")
```

### 3. Handle Auth0 Authentication

```swift
func loginWithAuth0() async throws -> Credentials {
    return try await Auth0
        .webAuth()
        .scope("openid profile email")
        .audience("https://your-api-identifier")
        .start()
}
```

### 4. Connect Web3Auth with JWT

```swift
func connectWeb3Auth(credentials: Credentials) async throws {
    guard let idToken = credentials.idToken else {
        throw AuthError.missingToken
    }
    
    try await web3Auth.connectTo("jwt", {
        id_token: idToken,
        verifier: "auth0-verifier"
    })
}
```

## Auth0 Setup Guide

1. Create an Auth0 Application:
   - Go to Auth0 Dashboard
   - Create a new Native Application
   - Configure callback URLs
   - Note down credentials

2. Configure Auth0 Rules:
   - Create custom rules if needed
   - Configure token lifetime
   - Set up appropriate scopes

3. Web3Auth Configuration:
   - Create a custom verifier
   - Configure JWT validation
   - Set up Auth0 as an authentication method

## Common Issues and Solutions

1. **Auth0 Configuration**
   - Verify callback URLs
   - Check scope configuration
   - Handle CORS issues

2. **Token Handling**
   - Ensure proper token format
   - Handle token expiration
   - Implement token refresh logic

3. **Integration Issues**
   - Validate JWT format
   - Check verifier configuration
   - Handle authentication state properly

## Security Best Practices

- Secure token storage using Keychain
- Implement proper JWT validation
- Handle authentication state securely
- Use HTTPS for all network calls
- Regular security audits
- Follow Auth0 security guidelines

## Resources

- [Web3Auth iOS Documentation](https://web3auth.io/docs/sdk/pnp/ios)
- [Auth0 iOS QuickStart](https://auth0.com/docs/quickstart/native/ios-swift)
- [JWT Integration Guide](https://web3auth.io/docs/custom-authentication/jwt)
- [Auth0 Documentation](https://auth0.com/docs)
- [Web3Auth Dashboard](https://dashboard.web3auth.io)
- [Community Portal](https://community.web3auth.io)
- [Discord Support](https://discord.gg/web3auth)

## Contributing

We welcome contributions! Please feel free to submit issues and pull requests.

## License

This example is available under the MIT License. See the LICENSE file for more info.
