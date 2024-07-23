package com.example.tvapplication

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import com.example.tvapplication.commons.restartApp
import com.example.tvapplication.data.ApiURL
import com.example.tvapplication.data.local.SharedPreferencesHelper
import com.example.tvapplication.data.local.SharedPreferencesHelper.Companion.BASE_URL_KEY
import com.example.tvapplication.di.DynamicBaseUrlProvider
import com.example.tvapplication.navigation.LocalNavigation
import com.example.tvapplication.navigation.MainGraph
import com.example.tvapplication.navigation.Navigation
import com.example.tvapplication.ui.home.CustomDialog
import com.example.tvapplication.ui.theme.TVApplicationTheme
import com.example.tvapplication.ui.video.VideoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var dynamicBaseUrlProvider: DynamicBaseUrlProvider

    private lateinit var requestMultiplePermission: ActivityResultLauncher<Array<String>>
    val showDialog = mutableStateOf(false)

    private val inputKeys = arrayListOf<Int?>()

    //    private val secretKey = listOf(
//        KeyEvent.KEYCODE_1, KeyEvent.KEYCODE_2,
//        KeyEvent.KEYCODE_3, KeyEvent.KEYCODE_4
//    )
    val secretKey = listOf(
        KeyEvent.KEYCODE_DPAD_DOWN,
        KeyEvent.KEYCODE_DPAD_DOWN,
        KeyEvent.KEYCODE_DPAD_UP,
        KeyEvent.KEYCODE_DPAD_UP,
        KeyEvent.KEYCODE_DPAD_RIGHT,
        KeyEvent.KEYCODE_DPAD_RIGHT
    )
    val secretKeyExit = listOf(
        KeyEvent.KEYCODE_DPAD_DOWN,
        KeyEvent.KEYCODE_DPAD_UP,
        KeyEvent.KEYCODE_DPAD_UP,
        KeyEvent.KEYCODE_DPAD_UP,
        KeyEvent.KEYCODE_DPAD_LEFT
    )
    var validSecretKeyIndex = 0
    var validSecretKeyExitIndex = 0

    @SuppressLint("RestrictedApi")
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        super.dispatchKeyEvent(event)

        if (event.action == KeyEvent.ACTION_DOWN) {
            Log.d("TV_KEY_DOWN_LOG", event.keyCode.toString() + ":" + validSecretKeyIndex)
            handleSecretMenu(event)
            handleSecretExit(event)

        }
        return false
    }

    private fun handleSecretMenu(event: KeyEvent) {
        if (secretKey[validSecretKeyIndex] == event.keyCode) {
            validSecretKeyIndex++
        } else {
            validSecretKeyIndex = 0
        }
        if (validSecretKeyIndex == secretKey.size) {
            validSecretKeyIndex = 0
            showDialog.value = true
        }
    }

    private fun handleSecretExit(event: KeyEvent) {
        if (secretKeyExit[validSecretKeyExitIndex] == event.keyCode) {
            validSecretKeyExitIndex++
        } else {
            validSecretKeyExitIndex = 0
        }
        if (validSecretKeyExitIndex == secretKeyExit.size) {
            validSecretKeyExitIndex = 0
            finish()
        }
    }

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

        dynamicBaseUrlProvider.setBaseUrl(
            SharedPreferencesHelper(this).getDataFromSharPrf(
                BASE_URL_KEY,
                "https://mrk.co.ir/"
            )
        )
        requestMultiplePermission = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            var isGranted = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                it.forEach { (s, b) ->
                    isGranted = b
                }
            }

            if (!isGranted) {
                Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_SHORT).show()
            }
        }

        setContent {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                TVApplicationTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        shape = RectangleShape
                    ) {
                        val context = LocalContext.current
                        hideSystemUI()

                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        requestMultiplePermission.launch(
                            arrayOf(
                                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                            )
                        )
                        val videoListViewModel: VideoViewModel by viewModels()
                        val videos by videoListViewModel.videos.collectAsStateWithLifecycle()

                        val navController = rememberNavController()
                        val navigation = remember {
                            Navigation(navController)
                        }

                        CompositionLocalProvider(LocalNavigation provides navigation) {
                            MainGraph(navController, this@MainActivity)
                        }
                        if (showDialog.value)
                            CustomDialog(value = "", setShowDialog = {
                                showDialog.value = it
                            }) {
                                SharedPreferencesHelper(context).saveDataInSharPrf(BASE_URL_KEY, it)
                                Log.i("HomePage", "HomePage : $it")
                                restartApp(context)
                            }

                    }
                }
            }
        }
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