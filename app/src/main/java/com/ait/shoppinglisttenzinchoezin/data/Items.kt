package com.ait.shoppinglisttenzinchoezin.data

import android.graphics.drawable.Drawable
import android.widget.Spinner
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import java.io.Serializable

@Entity(tableName = "items")
data class Items (

    @PrimaryKey(autoGenerate = true) var itemsId: Long?,
    @ColumnInfo(name = "itemsCat") var itemsCat: Int,
    @ColumnInfo(name = "itemsName") var itemsName: String,
    @ColumnInfo(name = "purchased") var purchased: Boolean,
    @ColumnInfo(name = "itemsPrice") var itemsPrice: String,
    @ColumnInfo(name = "itemsDetails") var itemsDetails: String

) : Serializable