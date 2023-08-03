package com.example.kotlin.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 3, exportSchema = true)
abstract class CarsDatabase: RoomDatabase() {

    //uygulama ayağa kaldırılken çağırılacak
    abstract fun getDao(): UserInfoDAO

//    abstract fun locationDao(): LocationDAO
//    abstract fun categoryDao(): CategoryDAO

    companion object {
        private var dbINSTANCE: CarsDatabase? = null

        fun getAppDB(context: Context): CarsDatabase {
            if (dbINSTANCE == null) {
                synchronized(CarsDatabase::class) {
                    dbINSTANCE = Room.databaseBuilder<CarsDatabase>(
                        context.applicationContext,
                        CarsDatabase::class.java,
                        "user_database"
                    ).fallbackToDestructiveMigration().allowMainThreadQueries().build()
                    //If you don’t want to provide migrations and you specifically want your database to be cleared when you upgrade the version, call fallbackToDestructiveMigration in the database builder.
                    //geçis saglamaya calıstıgımda try çalıştıktan sonra catch e düsüyordu
                }
            }
            return dbINSTANCE!!
        }
    }
}