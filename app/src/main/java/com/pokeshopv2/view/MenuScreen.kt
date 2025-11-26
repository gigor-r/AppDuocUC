package com.pokeshopv2.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.pokeshopv2.R
import com.pokeshopv2.model.Producto
import com.pokeshopv2.viewmodel.CarroViewModel
import com.pokeshopv2.viewmodel.MenuViewModel

val pkmnfont = FontFamily(Font(R.font.pkmnfont))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    navController: NavHostController,
    menuViewModel: MenuViewModel,
    carroViewModel: CarroViewModel
) {

    val productosFiltrados by menuViewModel.productosFiltrados.collectAsState()
    val cargando by menuViewModel.cargando.collectAsState()
    val errorApi by menuViewModel.errorApi.collectAsState()

    var isSearchActive by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var selectedItem by remember { mutableStateOf(0) }
    val navItems = listOf("Inicio", "Categorías", "Perfil")
    val cartItemCount = carroViewModel.cartItems.size

    Scaffold(
        topBar = {
            if (isSearchActive) {
                // Barra de búsqueda activa
                OutlinedTextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                        menuViewModel.filtrarProductos(it)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    placeholder = { Text("Buscar productos...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
                    trailingIcon = {
                        IconButton(onClick = {
                            searchText = ""
                            menuViewModel.filtrarProductos("")
                            isSearchActive = false
                            keyboardController?.hide()
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar búsqueda")
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = {
                        focusManager.clearFocus()
                    })
                )
            } else {
                // Barra superior normal
                CenterAlignedTopAppBar(
                    title = { Text("PokéShop", fontFamily = pkmnfont) },
                    actions = {
                        IconButton(onClick = { navController.navigate("qr") }) {
                            Icon(imageVector = Icons.Default.QrCodeScanner, contentDescription = "Escanear QR")
                        }
                        IconButton(onClick = { isSearchActive = true }) {
                            Icon(Icons.Default.Search, contentDescription = "Buscar")
                        }
                        IconButton(onClick = { navController.navigate("Carrito") }) {
                            BadgedBox(
                                badge = {
                                    if (cartItemCount > 0) {
                                        Badge { Text(cartItemCount.toString()) }
                                    }
                                }
                            ) {
                                Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                            }
                        }
                    }
                )
            }
        },
        bottomBar = {
            NavigationBar {
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            when (item) {
                                "Inicio" -> Icon(Icons.Default.Home, contentDescription = item)
                                "Categorías" -> Icon(Icons.Outlined.Category, contentDescription = item)
                                "Perfil" -> Icon(Icons.Default.Person, contentDescription = item)
                            }
                        },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = { /* Lógica de navegación del BottomBar */ }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (cargando) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (errorApi != null) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = errorApi!!, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { menuViewModel.recargarProductos() }) {
                        Text("Reintentar")
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item(span = { GridItemSpan(2) }) { Banner() }
                    item(span = { GridItemSpan(2) }) { Spacer(modifier = Modifier.height(8.dp)) }
                    item(span = { GridItemSpan(2) }) { CategoryCarousel() }
                    item(span = { GridItemSpan(2) }) { Spacer(modifier = Modifier.height(8.dp)) }
                    item(span = { GridItemSpan(2) }) { Text("Recomendado para ti", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }

                    items(productosFiltrados) { product ->
                        ProductCard(product = product, viewModel = carroViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun Banner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = R.drawable.promo),
                contentDescription = "Banner de promoción",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun CategoryCarousel() {
    val categories = listOf("Novedades", "PokéBalls", "Mt's", "Medicina")
    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(categories) {
            ElevatedSuggestionChip(onClick = {}, label = { Text(it) })
        }
    }
}

@Composable
fun ProductCard(product: Producto, modifier: Modifier = Modifier, viewModel: CarroViewModel) {
    Card(
        modifier = modifier.height(250.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column {
            AsyncImage(
                model = product.imagen,
                contentDescription = "Imagen de ${product.nombre}",
                modifier = Modifier.fillMaxWidth().height(120.dp),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.prod01pokeball),
                error = painterResource(id = R.drawable.prod01pokeball)
            )
            Column(
                modifier = Modifier.padding(8.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(product.nombre, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, maxLines = 1)
                Text("$${product.precio}", style = MaterialTheme.typography.bodyMedium)
                Button(
                    onClick = { viewModel.addProduct(product) },
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text("Agregar")
                }
            }
        }
    }
}
