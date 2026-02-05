package com.xxx.app.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.xxx.app.database.dao.AuthDao
import com.xxx.app.database.dao.UserDao
import com.xxx.app.database.entity.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun authDao(): AuthDao
    abstract fun userDao(): UserDao

    // companion object {
    //     @Volatile
    //     private var INSTANCE: AppDatabase? = null

    //     fun getInstance(context: Context): AppDatabase {
    //         return INSTANCE ?: synchronized(this) {
    //             INSTANCE ?: Room.databaseBuilder(
    //                 context.applicationContext,
    //                 AppDatabase::class.java,
    //                 "order_server.db"
    //             )
    //                 .fallbackToDestructiveMigration()
    //                 .addCallback(object : RoomDatabase.Callback() {
    //                     override fun onCreate(db: SupportSQLiteDatabase) {
    //                         super.onCreate(db)
    //                         DatabaseInitializer.seed(INSTANCE!!)
    //                     }
    //                 })
    //                 .build()
    //                 .also { INSTANCE = it }
    //         }
    //     }
    // }
}
