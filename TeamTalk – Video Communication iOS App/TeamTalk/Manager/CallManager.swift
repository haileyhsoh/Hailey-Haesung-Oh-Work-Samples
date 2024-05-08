//
//  CallManager.swift
//  MyFacetime
//
//  Created by Hailey Oh on 4/8/24.
//

import Foundation
import StreamVideo
import StreamVideoSwiftUI
import StreamVideoUIKit

class CallManager {
    static let shared = CallManager()
    
    struct Constants {
        static let userToken =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiUHJpbmNlc3NfTGVpYSIsImlzcyI6Imh0dHBzOi8vcHJvbnRvLmdldHN0cmVhbS5pbyIsInN1YiI6InVzZXIvUHJpbmNlc3NfTGVpYSIsImlhdCI6MTcxMjU3NjEyNywiZXhwIjoxNzEzMTgwOTMyfQ.ESB0wnChlUNF_SkJ9Br_hh5LZN2eOpEev1Xa88uduJk"
    }
    
    private var video: StreamVideo?
    private var videoUI: StreamVideoUI?
    public private(set) var callViewModel: CallViewModel?
    
    struct UserCredentials {
        let user: User
        let token: UserToken
    }
    
    func setUp(email: String) {
        setUpCallViewModel()
        // UserCredential
        let credential = UserCredentials(
            user: .guest(email),
            token: UserToken(rawValue: Constants.userToken)
        )
        // StreamVideo
        let video = StreamVideo(
            apiKey: "9ezkzx7gvgsb",
            user: credential.user,
            token: credential.token) {result in
                // Refresh token via real backend
                result(.success(credential.token))
            }
        // StreamVideoUI
        let videoUI = StreamVideoUI(streamVideo: video)
        
        self.video = video
        self.videoUI = videoUI
        
        
    }
    
    private func setUpCallViewModel() {
        guard callViewModel == nil else { return }
        DispatchQueue.main.async {
            self.callViewModel = CallViewModel()
        }
    }
}
