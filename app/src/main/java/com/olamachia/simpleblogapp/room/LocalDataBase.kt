package com.olamachia.simpleblogapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PostEntity::class, CommentEntity::class], version = 1)

abstract class LocalDataBase : RoomDatabase() {

    abstract fun userDao(): Dao

    companion object {
        @Volatile
        private var INSTANCE: LocalDataBase? = null
//
        fun getInstance(context: Context): LocalDataBase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: createDatabase(context).also { INSTANCE = it }
            }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            LocalDataBase::class.java,
            "user_table"
        ).build()

    }

}