package com.quraanali.pos.screens.detail

import androidx.compose.runtime.Composable
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DetailScreen(
    navigateBack: () -> Unit,
) {
    val viewModel = koinViewModel<DetailViewModel>()

}
