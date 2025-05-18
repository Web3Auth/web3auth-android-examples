# Web3Auth iOS Solana Example

[![Web3Auth](https://img.shields.io/badge/Web3Auth-SDK-blue)](https://web3auth.io/docs/sdk/pnp/ios)
[![Web3Auth](https://img.shields.io/badge/Web3Auth-Community-cyan)](https://community.web3auth.io)

This example demonstrates how to integrate Web3Auth with Solana blockchain in an iOS application, enabling secure wallet creation and Solana network interactions.

## Features

- 📱 Native iOS integration
- 🔐 Social login support (Google, Facebook, Twitter, etc.)
- ⛓️ Solana blockchain integration
- 💰 SOL token management
- 🔑 SPL token support
- 📝 Transaction signing
- 🔄 Account management
- 🎨 Customizable UI components

## Prerequisites

- Xcode 13.0 or higher
- iOS 13.0+ deployment target
- Swift 5.0 or higher
- CocoaPods or Swift Package Manager
- A Web3Auth account and client ID (get one at [Web3Auth Dashboard](https://dashboard.web3auth.io))
- Basic understanding of Solana blockchain

## Tech Stack

- **Language**: Swift
- **Package Manager**: CocoaPods/SPM
- **Web3 Libraries**: 
  - `Web3Auth`: Core Web3Auth functionality
  - `Solana`: Solana Swift SDK
  - `TorusUtils`: Underlying key management
  - `Base58Swift`: Base58 encoding/decoding

## Installation

1. Clone the repository:
```bash
npx degit Web3Auth/web3auth-pnp-examples/ios/ios-solana-example w3a-ios-solana
```

2. Install dependencies:
   - Using CocoaPods:
   ```bash
   cd w3a-ios-solana
   pod install
   ```
   - Or using Swift Package Manager:
     - Open the project in Xcode
     - File > Add Packages
     - Add the required SDK packages

3. Configure your project:
   - Open `w3a-ios-solana.xcworkspace` (for CocoaPods) or `w3a-ios-solana.xcodeproj` (for SPM)
   - Update Bundle Identifier
   - Add your Web3Auth client ID in the configuration
   - Configure URL Schemes for social login callbacks
   - Set up Solana network endpoints (Mainnet/Devnet/Testnet)

4. Run the application:
   - Select your target device/simulator
   - Build and run (⌘ + R)

## Project Structure

```
Web3AuthSolana/
├── Sources/
│   ├── AppDelegate.swift          # Application delegate
│   ├── Web3AuthService.swift      # Web3Auth integration
│   ├── SolanaService.swift        # Solana operations
│   ├── TokenService.swift         # SPL token operations
│   └── ViewControllers/           # UI controllers
├── Resources/                     # Assets and configs
└── Web3AuthSolana.xcodeproj      # Xcode project file
```

## Implementation Guide

### 1. Initialize Web3Auth with Solana Configuration

```swift
let web3Auth = Web3Auth(
    clientId: "YOUR_CLIENT_ID",
    network: .testnet,
    redirectURL: "com.example.app://auth",
    chainConfig: ChainConfig(
        chainNamespace: "solana",
        chainId: "0x1", // Mainnet
        rpcTarget: "https://api.mainnet-beta.solana.com"
    )
)
```

### 2. Handle Solana Account Creation

```swift
func createSolanaAccount(privateKey: String) throws -> Account {
    guard let keyData = Data(base58Decoding: privateKey) else {
        throw Web3AuthError.invalidPrivateKey
    }
    return try Account(secretKey: keyData)
}
```

### 3. Perform Solana Transactions

```swift
func sendSOL(to recipient: String, amount: UInt64) async throws {
    let transaction = try await solana.action.sendSOL(
        to: recipient,
        amount: amount
    )
    let signature = try await solana.action.sendTransaction(transaction)
    print("Transaction sent: \(signature)")
}
```

### 4. Handle SPL Tokens

```swift
func getTokenBalance(mint: String, owner: String) async throws -> UInt64 {
    let tokenProgram = TokenProgram(solana: solana)
    return try await tokenProgram.getBalance(
        mint: PublicKey(string: mint),
        owner: PublicKey(string: owner)
    )
}
```

## Common Issues and Solutions

1. **Build Issues**
   - Update CocoaPods/SPM dependencies
   - Clean build folder and rebuild
   - Check Solana SDK compatibility

2. **Solana Network Issues**
   - Verify RPC endpoint configuration
   - Check network status
   - Handle rate limiting appropriately

3. **Transaction Issues**
   - Ensure sufficient SOL balance for fees
   - Verify account permissions
   - Handle transaction timeouts

## Security Best Practices

- Secure private key storage using Keychain
- Implement proper transaction signing
- Handle failed transactions gracefully
- Validate all input addresses
- Regular security audits
- Follow Solana security guidelines

## Resources

- [Web3Auth iOS Documentation](https://web3auth.io/docs/sdk/pnp/ios)
- [Solana iOS Integration Guide](https://web3auth.io/docs/connect-blockchain/solana)
- [Solana Swift SDK](https://github.com/solana-labs/solana-swift)
- [Solana Documentation](https://docs.solana.com)
- [Web3Auth Dashboard](https://dashboard.web3auth.io)
- [Community Portal](https://community.web3auth.io)
- [Discord Support](https://discord.gg/web3auth)

## Contributing

We welcome contributions! Please feel free to submit issues and pull requests.

## License

This example is available under the MIT License. See the LICENSE file for more info.
