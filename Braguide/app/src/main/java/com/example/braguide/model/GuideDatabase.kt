package com.example.braguide.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlin.concurrent.Volatile
import kotlinx.coroutines.launch


@Database(entities = [Trail::class, User::class, AppData::class, Contact::class, Social::class, Partner::class, TrailMetrics::class], version = 961)
@TypeConverters(
    TypeConverter::class,
    EdgeTipConverter::class,
    RelTrailConverter::class,
    EdgeConverter::class
)
abstract class GuideDatabase : RoomDatabase() {

    abstract fun trailDAO(): TrailDAO
    abstract fun userDAO(): UserDAO

    abstract fun appDataDAO() : AppDataDAO

    abstract fun trailMetricsDAO() : TrailMetricsDAO

    private class GuideDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var traildao = database.trailDAO()

                    var userdao = database.userDAO()

                    var appdao = database.appDataDAO()

                    var trailMetricsDao = database.trailMetricsDAO()
                }
            }

        }

    }

    companion object {
        @Volatile
        private var INSTANCE: GuideDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): GuideDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GuideDatabase::class.java,
                    "BraGuide"
                )
                    .addCallback(GuideDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}