package com.example.tvapplication.util

import java.io.BufferedReader
import java.io.InputStreamReader

fun executeCommand(command: String): Boolean {
    val process = Runtime.getRuntime().exec(command)
    val reader = BufferedReader(InputStreamReader(process.inputStream))
    val errorReader = BufferedReader(InputStreamReader(process.errorStream))
    var line: String?

    while (reader.readLine().also { line = it } != null) {
        // Process command output if needed
    }

    while (errorReader.readLine().also { line = it } != null) {
        // Handle error output if needed
    }

    val exitCode = process.waitFor()

    return exitCode == 0
}