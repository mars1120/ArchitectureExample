package com.marschen.architectureexample.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [Drama::class],
    version = 1,
    exportSchema = false
)
abstract class DramaRoomDatabase : RoomDatabase() {
    abstract fun dramaDao(): DramaDao

    companion object {
        private var INSTANCE: DramaRoomDatabase? = null
        fun getDatabase(context: Context): DramaRoomDatabase {
            if (INSTANCE == null) {
                synchronized(DramaRoomDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE =
                            Room.databaseBuilder(
                                context.applicationContext,
                                DramaRoomDatabase::class.java, "drama.db"
                            )
                                .fallbackToDestructiveMigration()
                                .build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}