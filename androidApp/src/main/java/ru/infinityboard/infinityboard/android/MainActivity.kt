package ru.infinityboard.infinityboard.android

import AppTheme
import Application
import PlatformSDK
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import di.PlatformConfiguration

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PlatformSDK.init(PlatformConfiguration(this))
        setContent {
            AppTheme {
                Application()
            }
        }
    }
}