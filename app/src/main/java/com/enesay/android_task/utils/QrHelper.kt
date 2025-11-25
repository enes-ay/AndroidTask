package com.enesay.android_task.utils

import android.content.Context
import android.widget.Toast
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

fun startQrCodeScanner(
    context: Context, onResult: (String) -> Unit,
    onError: (String) -> Unit
) {
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
                        onError("Scanning failed: ${e.message}")
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
                            onError("Module is installed, Press the QR Button again!")
                        }
                    }
                    .addOnFailureListener { e ->
                        onError(e.localizedMessage ?: "Module installation error")
                    }
                    .addOnCompleteListener {
                        onError("Module is installed, Press the QR Button again!")
                    }
            }
        }
        .addOnFailureListener { e ->
            onError(e.localizedMessage ?: "QR scanning error")
        }
}