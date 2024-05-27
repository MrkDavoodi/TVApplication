package com.example.tvapplication.launcher

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.tvapplication.util.executeCommand

class ScreenOffReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
//        val intent = context.applicationContext.packageManager.getLaunchIntentForPackage(context.applicationContext.packageName)
//        intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        context.applicationContext.startActivity(intent)
//        Utils.startActivity(context,MainActivity::class.java)

//        "cmd activity start com.example.tvapplication".runCommand()
//        executeCommand("cmd activity start com.example.tvapplication")
        executeCommand("am start -n com.example.tvapplication/.MainActivity")
        Log.i("in Receiver","test for command")
//        val devicePolicyManager =
//            context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
//        val componentName = ComponentName(context, ScreenOffReceiver::class.java)
//
//        if (devicePolicyManager.isAdminActive(componentName)) {
//            devicePolicyManager.lockNow()
//        } else {
//            // Request device admin permission if not already granted
//            val adminIntent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
//            adminIntent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName)
//            adminIntent.putExtra(
//                DevicePolicyManager.EXTRA_ADD_EXPLANATION,
//                "Please allow this app to lock the screen"
//            )}
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                adminIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            }
//
//            if (intent.action == "ofTime") {
//                (context.applicationContext  as? Activity)?.finishAffinity()
//                exitProcess(0)
//            } else {
////            context.startActivity(adminIntent)
//                val intent = context.packageManager?.getLaunchIntentForPackage(context.packageName)
//                intent?.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                context.startActivity(intent)
//            }
//        }

    }

}