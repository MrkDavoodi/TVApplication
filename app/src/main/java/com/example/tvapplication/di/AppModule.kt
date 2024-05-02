package com.example.tvapplication.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Provides the application context as a dependency.
     * @param context The application context provided by Dagger Hilt.
     * @return The provided application context.
     */
    @Provides
    fun providesContext(@ApplicationContext context: Context) = context
}