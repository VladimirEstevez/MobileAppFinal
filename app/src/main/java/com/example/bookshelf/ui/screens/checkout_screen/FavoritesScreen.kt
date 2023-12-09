package com.example.bookshelf.ui.screens.checkout_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookshelf.R
import com.example.bookshelf.data.db.entities.BookEntity
import com.example.bookshelf.model.Book
import com.example.bookshelf.ui.screens.beer_inventory_screen.*
import com.example.bookshelf.ui.theme.BookshelfTheme
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    viewModel: QueryViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState(initial = QueryUiState.Loading)
    val (email, setEmail) = remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val datePickerState = rememberDatePickerState( initialSelectedDateMillis = Instant.now().toEpochMilli() )
    var isDatePickerDialogOpen by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel) {
        viewModel.getBeers()
    }

    val books by viewModel.books.collectAsState()

    Column {
        if (isDatePickerDialogOpen) {
            Dialog(onDismissRequest = { isDatePickerDialogOpen = false }) {
                Column {
                    DatePicker(
                        datePickerState = datePickerState,
                        modifier = Modifier.fillMaxWidth(),
                        dateFormatter = remember { DatePickerFormatter() },
                        dateValidator = { true }, // Your custom date validation logic here
                        title = { Text(text = "Select a pick-up date:") },
                        headline = { }, // Your custom headline composable
                        colors = DatePickerDefaults.colors()
                    )
                    Button(onClick = { isDatePickerDialogOpen = false }) {
                        Text("Confirm Date")
                    }
                }
            }
        }else {
            OrderTotal()
            OrderId()
            EmailInput(email, setEmail)
            Row(){
                Button(onClick = { isDatePickerDialogOpen = !isDatePickerDialogOpen}) {
                    Text("Select a date")
                }
                Text(text = selectedDate.toString())
            }

            Button(onClick = {  }) {
                Text("Submit Order!")
            }
            Text(text = "Order Summary")
            LazyColumn(contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp)) {
                items(books.size) { index ->
                    FavoritesCard(books[index])
                }
            }
        }

        LaunchedEffect(datePickerState.selectedDateMillis) {
            val dateMillis = datePickerState.selectedDateMillis ?: return@LaunchedEffect
            selectedDate = Instant.ofEpochMilli(dateMillis).atZone(ZoneId.systemDefault()).toLocalDate()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailInput(email: String, setEmail: (String) -> Unit) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Email",
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 10.dp, start = 10.dp)
        )
        OutlinedTextField(
            value = email,
            onValueChange = { newEmail -> setEmail(newEmail) },
            placeholder = { Text("Enter your email") },
            //textStyle = androidx.compose.ui.text.TextStyle(fontSize = .sp),
            modifier = Modifier
                .width(230.dp)
                .height(60.dp)
        )
    }
}

@Composable
fun OrderId() {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = "Order ID",
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 10.dp, start = 10.dp)
        )

        // Details label
        Text(
            text = "1",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun OrderTotal() {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
    ){
        Text(
            text = "Total Amount",
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 10.dp, start = 10.dp)
        )

        // Details label
        Text(
            text = "9001",
            modifier = Modifier.weight(1f)
        )
    }
}
@Composable
fun FavoritesCard(item: BookEntity) {
    // Your existing card composable, modified to use data from the item
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .shadow(4.dp),
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
        ) {
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
                    text = (5*item.price).toString(),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
@Composable
fun DatePicker(){

}
//@Preview(showSystemUi = true)
//@Composable
//fun MenuScreenPreview() {
//    val viewModel : QueryViewModel = viewModel(factory = QueryViewModel.Factory)
//    BookshelfTheme {
//        FavoritesScreen(viewModel)
//    }
//}


