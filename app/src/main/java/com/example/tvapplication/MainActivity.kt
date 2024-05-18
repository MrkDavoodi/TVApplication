package com.example.tvapplication

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.view.WindowCompat
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import com.example.tvapplication.launcher.BootCompletedReceiver
import com.example.tvapplication.ui.theme.TVApplicationTheme
import com.example.tvapplication.ui.video.ExoPlayerColumnAutoplayScreen
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        init {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private val inputKeys = arrayListOf<Int?>()
    private val secretKey = listOf(
        KeyEvent.KEYCODE_1, KeyEvent.KEYCODE_2,
        KeyEvent.KEYCODE_3, KeyEvent.KEYCODE_4
    )

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        inputKeys.add(keyCode)
        if (inputKeys == secretKey) {
            finish()
        }

        return super.onKeyDown(keyCode, event)
    }

    override fun onStart() {
        super.onStart()
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
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
                        Log.i("IN MainActivity", "There is AlarmReceiver IN MAin!!! $context")

                        val alarm = Intent(
                            context,
                            BootCompletedReceiver::class.java
                        )

                        val alarmRunning = PendingIntent.getBroadcast(
                            context,
                            1234,
                            alarm,
                            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
                        ) != null
//                        if (!alarmRunning) {
                            val pendingIntent = PendingIntent.getBroadcast(context, 1234, alarm, 0)
                            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                alarmManager.setAndAllowWhileIdle(
//                                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                                    60000,
//                                    pendingIntent
//                                )
//                            }
                            val calendar = Calendar.getInstance().apply {
                                set(Calendar.HOUR_OF_DAY, 13)
                                set(Calendar.MINUTE, 50)
                                set(Calendar.SECOND, 0)
                                set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)

                            }
                            val time = System.currentTimeMillis()
//                        alarmManager.cancel(pendingIntent)
//                            alarmManager.setRepeating(    AlarmManager.RTC_WAKEUP,
//                                System.currentTimeMillis(),
//                                60000,
//                                pendingIntent)
                            alarmManager.set(
                                AlarmManager.RTC_WAKEUP,
                                calendar.timeInMillis,
                                pendingIntent
                            )

//                            alarmManager.setRepeating(
//                                AlarmManager.RTC_WAKEUP,
//                                System.currentTimeMillis(),
//                                60000,
//                                pendingIntent
//                            )
//                        }




                        ExoPlayerColumnAutoplayScreen()
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