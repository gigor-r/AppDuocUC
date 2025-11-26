package com.pokeshopv2.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pokeshopv2.R
import com.pokeshopv2.viewmodel.CarroViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagoScreen(
  cartViewModel: CarroViewModel,
  onBack: () -> Unit,
  onConfirm: () -> Unit,
  navController : NavHostController
) {
  var selectedMethod by remember { mutableStateOf<String?>(null) }

  Scaffold(
    topBar = {
      CenterAlignedTopAppBar(
        title = { Text("Método de Pago") },
        navigationIcon = {
          IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
          }
        }
      )
    }
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .padding(20.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
      Text(
        "Selecciona tu método de pago",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
      )

      MetodoPagoCard(
        titulo = "Tarjeta de Crédito / Débito",
        descripcion = "Visa, MasterCard o PokéBank",
        icono = R.drawable.ic_tarjeta,
        seleccionado = selectedMethod == "tarjeta",
        onClick = { selectedMethod = "tarjeta" }
      )

      MetodoPagoCard(
        titulo = "Transferencia Bancaria",
        descripcion = "Cuenta corriente o vista",
        icono = R.drawable.ic_transferencia,
        seleccionado = selectedMethod == "transferencia",
        onClick = { selectedMethod = "transferencia" }
      )

      MetodoPagoCard(
        titulo = "Efectivo",
        descripcion = "Pago presencial al entregar",
        icono = R.drawable.ic_efectivo,
        seleccionado = selectedMethod == "efectivo",
        onClick = { selectedMethod = "efectivo" }
      )

      Spacer(modifier = Modifier.height(20.dp))

      Divider()

      Text(
        text = "Total a pagar: $${cartViewModel.getTotal()}",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.align(Alignment.End)
      )

      Spacer(modifier = Modifier.height(20.dp))

      Button(
        onClick = {
          if (selectedMethod != null) {
            onConfirm()
            cartViewModel.clearCart()
            navController.navigate("confirmarPago")
          }
        },
        enabled = selectedMethod != null,
        modifier = Modifier
          .fillMaxWidth()
          .height(55.dp),
        shape = RoundedCornerShape(12.dp)
      ) {
        Text("Confirmar pago", fontWeight = FontWeight.Bold)
      }
    }
  }
}

@Composable
fun MetodoPagoCard(
  titulo: String,
  descripcion: String,
  icono: Int,
  seleccionado: Boolean,
  onClick: () -> Unit
) {
  Card(
    colors = CardDefaults.cardColors(
      containerColor = if (seleccionado)
        MaterialTheme.colorScheme.primaryContainer
      else
        MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(4.dp),
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick),
    shape = RoundedCornerShape(12.dp)
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(16.dp)
    ) {
      Image(
        painter = painterResource(id = icono),
        contentDescription = titulo,
        modifier = Modifier.size(48.dp)
      )
      Column(
        Modifier
          .padding(start = 12.dp)
          .weight(1f)
      ) {
        Text(titulo, fontWeight = FontWeight.Bold)
        Text(descripcion, style = MaterialTheme.typography.bodySmall)
      }
      RadioButton(selected = seleccionado, onClick = onClick)
    }
  }
}
