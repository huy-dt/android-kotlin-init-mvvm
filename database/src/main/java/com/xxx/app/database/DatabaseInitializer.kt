package com.xxx.app.database

import com.xxx.app.database.entity.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object DatabaseInitializer {

    fun seed(database: AppDatabase) {
        CoroutineScope(Dispatchers.IO).launch {
            val userDao = database.userDao()

            if (userDao.count() > 0) return@launch

            userDao.insert(
                UserEntity(
                    id = 1L,
                    username = "admin",
                    password = "12345678",
                    email = "admin@mail.com",
                    role = "ADMIN"
                )
            )
        }
    }
}
