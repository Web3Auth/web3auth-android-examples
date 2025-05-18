# Web3Auth React Native Quick Start Example

This example demonstrates how to integrate Web3Auth into a React Native application using the Web3Auth React Native SDK. It provides a simple yet comprehensive example of implementing Web3Auth's authentication and blockchain functionality in a React Native app.

## 📝 Features
- Social login integration (Google, Facebook, Twitter, etc.)
- Ethereum wallet creation and management
- Basic blockchain interactions
- Secure key management
- Cross-platform support (iOS & Android)

## 🔗 Live Demo
Scan this QR code to try the example app:
[Add QR code image here]

## 🚀 Getting Started

### Prerequisites
- Node.js 14+
- React Native development environment set up ([React Native CLI Quickstart](https://reactnative.dev/docs/environment-setup))
- [Web3Auth Dashboard](https://dashboard.web3auth.io) account
- For iOS:
  - Xcode 13+
  - CocoaPods
- For Android:
  - Android Studio
  - JDK 11+

### Installation

1. Clone the repository:
```bash
git clone https://github.com/Web3Auth/web3auth-mobile-examples.git
cd web3auth-mobile-examples/react-native/rn-bare-quick-start
```

2. Install dependencies:
```bash
npm install
# or
yarn install
```

3. Install iOS dependencies (iOS only):
```bash
cd ios && pod install && cd ..
```

4. Configure Web3Auth:
   - Get your Client ID from the [Web3Auth Dashboard](https://dashboard.web3auth.io)
   - Update the Client ID in `App.tsx`:
   ```typescript
   const web3auth = new Web3Auth(
     new Web3AuthOptions({
       clientId: "YOUR-CLIENT-ID", // Get from Web3Auth Dashboard
       network: "testnet",
     })
   );
   ```

5. Configure OAuth (for social logins):
   - Follow the [OAuth Configuration Guide](https://web3auth.io/docs/guides/oauth-providers) to set up your providers
   - Update your OAuth credentials in the Web3Auth Dashboard

### Running the App

#### iOS
```bash
npm run ios
# or
yarn ios
```

#### Android
```bash
npm run android
# or
yarn android
```

## 💡 Implementation Details

### Key Files
- `App.tsx`: Main application file with Web3Auth integration
- `web3auth/`: Directory containing Web3Auth configuration and utilities
- `components/`: Reusable UI components

### Core Features Implementation

1. **Initialization**
```typescript
// Initialize Web3Auth
const web3auth = new Web3Auth(
  new Web3AuthOptions({
    clientId: "YOUR-CLIENT-ID",
    network: "testnet",
  })
);
```

2. **Authentication**
```typescript
// Login
await web3auth.login({
  loginProvider: "google",
});

// Logout
await web3auth.logout();
```

3. **Blockchain Interactions**
```typescript
// Get user info
const userInfo = await web3auth.getUserInfo();

// Get accounts
const provider = await web3auth.provider;
const web3 = new Web3(provider);
const accounts = await web3.eth.getAccounts();
```

## 🔒 Security Considerations

- Private keys are securely managed by Web3Auth
- Social login credentials are handled through OAuth
- Session management follows security best practices
- Secure storage implementation for persistent auth

## 🛠️ Troubleshooting

### Common Issues

1. **Build Errors**
   - Clean the build:
     ```bash
     cd android && ./gradlew clean && cd ..
     cd ios && pod deintegrate && pod install && cd ..
     ```

2. **OAuth Configuration**
   - Verify OAuth credentials in Web3Auth Dashboard
   - Check URL scheme configuration
   - Validate OAuth provider setup

3. **Network Issues**
   - Check internet connectivity
   - Verify network configuration in Web3Auth setup
   - Ensure proper SSL/TLS configuration

## 📚 Resources

- [Web3Auth Documentation](https://web3auth.io/docs)
- [React Native SDK Reference](https://web3auth.io/docs/sdk/pnp/react-native)
- [Integration Builder](https://web3auth.io/docs/integration-builder)
- [OAuth Setup Guide](https://web3auth.io/docs/guides/oauth-providers)

## 🤝 Support

- [Discord](https://discord.gg/web3auth)
- [GitHub Issues](https://github.com/Web3Auth/web3auth-mobile-examples/issues)
- [Web3Auth Support](https://web3auth.io/docs/troubleshooting/support)

## 📄 License

This example is available under the MIT License. See the [LICENSE](../../LICENSE) file for more info.
