package com.ag_apps.shopy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import com.ag_apps.core.presentation.designsystem.ShopyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShopyTheme {
                NavigationRoot(
                    modifier = Modifier,
                    isLoggedIn = true
                )
            }
        }
    }
}