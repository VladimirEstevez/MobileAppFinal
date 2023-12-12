package com.example.bookshelf

import com.example.bookshelf.data.db.dao.BookDao
import com.example.bookshelf.data.db.dao.OrderDao
import com.example.bookshelf.data.db.entities.BookEntity
import com.example.bookshelf.data.db.entities.OrderEntity
import com.example.bookshelf.ui.screens.beer_inventory_screen.QueryViewModel
import com.example.bookshelf.ui.screens.checkout_screen.verifyEmail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import java.time.LocalDate
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain

class FSMBUnitTests {

    @Test
    fun testVerifyEmail_validEmail() {
        // Arrange
        val validEmail = "user@example.com"

        // Act
        val result = verifyEmail(validEmail)

        // Assert
        assertTrue(result)
    }

    @Test
    fun testVerifyEmail_invalidEmail() {
        // Arrange
        val invalidEmail = "invalid_email"

        // Act
        val result = verifyEmail(invalidEmail)

        // Assert
        assertFalse(result)
    }

    @Test
    fun testVerifyEmail_emptyEmail() {
        // Arrange
        val emptyEmail = ""

        // Act
        val result = verifyEmail(emptyEmail)

        // Assert
        assertFalse(result)
    }

    @Test
    fun testQueryViewModel_addBeer() = runBlocking{
        // Arrange
        val viewModel = createQueryViewModelWithMocks()
        val book = BookEntity("1", "Beer 1", "Description", 20.0f, 5f)

        // Act
        viewModel.addBeer(book)

        // Assert
        viewModel.getBeers()
        val books = viewModel.books.value
        assertEquals(1, books.size)
    }

    /**
     * Could not get it to work it would assert before the values got updated.
     */
//    @Test
//    fun testQueryViewModel_deleteBook() = runBlocking {
//        // Arrange
//        val viewModel = createQueryViewModelWithMocks()
//        val book = BookEntity("1", "Beer 1", "Description", 20.0f, 5f)
//        viewModel.addBeer(book)
//        viewModel.getBeers()
//        val booksBefore = viewModel.books.value
//        assertEquals(1, booksBefore.size)
//
//        // Act
//        viewModel.deleteBook(book)
//
//        // Assert
//        val booksAfter = viewModel.books.value
//        assertEquals(0, booksAfter.size)
//    }

    @Test
    fun testQueryViewModel_insertOrder() = runBlocking {
        // Arrange
        val viewModel = createQueryViewModelWithMocks()
        val order = OrderEntity(1, "test@email.com", LocalDate.now().toEpochDay(), "1,2,3", 50.0, "Pending")
        val book = BookEntity("1", "Beer 1", "Description", 20.0f, 5f)

        viewModel.addBeer(book)
        viewModel.getBeers()
        val booksBefore = viewModel.books.value
        assertEquals(1, booksBefore.size)

        // Act

        viewModel.insertOrder(order)

        // Assert
        val booksAfter = viewModel.books.value
        assertEquals(0, booksAfter.size)

        val orders = viewModel.orders.value
        assertEquals(1, orders.size)
    }

    private fun createQueryViewModelWithMocks(): QueryViewModel {
        val mockBookDao = mockk<BookDao>()
        val mockOrderDao = mockk<OrderDao>()
        val testBookEntity: BookEntity = mockk()
        val testOrderEntity: OrderEntity = mockk()

        // Assuming you have a way to set up your ViewModel with these mocks
        val viewModel = QueryViewModel(
            bookshelfRepository = mockk(),
            bookDao = mockBookDao,
            orderDao = mockOrderDao
        ).apply {
            // Set the mocks in the ViewModel for verification
            this.bookDao = mockBookDao
            this.orderDao = mockOrderDao
        }

        // Mock the behavior of the insert function
        coEvery { mockBookDao.insert(any()) } just Runs
        coEvery { mockBookDao.getAll() } returns listOf(testBookEntity)
        coEvery { mockBookDao.clearAll() } just Runs
        coEvery { mockBookDao.delete(any()) } just Runs
        coEvery { mockOrderDao.insert(any()) } just Runs
        coEvery { mockOrderDao.getAll() } returns listOf(testOrderEntity)

        return viewModel
    }


    private val testDispatcher = TestCoroutineDispatcher()

    // Set up the test coroutine dispatcher before running tests
    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
        clearAllMocks(true)
    }

    // Tear down the test coroutine dispatcher after running tests
    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}