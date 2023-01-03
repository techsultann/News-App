package com.example.newsapp.db

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsapp.models.Article


@Database(entities = [Article::class], version = 2, exportSchema = false, autoMigrations = [AutoMigration (from = 1, to = 2)])
@TypeConverters(Converters::class)
abstract class ArticleDatabase() : RoomDatabase() {

    abstract fun articleDao(): ArticleDao


    companion object {

        @Volatile
        private var INSTANCE : ArticleDatabase? = null

        fun getDatabase(context: Context): ArticleDatabase {
            return  INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ArticleDatabase::class.java,
                    "article_db"
                ).build()

                INSTANCE = instance
                return instance
            }
        }
    }



    /*companion object {
        @Volatile
        private var instance: ArticleDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: this.createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                "article_db.db"
            ).build()
    }*/
}