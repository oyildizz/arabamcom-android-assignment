package com.example.kotlin.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1, exportSchema = false)
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
                    ).build()
                    //.allowMainThreadQueries().build() yazılabilirdi fakat Bu, Room veritabanı işlemlerinin ana iş parçacığında çalışmasına izin verir ve uygulamanın performansını düşürebilir.
                // Room işlemlerini arka planda yapmak için Coroutine kullanıyorum zaten, bu nedenle allowMainThreadQueries() kullanmama gerek yok.
                }
            }
            return dbINSTANCE!!
        }
    }
}