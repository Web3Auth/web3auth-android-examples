# Web3Auth React Native Auth0 Example

This example demonstrates how to integrate Web3Auth with Auth0 authentication in a React Native application. It showcases a custom authentication setup using Auth0 as the authentication provider with Web3Auth's blockchain functionality.

## 📝 Features
- Auth0 social login integration
- Custom authentication flow
- Ethereum wallet creation and management
- Basic blockchain interactions
- Secure key management
- Cross-platform support (iOS & Android)

## 🚀 Getting Started

### Prerequisites
- Node.js 14+
- React Native development environment ([React Native CLI Quickstart](https://reactnative.dev/docs/environment-setup))
- [Web3Auth Dashboard](https://dashboard.web3auth.io) account
- [Auth0 Account](https://auth0.com)
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
cd web3auth-mobile-examples/react-native/rn-bare-auth0-example
```

2. Install dependencies:
```bash
npm install
# or
yarn install
```

3. Install iOS dependencies:
```bash
cd ios && pod install && cd ..
```

### Configuration

1. Auth0 Setup:
   - Create a new application in [Auth0 Dashboard](https://manage.auth0.com)
   - Configure callback URLs:
     ```
     your.app.scheme://auth0.com/ios/your.bundle.id/callback
     your.app.scheme://auth0.com/android/your.package.name/callback
     ```
   - Note down Domain and Client ID

2. Web3Auth Setup:
   - Get your Client ID from [Web3Auth Dashboard](https://dashboard.web3auth.io)
   - Create a custom verifier with Auth0 configuration
   - Update configuration in `App.tsx`:
   ```typescript
   const web3auth = new Web3Auth(
     new Web3AuthOptions({
       clientId: "YOUR-WEB3AUTH-CLIENT-ID",
       network: "testnet",
       customVerifier: "YOUR-VERIFIER-NAME",
     })
   );
   ```

3. Update Auth0 credentials in `auth0-config.ts`:
```typescript
export const auth0Config = {
  domain: "YOUR-AUTH0-DOMAIN",
  clientId: "YOUR-AUTH0-CLIENT-ID",
};
```

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
- `App.tsx`: Main application with Web3Auth and Auth0 integration
- `auth0-config.ts`: Auth0 configuration
- `services/`: Web3Auth and blockchain services

### Core Features Implementation

1. **Auth0 Configuration**
```typescript
const auth0 = new Auth0Client({
  domain: auth0Config.domain,
  clientId: auth0Config.clientId,
});
```

2. **Web3Auth with Auth0**
```typescript
// Initialize custom authentication
const web3auth = new Web3Auth({
  clientId: "YOUR-CLIENT-ID",
  network: "testnet",
  customVerifier: "YOUR-VERIFIER",
});

// Login with Auth0
const login = async () => {
  const auth0Token = await auth0.authorize();
  const web3authProvider = await web3auth.connect({
    verifier: "YOUR-VERIFIER",
    verifierId: auth0Token.sub,
    idToken: auth0Token.idToken,
  });
};
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
- [Auth0 React Native Guide](https://auth0.com/docs/quickstart/native/react-native)
- [Custom Authentication Setup](https://web3auth.io/docs/guides/custom-authentication)
- [Auth0 Setup Guide](https://web3auth.io/docs/guides/auth0)

## 🤝 Support

- [Discord](https://discord.gg/web3auth)
- [GitHub Issues](https://github.com/Web3Auth/web3auth-mobile-examples/issues)
- [Web3Auth Support](https://web3auth.io/docs/troubleshooting/support)

## 📄 License

This example is available under the MIT License. See the [LICENSE](../../LICENSE) file for more info.
