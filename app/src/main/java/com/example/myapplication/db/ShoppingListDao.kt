package com.example.myapplication.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.models.ShoppingListItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDao {

    @Query("SELECT * FROM shopping_list_items ORDER BY category")
    fun getAllShoppingItems(): Flow<List<ShoppingListItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ShoppingListItem)

    @Delete
    suspend fun deleteItem(item: ShoppingListItem)

    @Query("DELETE FROM shopping_list_items WHERE id = :itemId")
    suspend fun deleteItemById(itemId: Int)

    @Update
    suspend fun updateItem(item: ShoppingListItem)

    @Query("SELECT * FROM shopping_list_items")
    fun getAllShoppingListItems(): Flow<List<ShoppingListItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ShoppingListItem)

    @Update
    suspend fun update(item: ShoppingListItem)

    @Delete
    suspend fun delete(item: ShoppingListItem)
}
