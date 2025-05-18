# Web3Auth iOS Aptos Example

[![Web3Auth](https://img.shields.io/badge/Web3Auth-SDK-blue)](https://web3auth.io/docs/sdk/pnp/ios)
[![Web3Auth](https://img.shields.io/badge/Web3Auth-Community-cyan)](https://community.web3auth.io)

This example demonstrates how to integrate Web3Auth with Aptos blockchain in an iOS application, enabling secure wallet creation and Aptos network interactions.

## Features

- 📱 Native iOS integration
- 🔐 Social login support (Google, Facebook, Twitter, etc.)
- ⛓️ Aptos blockchain integration
- 💰 APT token management
- 🔑 Move module interactions
- 📝 Transaction signing
- 🔄 Account management
- 🎨 Customizable UI components

## Prerequisites

- Xcode 13.0 or higher
- iOS 13.0+ deployment target
- Swift 5.0 or higher
- CocoaPods or Swift Package Manager
- A Web3Auth account and client ID (get one at [Web3Auth Dashboard](https://dashboard.web3auth.io))
- Basic understanding of Aptos blockchain

## Tech Stack

- **Language**: Swift
- **Package Manager**: CocoaPods/SPM
- **Web3 Libraries**: 
  - `Web3Auth`: Core Web3Auth functionality
  - `AptosSwift`: Aptos Swift SDK
  - `TorusUtils`: Underlying key management
  - `Base58Swift`: Base58 encoding/decoding

## Installation

1. Clone the repository:
```bash
npx degit Web3Auth/web3auth-pnp-examples/ios/ios-aptos-example w3a-ios-aptos
```

2. Install dependencies:
   - Using CocoaPods:
   ```bash
   cd w3a-ios-aptos
   pod install
   ```
   - Or using Swift Package Manager:
     - Open the project in Xcode
     - File > Add Packages
     - Add the required SDK packages

3. Configure your project:
   - Open `w3a-ios-aptos.xcworkspace` (for CocoaPods) or `w3a-ios-aptos.xcodeproj` (for SPM)
   - Update Bundle Identifier
   - Add your Web3Auth client ID in the configuration
   - Configure URL Schemes for social login callbacks
   - Set up Aptos network endpoints (Mainnet/Testnet/Devnet)

4. Run the application:
   - Select your target device/simulator
   - Build and run (⌘ + R)

## Project Structure

```
Web3AuthAptos/
├── Sources/
│   ├── AppDelegate.swift          # Application delegate
│   ├── Web3AuthService.swift      # Web3Auth integration
│   ├── AptosService.swift         # Aptos operations
│   ├── TokenService.swift         # APT token operations
│   └── ViewControllers/           # UI controllers
├── Resources/                     # Assets and configs
└── Web3AuthAptos.xcodeproj       # Xcode project file
```

## Implementation Guide

### 1. Initialize Web3Auth with Aptos Configuration

```swift
let web3Auth = Web3Auth(
    clientId: "YOUR_CLIENT_ID",
    network: .testnet,
    redirectURL: "com.example.app://auth"
)
```

### 2. Handle Aptos Account Creation

```swift
func createAptosAccount(privateKey: String) throws -> Account {
    guard let keyData = Data(base58Decoding: privateKey) else {
        throw Web3AuthError.invalidPrivateKey
    }
    return try Account(privateKey: keyData)
}
```

### 3. Perform Aptos Transactions

```swift
func sendAPT(to recipient: String, amount: UInt64) async throws {
    let transaction = try await aptosClient.transfer(
        to: recipient,
        amount: amount
    )
    let hash = try await aptosClient.submitTransaction(transaction)
    print("Transaction sent: \(hash)")
}
```

### 4. Handle Move Module Interactions

```swift
func executeMove(module: String, function: String, args: [String]) async throws {
    let payload = try MovePayload(
        module: module,
        function: function,
        args: args
    )
    let result = try await aptosClient.executeMove(payload)
    print("Move execution result: \(result)")
}
```

## Common Issues and Solutions

1. **Build Issues**
   - Update CocoaPods/SPM dependencies
   - Clean build folder and rebuild
   - Check Aptos SDK compatibility

2. **Aptos Network Issues**
   - Verify network endpoint configuration
   - Check network status
   - Handle rate limiting appropriately

3. **Transaction Issues**
   - Ensure sufficient APT balance
   - Verify account permissions
   - Handle transaction timeouts

## Security Best Practices

- Secure private key storage using Keychain
- Implement proper transaction signing
- Handle failed transactions gracefully
- Validate all input addresses
- Regular security audits
- Follow Aptos security guidelines

## Resources

- [Web3Auth iOS Documentation](https://web3auth.io/docs/sdk/pnp/ios)
- [Aptos iOS Integration Guide](https://web3auth.io/docs/connect-blockchain/aptos)
- [Aptos Swift SDK](https://github.com/ALCOVE-LAB/aptos-swift-sdk)
- [Aptos Documentation](https://aptos.dev)
- [Web3Auth Dashboard](https://dashboard.web3auth.io)
- [Community Portal](https://community.web3auth.io)
- [Discord Support](https://discord.gg/web3auth)

## Contributing

We welcome contributions! Please feel free to submit issues and pull requests.

## License

This example is available under the MIT License. See the LICENSE file for more info.
