//
//  SceneDelegate.swift
//  TeamTalk
//
//  Created by Hailey Oh.
//

import UIKit
import FirebaseAuth

class SceneDelegate: UIResponder, UIWindowSceneDelegate {

    var window: UIWindow?


    func scene(_ scene: UIScene, willConnectTo session: UISceneSession, options connectionOptions: UIScene.ConnectionOptions) {
       
        guard let windowScene = (scene as? UIWindowScene) else { return }
        
        let window = UIWindow(windowScene: windowScene)
        
        let rootViewController: UIViewController
        
        if AuthManager.shared.isSignedIn {
            rootViewController = AccountViewController()
            
            if let email = Auth.auth().currentUser?.email {
                CallManager.shared.setUp(email: email)
            }
        } else {
            rootViewController = WelcomeViewController()
        }
        
        
        window.rootViewController = UINavigationController(rootViewController: rootViewController)
        window.makeKeyAndVisible()
        self.window = window
    }

  


}

