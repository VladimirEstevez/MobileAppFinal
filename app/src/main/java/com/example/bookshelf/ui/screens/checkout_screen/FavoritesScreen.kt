package com.example.bookshelf.ui.screens.checkout_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.semantics.SemanticsProperties.Selected
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.bookshelf.data.db.entities.BookEntity
import com.example.bookshelf.data.db.entities.OrderEntity
import com.example.bookshelf.ui.screens.beer_inventory_screen.*
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    viewModel: QueryViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState(initial = QueryUiState.Loading)
    val (email, setEmail) = remember { mutableStateOf("") }
    val minDaysFromNow = 3
    var selectedDate by remember { mutableStateOf(LocalDate.now())
}
    Log.d("DatePicker", "Selected date: $selectedDate")
    val datePickerState = rememberDatePickerState(
    initialSelectedDateMillis = LocalDate.now().atStartOfDay(ZoneId.of("America/New_York")).toInstant().toEpochMilli()
)
    var isDatePickerDialogOpen by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel) {
        viewModel.getBeers()
    }

    LaunchedEffect(datePickerState.selectedDateMillis) {
        val dateMillis = datePickerState.selectedDateMillis ?: return@LaunchedEffect

        selectedDate = LocalDate.ofEpochDay(dateMillis / (24 * 60 * 60 * 1000))

        if (selectedDate == LocalDate.now()){
            selectedDate = LocalDate.now().plusDays(1 + minDaysFromNow.toLong())
        }

        Log.d("DatePicker", "Date millis: $dateMillis, Selected date: $selectedDate")
    }

    val books by viewModel.books.collectAsState()
    val orderTotal = books.sumOf { it.price.toDouble() }
    val isButtonEnabled = verifyEmail(email) && selectedDate.isAfter(LocalDate.now()) && books.isNotEmpty()

    Column {
        if (isDatePickerDialogOpen) {
            Dialog(onDismissRequest = { isDatePickerDialogOpen = false }) {
                Column {
                    DatePicker(
                        datePickerState = datePickerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        dateFormatter = remember { DatePickerFormatter() },
                        dateValidator = { selectedDate -> 
                            // Calculate the minimum allowed date
                            val minDate = LocalDate.now().plusDays(minDaysFromNow.toLong())
                            val minDateMillis: Long =
                                minDate.atStartOfDay(ZoneId.of("America/New_York")).toInstant()
                                    .toEpochMilli()

                            // Check if the selected date is equal to or after the minimum allowed date
                            val isValid = selectedDate >= minDateMillis
                            Log.d("DatePicker", "Selected date: $selectedDate, Min date: $minDateMillis, Is valid: $isValid")

                            isValid
                        },
                        title = { Text(text = "Select a pick-up date:") },
                        headline = { }, // Your custom headline composable
                        colors = DatePickerDefaults.colors()
                    )
                    Button(
                        onClick = { isDatePickerDialogOpen = false },
                        modifier = Modifier.height(40.dp),
                        shape = RoundedCornerShape(5.dp),
                        contentPadding = PaddingValues(5.dp),
                    ) {
                        Text("Confirm Date")
                    }
                }
            }
        } else {
            OrderTotal(orderTotal)
            //OrderId(orderId)
            EmailInput(email, setEmail)

            Row(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(bottom = 10.dp, start = 10.dp, end = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pick up date: $selectedDate",
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .weight(1f)
                )
                Button(
                    onClick = { isDatePickerDialogOpen = !isDatePickerDialogOpen },
                    modifier = Modifier.height(40.dp),
                    shape = RoundedCornerShape(5.dp),
                    contentPadding = PaddingValues(5.dp),
                ) {
                    Text("Select a date")
                }
            }
            Row(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(bottom = 10.dp, start = 10.dp, end = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        val selectedBooks = buildString {
                            books.forEachIndexed { index, book ->
                                append(book.id)
                                if (index < books.size - 1) {
                                    append(", ")
                                }
                            }
                        }

                        val newOrder = OrderEntity(
                            customerEmail = email,
                            pickupDate = selectedDate.toEpochDay(),
                            beerIds = selectedBooks,
                            totalPrice = orderTotal,
                            orderStatus = "Pending",
                        )

                        viewModel.submitOrder(newOrder)

                        setEmail("")
                        selectedDate = LocalDate.now().plusDays(1 + minDaysFromNow.toLong())
                    },
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(5.dp),
                    enabled = isButtonEnabled
                ) {
                    Text("Submit Order!")
                }
            }
            Text(
                text = "Order Summary",
                modifier = Modifier.padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            )
            LazyColumn(contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp)) {
                items(books.size) { index ->
                    FavoritesCard(books[index], viewModel)
                }
            }
        }

        
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailInput(email: String, setEmail: (String) -> Unit) {

    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Enter email: ",
            modifier = Modifier
                .weight(1f)

        )
        OutlinedTextField(
            value = email,
            onValueChange = { newEmail -> setEmail(newEmail) },
            placeholder = { Text("youremail@gmail.com") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                capitalization = KeyboardCapitalization.None
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "emailIcon"
                )
            },
            singleLine = true,
            modifier = Modifier
                .weight(2f)
                .height(60.dp)
        )
    }
}

@Composable
fun OrderTotal(total: Double) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
    ) {
        Text(
            text = "Total Amount: ",
            modifier = Modifier
                .weight(2f)
        )

        Text(
            text = "$total $",
            textAlign = TextAlign.End,
            modifier = Modifier
                .weight(1f)
        )
    }
}

@Composable
fun FavoritesCard(item: BookEntity, viewModel: QueryViewModel) {
    var buttonClicked by remember { mutableStateOf(false) }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .shadow(4.dp),
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Start-aligned column
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, bottom = 10.dp, top = 10.dp)
                    .weight(2f), // Occupy 1/3 of the available space
            ) {
                // Name label
                Text(
                    text = item.name,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Text(
                    text = "ID: " + item.id,
                )
            }

            // End-aligned column
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        end = 16.dp,
                        bottom = 10.dp,
                        top = 10.dp,
                        start = 10.dp
                    )
            ) {
                Text(
                    text = "Price: " + item.price.toString() + " $",
                )

                Button(
                    onClick = { buttonClicked = true },
                    modifier = Modifier.height(40.dp),
                    shape = RoundedCornerShape(5.dp),
                    contentPadding = PaddingValues(5.dp),
                ) {
                    Text(
                        text = "Remove",
                    )
                }
            }
        }
    }

    LaunchedEffect(buttonClicked) {
        if (buttonClicked) {
            //viewModel.bookDao.delete(item)
            buttonClicked = false // Reset the state
        }
    }

}

fun verifyEmail(email: String): Boolean{
    val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\$")
    return emailRegex.matches(email)
}