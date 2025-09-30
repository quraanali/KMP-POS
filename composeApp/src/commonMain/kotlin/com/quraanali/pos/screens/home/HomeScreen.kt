package com.quraanali.pos.screens.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.quraanali.pos.R
import com.quraanali.pos.data.models.Product
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen() {
    val viewModel = koinViewModel<HomeViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    uiState.errorMessage?.let {
        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        viewModel.clearErrorMsg()
    }
    Box(Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = if (uiState.isConnected) "Online" else "Offline",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .background(
                        if (uiState.isConnected) Color.Green else Color.Red
                    ),
                color = Color.White,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )


            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                ProductsGrid(
                    modifier = Modifier
                        .weight(0.75f),
                    list = uiState.productsList,
                    onItemClick = { product ->
                        viewModel.addProductToCart(product)
                    },
                )
                CartView(
                    modifier = Modifier
                        .weight(0.25f)
                        .padding(16.dp), uiState = uiState,
                    viewModel = viewModel
                )
            }
        }

        if (uiState.isLoading) {
            ProgressLoader(Modifier.fillMaxSize())
        }
    }
}

@Composable
fun CartView(modifier: Modifier, uiState: HomeUiState, viewModel: HomeViewModel) {

    LazyColumn(modifier = modifier) {
        items(uiState.selectedProductList.size, key = { it }) { item ->
            CartItem(selectedProduct = uiState.selectedProductList[item], viewModel = viewModel)
        }

        if (uiState.total != null) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = uiState.total)
                        if (uiState.discount != null)
                            Text(text = uiState.discount, color = Color(0xFF653F03))
                    }
                    Button(onClick = {
                        viewModel.checkoutOrder()
                    }) {
                        Text("Checkout")
                    }
                }
            }
        }
    }

}


@Composable
fun CartItem(selectedProduct: SelectedProduct, viewModel: HomeViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
    ) {
        AsyncImage(
            model = selectedProduct.thumb,
            contentDescription = selectedProduct.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(40.dp)
        )

        Text(
            text = selectedProduct.name + " x " + selectedProduct.quantity + "\n"
                    + "Price: " + selectedProduct.price * selectedProduct.quantity,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
        )
        Icon(
            imageVector = Icons.Default.Delete, tint = Color.Red,
            contentDescription = "Delete",
            modifier = Modifier
                .size(22.dp)
                .clickable { viewModel.removeProductFromCart(selectedProduct) })

    }

}

@Composable
private fun ProductsGrid(
    list: List<Product>,
    onItemClick: (Product) -> Unit,
    modifier: Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(180.dp),
        contentPadding = WindowInsets.safeDrawing.asPaddingValues(),
        modifier = modifier,
    ) {
        items(list, key = { it.id }) { item ->
            GridListItem(
                item = item,
                onClick = { onItemClick(item) },
            )
        }
    }
}

@Composable
private fun GridListItem(
    item: Product,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .padding(8.dp)
            .clickable { onClick() }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(Color.LightGray)
        ) {
            AsyncImage(
                model = item.thumb,
                contentDescription = item.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            Icon(
                painterResource(id = if (item.taxable) R.drawable.with_tax else R.drawable.no_tax), // you can pick another icon if you like
                tint = Color.Unspecified,
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(if (item.taxable) (50.dp) else (32.dp))
            )

        }

        Spacer(Modifier.height(2.dp))

        Text(item.name, style = MaterialTheme.typography.titleMedium)
        Text(item.price.toString(), style = MaterialTheme.typography.bodyMedium)
        Text(item.taxable.toString(), style = MaterialTheme.typography.bodySmall)
    }

}
