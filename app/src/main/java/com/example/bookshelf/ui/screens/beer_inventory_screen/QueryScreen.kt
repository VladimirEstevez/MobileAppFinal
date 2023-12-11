package com.example.bookshelf.ui.screens.beer_inventory_screen

import android.view.KeyEvent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookshelf.R
import com.example.bookshelf.model.Book
import com.example.bookshelf.ui.screens.components.ErrorScreen
import com.example.bookshelf.ui.screens.components.LoadingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QueryScreen(
    modifier: Modifier = Modifier,
    viewModel: QueryViewModel,
    retryAction: () -> Unit,
) {
    val uiState = viewModel.uiState.collectAsState().value
    val uiStateQuery = viewModel.uiStateSearch.collectAsState().value

    val focusManager = LocalFocusManager.current

    // LaunchedEffect to trigger the API request on page load
    LaunchedEffect(key1 = Unit) {
        viewModel.getBooks(uiStateQuery.query)
    }

    Column {
        Text(
            text = "Search our beer inventory!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        OutlinedTextField(
            value = uiStateQuery.query,
            onValueChange = { viewModel.updateQuery(it) },
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "searchIcon"
                )
            },
            placeholder = {
                Text(text = stringResource(R.string.search_placeholder))
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    focusManager.clearFocus()
                    viewModel.getBooks(uiStateQuery.query)
                }
            ),
            modifier = Modifier
                .onKeyEvent { e ->
                    if (e.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                        focusManager.clearFocus()
                        viewModel.getBooks(uiStateQuery.query)
                    }
                    false
                }
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, top = 8.dp)
        )

        if (uiStateQuery.searchStarted) {
            when (uiState) {
                is QueryUiState.Loading -> LoadingScreen(modifier)
                is QueryUiState.Success -> GridList(
                    viewModel = viewModel,
                    bookshelfList = uiState.bookshelfList,
                    modifier = modifier,
//                    onDetailsClick = onDetailsClick,
                )
                is QueryUiState.Error ->
                    ErrorScreen(retryAction = retryAction, modifier)
            }
        }
    }
}


//// Notes: the reason we see the images as the are is because it is using the image placeholder in AsyncImage
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun HomeScreenPreview() {
//    BookshelfTheme {
//        val mockData = List(10) {
//            Book(
//                "Lorem Ipsum - $it",
//                volumeInfo = VolumeInfo(
//                    title = "xxx $it",
//                    description = "xxx $it",
//                    imageLinks = null,
//                )
//            )
//        }
//        GridScreen(
//            bookshelfList = mockData,
//            modifier = Modifier,
//            onDetailsClick = { }
//        )
//    }
//}