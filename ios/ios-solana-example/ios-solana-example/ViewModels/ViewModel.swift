//
//  ViewModel.swift
//  ios-solana-example
//
//  Created by Ayush B on 26/02/24.
//

import Foundation
import Web3Auth

class ViewModel: ObservableObject {
    var web3AuthHelper: Web3AuthHelper!
    
    @Published var isUserAuthenticated: Bool = false
    @Published var isErrorAvailable: Bool = false
    var error: String = ""
    
    
    func initilize() {
        Task {
            web3AuthHelper = Web3AuthHelper()
            try await web3AuthHelper.initialize()
            DispatchQueue.main.async {
                self.isUserAuthenticated = self.web3AuthHelper.isUserAuthenticated()
            }
        }
    }
    
    func getSolanaPrivateKey() throws -> String {
        return try web3AuthHelper.getSolanaPrivateKey()
    }
    
    func logOut() {
        Task {
            do {
                try await web3AuthHelper.logOut()
                DispatchQueue.main.async {
                    self.isUserAuthenticated = false
                }
            } catch let error {
                DispatchQueue.main.async {
                    self.isErrorAvailable = true
                    self.error = error.localizedDescription
                }
                print(error)
            }
        }
    }
    
    func login(){
        Task {
            do {
                try await web3AuthHelper.login()
                DispatchQueue.main.async {
                    self.isUserAuthenticated = true
                }
            } catch let error {
                DispatchQueue.main.async {
                    self.isErrorAvailable = true
                    self.error = error.localizedDescription
                }
                print(error.localizedDescription)
            }
        }
    }
    
    func getUserInfo() throws -> Web3AuthUserInfo {
        try web3AuthHelper.getUserDetails()
    }
}
