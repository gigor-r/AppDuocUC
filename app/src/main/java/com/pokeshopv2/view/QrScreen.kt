package com.pokeshopv2.view

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

@Composable
fun QrScreen(navController: NavController) {
    val context = LocalContext.current

    val scanLauncher = rememberLauncherForActivityResult(
        contract = ScanContract(),
        onResult = { result ->
            if (result.contents == null) {
                Toast.makeText(context, "Escaneo cancelado", Toast.LENGTH_SHORT).show()
            } else {
                val qrValue = result.contents
                Toast.makeText(context, "QR Escaneado: $qrValue", Toast.LENGTH_LONG).show()
            }
            navController.popBackStack()
        }
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            if (isGranted) {
                val options = ScanOptions()
                options.setPrompt("Escanea un código QR")
                options.setBeepEnabled(true)
                options.setOrientationLocked(false)
                scanLauncher.launch(options)
            } else {
                Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }
        }
    )

    LaunchedEffect(key1 = true) {
        when (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)) {
            PackageManager.PERMISSION_GRANTED -> {
                val options = ScanOptions()
                options.setPrompt("Escanea un código QR")
                options.setBeepEnabled(true)
                options.setOrientationLocked(false)
                scanLauncher.launch(options)
            }
            else -> {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Solicitando permiso de cámara...")
    }
}
