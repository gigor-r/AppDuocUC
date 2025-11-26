package com.pokeshopv2.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.pokeshopv2.model.Producto
import com.pokeshopv2.model.CarroItem

class CarroViewModel : ViewModel() {

  //Lista observable del carrito
  private val _cartItems = mutableStateListOf<CarroItem>()
  val cartItems: List<CarroItem> get() = _cartItems

  //Agregar producto (permite definir cuántas unidades)
  fun addProduct(productToAdd: Producto, cantidad: Int = 1) {
    val existingItem = _cartItems.find { it.producto.nombre == productToAdd.nombre }
    if (existingItem != null) {
      val index = _cartItems.indexOf(existingItem)
      if (index != -1) {
        _cartItems[index] = existingItem.copy(quantity = existingItem.quantity + cantidad)
      }
    } else {
      _cartItems.add(CarroItem(productToAdd, quantity = cantidad))
    }
  }

  //Aumentar cantidad en el carrito
  fun increaseQuantity(cartItem: CarroItem) {
    val index = _cartItems.indexOf(cartItem)
    if (index != -1) {
      val current = _cartItems[index]
      _cartItems[index] = current.copy(quantity = current.quantity + 1)
    }
  }

  //Disminuir cantidad (si llega a 0, elimina el producto)
  fun decreaseQuantity(cartItem: CarroItem) {
    val index = _cartItems.indexOf(cartItem)
    if (index != -1) {
      val current = _cartItems[index]
      if (current.quantity > 1) {
        _cartItems[index] = current.copy(quantity = current.quantity - 1)
      } else {
        _cartItems.removeAt(index)
      }
    }
  }

  //Eliminar producto completamente
  fun removeProduct(cartItem: CarroItem) {
    _cartItems.remove(cartItem)
  }

  //Vaciar el carrito
  fun clearCart() {
    _cartItems.clear()
  }

  //Calcular el total
  fun getTotal(): Double {
    return _cartItems.sumOf { it.producto.precio * it.quantity }
  }
}