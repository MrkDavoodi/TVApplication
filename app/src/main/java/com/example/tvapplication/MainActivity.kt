package com.example.tvapplication

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import com.example.tvapplication.admin.MyDeviceAdminReceiver
import com.example.tvapplication.navigation.LocalNavigation
import com.example.tvapplication.navigation.MainGraph
import com.example.tvapplication.navigation.Navigation
import com.example.tvapplication.ui.theme.TVApplicationTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

//    companion object {
//        init {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//        }
//    }

    private val inputKeys = arrayListOf<Int?>()
    private val secretKey = listOf(
        KeyEvent.KEYCODE_1, KeyEvent.KEYCODE_2,
        KeyEvent.KEYCODE_3, KeyEvent.KEYCODE_4
    )

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        inputKeys.add(keyCode)
        if (inputKeys == secretKey) {
            finish()
        } else {
            return false
        }

        return super.onKeyDown(keyCode, event)
    }

    @Deprecated(
        "Deprecated in Java",
        ReplaceWith("super.onBackPressed()", "androidx.activity.ComponentActivity")
    )
    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onStart() {
        super.onStart()
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

    }

    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                TVApplicationTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        shape = RectangleShape
                    ) {
                        val context = LocalContext.current

                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        val navController = rememberNavController()
                        val navigation = remember {
                            Navigation(navController)
                        }

                        CompositionLocalProvider(LocalNavigation provides navigation) {
                            MainGraph(navController, this@MainActivity)
                        }

                    }
                }
            }
        }
        hideSystemUI()
    }

    private fun hideSystemUI() {

        //Hides the ugly action bar at the top
        actionBar?.hide()

        //Hide the status bars

        WindowCompat.setDecorFitsSystemWindows(window, false)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            window.insetsController?.apply {
                hide(WindowInsets.Type.statusBars())
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TVApplicationTheme {
//        Greeting("Android")
    }
}