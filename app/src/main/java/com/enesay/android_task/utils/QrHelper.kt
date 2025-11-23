package com.enesay.android_task.utils

import android.content.Context
import android.widget.Toast
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

fun startQrCodeScanner(context: Context, onResult: (String) -> Unit) {
    val moduleInstallClient = ModuleInstall.getClient(context)
    val scannerClient = GmsBarcodeScanning.getClient(context)

    moduleInstallClient
        .areModulesAvailable(scannerClient)
        .addOnSuccessListener { response ->

            if (response.areModulesAvailable()) {
                scannerClient.startScan()
                    .addOnSuccessListener { barcode ->
                        barcode.rawValue?.let { code ->
                            onResult(code)
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            context,
                            "Scanning failed: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                Toast.makeText(
                    context,
                    "QR Scanner is downloading for first use.",
                    Toast.LENGTH_LONG
                ).show()

                val installRequest = ModuleInstallRequest.newBuilder()
                    .addApi(scannerClient)
                    .build()

                moduleInstallClient.installModules(installRequest)
                    .addOnSuccessListener {
                        if (it.areModulesAlreadyInstalled()) {
                            Toast.makeText(
                                context,
                                "Module is installed, Press the QR Button again!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            context,
                            "Installing failed!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }
        .addOnFailureListener {
            Toast.makeText(context, "Failed to access to Play Services", Toast.LENGTH_SHORT).show()
        }
}