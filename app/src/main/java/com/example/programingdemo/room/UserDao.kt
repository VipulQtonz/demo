package com.example.programingdemo.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.programingdemo.gallery.ImagePath

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User): Long

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Int): User?

    @Query("SELECT * FROM users")
    fun getAllUsers(): LiveData<List<User>>

    @Update
    suspend fun update(user: User): Int

    @Delete
    suspend fun delete(user: User): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecentImage(imagePath: ImagePath): Long

    @Query("SELECT * FROM recentPhotos WHERE path = :imagePath LIMIT 1")
    suspend fun getImageByPath(imagePath: String): ImagePath?

    @Delete
    suspend fun deleteRecentImage(imagePath: ImagePath)

    @Query("SELECT * FROM recentPhotos ORDER BY id DESC")
    fun getAllRecentPhotos(): LiveData<List<ImagePath>>

    @Query("SELECT COUNT(*) FROM recentPhotos")
    fun getRecentImagesCount(): LiveData<Int>

}