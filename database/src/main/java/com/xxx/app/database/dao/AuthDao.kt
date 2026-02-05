package com.xxx.app.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xxx.app.database.entity.UserEntity

@Dao
interface AuthDao {

    @Query(
        """
        SELECT * FROM users
        WHERE username = :username AND password = :password
        LIMIT 1
        """
    )
    suspend fun findByCredentials(
        username: String,
        password: String
    ): UserEntity?

    @Query(
        """
        SELECT EXISTS(
            SELECT 1 FROM users WHERE username = :username
        )
        """
    )
    suspend fun existsByUsername(
        username: String
    ): Boolean

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(
        user: UserEntity
    ): Long
}
