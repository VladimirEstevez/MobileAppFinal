package com.example.bookshelf.ui.screens.checkout_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookshelf.R
import com.example.bookshelf.model.Book
import com.example.bookshelf.ui.screens.beer_inventory_screen.*
import com.example.bookshelf.ui.theme.BookshelfTheme
import kotlin.math.absoluteValue


@Composable
fun FavoritesScreen(
    viewModel: QueryViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState(initial = QueryUiState.Loading)

    LaunchedEffect(viewModel) {
        viewModel.getBeers()
    }

    val books = when (val currentState = uiState) {
        is QueryUiState.Success -> currentState.bookshelfList
        else -> emptyList() // or handle other states if needed
    }

    LazyColumn(contentPadding = PaddingValues(vertical = 8.dp),) {
        items(books.size) { book ->
            FavoritesCard(books[book])
        }
    }
}


@Composable
fun FavoritesCard(item: Book) {
    // Your existing card composable, modified to use data from the item
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .shadow(4.dp),
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Image
            Image(
                painter = painterResource(id = R.drawable.fsm),//item.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .padding(8.dp)
                    .align(Alignment.CenterVertically),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.weight(1f))

            // Labels
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .fillMaxHeight(),
            ) {
                // Name label
                Text(
                    text = item.name,
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 4.dp)
                )

                // Details label
                Text(
                    text = (5*item.volume.value).toString(),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun MenuScreenPreview() {
    val viewModel : QueryViewModel = viewModel(factory = QueryViewModel.Factory)
    BookshelfTheme {
        FavoritesScreen(viewModel)
    }
}


