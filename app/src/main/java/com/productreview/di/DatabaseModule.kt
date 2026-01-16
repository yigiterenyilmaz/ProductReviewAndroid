package com.productreview.di

import android.content.Context
import androidx.room.Room
import com.productreview.data.local.AppDatabase
import com.productreview.data.local.WishlistDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for database dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "product_review_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideWishlistDao(database: AppDatabase): WishlistDao {
        return database.wishlistDao()
    }
}
