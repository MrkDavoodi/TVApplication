package com.example.tvapplication.launcher

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import java.io.BufferedReader
import java.io.InputStreamReader
data class AppInfo(val packageName:String , var enabled:Boolean , var name:String , var isLauncher:Boolean )

fun getAppListFromADB(context: Context): List<AppInfo> {


        val appList: MutableList<AppInfo> = ArrayList<AppInfo>()
        try {
            // Replace 'pm list packages' with your desired ADB command
            val process = Runtime.getRuntime().exec("pm list packages")
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            var line: String
            while (reader.readLine().also { line = it } != null) {
                val packageName = line.substring(line.indexOf(":") + 1)
                val appInfo = context.packageManager.getApplicationInfo(packageName, 0)
                val appName = context.packageManager.getApplicationLabel(appInfo).toString()

                if (packageName == context.packageName) continue
                appList.add(
                    AppInfo(
                        packageName,
                        true,
                        appName,
                        false
                    )
                ) // Assuming apps are enabled by default
            }
            process.waitFor()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val launchers = getLauncherApps(context.packageManager)
        launchers.forEach { lp ->
            val launcher = appList.find { it.packageName == lp }
            if (launcher != null) {
                launcher.isLauncher = true
            }

        }

        return appList.sortedBy {
            if (it.isLauncher) 0 else 1
        }

}

fun getLauncherApps(packageManager: PackageManager): List<String> {
    val launcherApps = mutableListOf<String>()

    // Create the intent to filter for launcher apps
    val intent = Intent(Intent.ACTION_MAIN)
    intent.addCategory(Intent.CATEGORY_LAUNCHER)
    intent.addCategory(Intent.CATEGORY_DEFAULT)
    intent.addCategory(Intent.CATEGORY_HOME)

    // Query the package manager for activities that match the intent
    val resolveInfoList: List<ResolveInfo> = packageManager.queryIntentActivities(intent, 0)

    // Extract package names of launcher apps
    for (resolveInfo in resolveInfoList) {
        val packageName = resolveInfo.activityInfo.packageName
        launcherApps.add(packageName)
    }

    return launcherApps
}