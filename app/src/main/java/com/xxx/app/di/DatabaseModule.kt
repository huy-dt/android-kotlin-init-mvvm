// Database & DAO là hạ tầng dùng chung cho nhiều feature
// nên được cung cấp ở app-level DI module

package com.xxx.app.di

import android.content.Context
import androidx.room.Room
import com.xxx.app.database.AppDatabase
import com.xxx.app.database.dao.AuthDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "order_server_db"
        ).build()
    }

    // 🔥 DÒNG QUYẾT ĐỊNH
    @Provides
    fun provideAuthDao(
        database: AppDatabase
    ): AuthDao {
        return database.authDao()
    }
}
