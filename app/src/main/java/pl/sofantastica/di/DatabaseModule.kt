package pl.sofantastica.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pl.sofantastica.data.db.AppDatabase
import pl.sofantastica.data.db.dao.CartDao
import pl.sofantastica.data.db.dao.FabricDao
import pl.sofantastica.data.db.dao.FavoriteDao
import pl.sofantastica.data.db.dao.FurnitureDao
import pl.sofantastica.data.db.dao.FurnitureImageDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "sofantastica.db").build()

    @Provides
    fun provideFurnitureDAO(appDatabase: AppDatabase): FurnitureDao {
        return appDatabase.furnitureDao()
    }

    @Provides
    fun provideFabricDao(appDatabase: AppDatabase): FabricDao {
        return appDatabase.fabricDao()
    }

    @Provides
    fun provideFavoriteDao(appDatabase: AppDatabase): FavoriteDao {
        return appDatabase.favoriteDao()
    }

    @Provides
    fun provideCartDao(appDatabase: AppDatabase): CartDao {
        return appDatabase.cartDao()
    }

    @Provides
    fun provideFurnitureImageDAO(appDatabase: AppDatabase): FurnitureImageDao {
        return appDatabase.furnitureImageDao()
    }
}
