package com.ait.shoppinglisttenzinchoezin.data

import androidx.room.*

@Dao
interface ItemsDao {

    @Query("SELECT * FROM items")
    fun getAllItems() :  List<Items>

    @Insert
    fun insertItems(items: Items) : Long

    @Delete
    fun deleteItems(items: Items)

    @Update
    fun updateItems(items: Items)

    @Query("DELETE FROM items")
    fun deleteAllItems()

}