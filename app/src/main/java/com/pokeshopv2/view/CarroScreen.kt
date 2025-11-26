package com.pokeshopv2.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.pokeshopv2.model.CarroItem
import com.pokeshopv2.viewmodel.CarroViewModel

@Composable
fun CarroScreen(
  navController: NavHostController,
  viewModel: CarroViewModel = viewModel(),
  onBack: () -> Unit
) {

  val cart = viewModel.cartItems

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp)
      .padding(bottom = 8.dp)
      .padding(top = 8.dp)
  ) {
    Text(text = "🛒 Tu carrito", modifier = Modifier.padding(bottom = 8.dp))

    Button(onClick = onBack)
    { Text("Volver") }
    if (cart.isEmpty()) {
      Text(text = "El carrito está vacío")
    } else {
      LazyColumn(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
      ) {
        items(cart) { cartItem ->
          CartItem(cartItem, viewModel,onRemove = { viewModel.removeProduct(cartItem) })
        }
      }

      Spacer(modifier = Modifier.height(12.dp))
      Text(text = "Total: $${viewModel.getTotal()}")
      Button(
        onClick = { navController.navigate("pago")},
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 8.dp)
      ) {
        Text("Finalizar compra")
      }
    }
  }
}

@Composable
fun CartItem(cartItem: CarroItem,viewModel: CarroViewModel , onRemove: () -> Unit) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp)
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(8.dp)
    ) {


      Column(
        modifier = Modifier
          .weight(1f)
          .padding(start = 12.dp)
      ) {
        Text(text = cartItem.producto.nombre)
        Text(text = "$${cartItem.producto.precio}")
      }
      Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ){ IconButton(onClick = {viewModel.decreaseQuantity(cartItem)}) {
        Text("-")
      }
        Text(
          text = "${cartItem.quantity}",
          modifier = Modifier.padding(horizontal = 8.dp)
        )
        IconButton(onClick = {viewModel.increaseQuantity(cartItem)}) {
          Text("+")
        }
        Text(
          text = "$${cartItem.producto.precio * cartItem.quantity}",
        )
      }

      IconButton(onClick = onRemove) {
        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
      }
    }
  }
}

