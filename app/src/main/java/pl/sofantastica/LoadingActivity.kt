package pl.sofantastica

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import pl.sofantastica.ui.auth.LoginScreen
import pl.sofantastica.ui.loading.LoadingScreen
import pl.sofantastica.ui.theme.SofantasticaTheme

@AndroidEntryPoint
class LoadingActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SofantasticaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    LoadingScreen(afterDataIsLoaded = {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    })
                }
            }
        }
    }

}