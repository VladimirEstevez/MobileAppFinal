package com.example.bookshelf.ui.screens.beer_inventory_screen

import android.media.CamcorderProfile.getAll
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.bookshelf.BookshelfApplication
import com.example.bookshelf.data.BookshelfRepository

import com.example.bookshelf.data.db.dao.BookDao
import com.example.bookshelf.data.db.dao.OrderDao
import com.example.bookshelf.data.db.entities.BookEntity
import com.example.bookshelf.data.db.entities.OrderEntity
import com.example.bookshelf.model.Book
import com.example.bookshelf.model.Volume
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDate

class QueryViewModel(
    private val bookshelfRepository: BookshelfRepository,
    var bookDao: BookDao,
    var orderDao: OrderDao
) : ViewModel() {
    private val _uiState = MutableStateFlow<QueryUiState>(QueryUiState.Loading)
    val uiState = _uiState.asStateFlow()

    var selectedBookId by mutableStateOf("")

    private val _uiStateSearch = MutableStateFlow(SearchUiState())
    val uiStateSearch = _uiStateSearch.asStateFlow()


    // Notes: Question: I would like this to go to a separate viewModel since it belangs to a
    //  different screen. but at moment I am not sure how to get it donem because to
    //  select a favority book I would need to pass both view models
    // Logic for Favorite books -- Beg
    var favoriteBooks: MutableList<Book> by mutableStateOf(mutableListOf<Book>())
        private set


    var favoritesfUiState: QueryUiState by mutableStateOf(QueryUiState.Loading)
        private set


    private val _books = MutableStateFlow<List<BookEntity>>(emptyList())
    val books: StateFlow<List<BookEntity>> = _books.asStateFlow()

    private val _orders = MutableStateFlow<List<OrderEntity>>(emptyList())
    val orders: StateFlow<List<OrderEntity>> = _orders.asStateFlow()


    suspend fun isBookFavorite(bookId: String): Boolean {
        return withContext(Dispatchers.IO) {
            bookDao.getBookById(bookId) != null
        }
    }

    suspend fun addFavoriteBook(book: Book): Boolean {
        return withContext(Dispatchers.IO) {
            val price = book.volume.value * 5
            val bookEntity = BookEntity(book.id, book.name, book.description, price, book.volume.value)
            bookDao.insert(bookEntity)
            favoritesUpdated()
            true
        }
    }

    suspend fun removeFavoriteBook(book: Book): Boolean {
        return withContext(Dispatchers.IO) {
            val bookEntity = bookDao.getBookById(book.id)
            if (bookEntity != null) {
                bookDao.delete(bookEntity)
                favoritesUpdated()
                true
            } else {
                false
            }
        }
    }

    fun getBeers() {
    viewModelScope.launch(Dispatchers.IO) {
        updateSearchStarted(true)
        try {
            _books.value = bookDao.getAll()
        } catch (e: IOException) {
            // Handle IOException
        } catch (e: HttpException) {
            // Handle HttpException
        } finally {
            withContext(Dispatchers.Unconfined) {
                updateSearchStarted(false)
            }
        }
    }
}

    private fun favoritesUpdated() {
        viewModelScope.launch(Dispatchers.IO) {
            favoritesfUiState = QueryUiState.Loading
            val favoriteBooksEntities = bookDao.getAll()
            val favoriteBooks = favoriteBooksEntities.map {
                Book(it.id, it.name, "", it.description, it.price, Volume(it.volume, "liters"))
            }
            withContext(Dispatchers.Main) {
                favoritesfUiState = QueryUiState.Success(favoriteBooks)
            }
        }
    }

    fun updateQuery(query: String) {
        _uiStateSearch.update { currentState ->
            currentState.copy(
                query = query
            )
        }
    }

    fun updateSearchStarted(searchStarted: Boolean) {
        _uiStateSearch.update { currentState ->
            currentState.copy(
                searchStarted = searchStarted
            )
        }
    }

    fun getBooks(query: String = "") { //  "travel"
        updateSearchStarted(true)
        viewModelScope.launch {
            _uiState.value = QueryUiState.Loading

            _uiState.value = try {
                // Notes: List<Book>? NULLABLE
                val books = bookshelfRepository.getBooks(query)
                if (books == null) {
                    QueryUiState.Error
                } else if (books.isEmpty()) {
                    QueryUiState.Success(emptyList())
                } else {
                    QueryUiState.Success(books)
                }
            } catch (e: IOException) {
                QueryUiState.Error
            } catch (e: HttpException) {
                QueryUiState.Error
            }
        }
    }

    fun submitOrder(newOrder: OrderEntity) {
        viewModelScope.launch {
            insertOrder(newOrder)
        }
    }

    fun removeFromCart(book: BookEntity) {
        viewModelScope.launch {
            deleteBook(book)
        }
    }

    fun addBeer(book: BookEntity){
        bookDao.insert(book)
        _books.value = bookDao.getAll()
    }

    fun getOrders(){
        _orders.value = orderDao.getAll()
    }

    suspend fun insertOrder(order: OrderEntity) {
        withContext(Dispatchers.IO) {
            orderDao.insert(order)
            bookDao.clearAll()
            _books.value = emptyList()
            _orders.value = orderDao.getAll()
        }
    }
    suspend fun deleteBook(book: BookEntity) {
        withContext(Dispatchers.IO) {
            bookDao.delete(book)
            _books.value = bookDao.getAll()
        }
    }

    // Notes: Question: At moment this is chuck of code is repeated in two files
    //  in QueryViewModel and in DetailsViewModel.
    //  what can I do/ place it so as not to have repeat code? I tried but I got a bunch of errors
    /**
     * Factory for BookshelfViewModel] that takes BookshelfRepository] as a dependency
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BookshelfApplication)
                val bookshelfRepository = application.container.bookshelfRepository
                val bookDao = application.container.appDatabase.bookDao()
                val orderDao = application.container.appDatabase.orderDao()
                QueryViewModel(bookshelfRepository = bookshelfRepository, bookDao = bookDao, orderDao = orderDao)
            }
        }
    }
}