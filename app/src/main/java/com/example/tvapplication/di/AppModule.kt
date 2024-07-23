package com.example.tvapplication.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.tvapplication.data.local.SharedPreferencesHelper
import com.example.tvapplication.data.local.SharedPreferencesHelper.Companion.PREFS_NAME
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

    @Provides
    fun provideSharedPrf(application: Application):SharedPreferences{
        return application.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE)
    }

    @Provides
    fun provideDynamicBaseUrlInterceptor(baseUrlProvider: BaseUrlPro): DynamicBaseUrlInterceptor {
        return DynamicBaseUrlInterceptor(baseUrlProvider)
    }

    @Provides
    fun provideBaseUrlProvider(sharePrf:SharedPreferencesHelper): BaseUrlPro {
        return DynamicBaseUrlProvider(sharePrf)
    }


}