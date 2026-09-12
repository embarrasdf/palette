//
//  ComposeViewController.swift
//  Palette
//
//  Created by embarrasdf on 10/20/24.
//

import SwiftUI
import App

struct MainView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        return MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
}
