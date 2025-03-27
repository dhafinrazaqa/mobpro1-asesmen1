package com.dhafinrazaqa0030.asesmen1mobpro1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.dhafinrazaqa0030.asesmen1mobpro1.ui.screen.MainScreen
import com.dhafinrazaqa0030.asesmen1mobpro1.ui.theme.AplikasiKonversiMataUangTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AplikasiKonversiMataUangTheme {
                MainScreen()
            }
        }
    }
}