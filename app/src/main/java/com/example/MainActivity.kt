package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.theme.ISYouthTheme
import com.example.ui.WorshipApp
import com.example.viewmodel.WorshipViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ISYouthTheme {
                val worshipViewModel: WorshipViewModel = viewModel()
                WorshipApp(viewModel = worshipViewModel)
            }
        }
    }
}

