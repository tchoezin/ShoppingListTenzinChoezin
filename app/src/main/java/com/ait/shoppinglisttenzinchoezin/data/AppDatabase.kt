package com.ait.shoppinglisttenzinchoezin.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database (entities = arrayOf(Items::class), version = 3)
abstract class AppDatabase : RoomDatabase(){
    abstract fun itemsDao() : ItemsDao

    companion object{
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context:Context) : AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase::class.java, "items.db")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE!!
        }
    }

}