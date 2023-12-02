package com.example.bookshelf.di

import com.example.bookshelf.data.BookshelfRepository
import com.example.bookshelf.data.DefaultBookshelfRepository
import com.example.bookshelf.network.BookshelfApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class DefaultAppContainer : AppContainer {
//    private val json = Json {
//        ignoreUnknownKeys = true
//        explicitNulls = false
//    }
private val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()


    override val bookshelfApiService: BookshelfApiService by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
//            .addConverterFactory(json
//                    .asConverterFactory("application/json".toMediaType()))
            .baseUrl(BookshelfApiService.BASE_URL)
            .client(client)  // Add this line
            .build()
            .create()
    }

    override val bookshelfRepository: BookshelfRepository by lazy {
        DefaultBookshelfRepository(bookshelfApiService)
    }
}