package pl.sofantastica.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pl.sofantastica.data.repository.FurnitureRepository
import pl.sofantastica.data.repository.FurnitureRepositoryImpl
import pl.sofantastica.data.repository.FabricRepository
import pl.sofantastica.data.repository.FabricRepositoryImpl
import pl.sofantastica.data.repository.OrderRepository
import pl.sofantastica.data.repository.OrderRepositoryImpl
import pl.sofantastica.data.repository.FavoritesRepository
import pl.sofantastica.data.repository.FavoritesRepositoryImpl
import pl.sofantastica.data.repository.CartRepository
import pl.sofantastica.data.repository.CartRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindFurnitureRepository(
        impl: FurnitureRepositoryImpl
    ): FurnitureRepository

    @Binds
    @Singleton
    abstract fun bindFabricRepository(
        impl: FabricRepositoryImpl
    ): FabricRepository

    @Binds
    @Singleton
    abstract fun bindOrderRepository(
        impl: OrderRepositoryImpl
    ): OrderRepository

    @Binds
    @Singleton
    abstract fun bindFavoritesRepository(
        impl: FavoritesRepositoryImpl
    ): FavoritesRepository

    @Binds
    @Singleton
    abstract fun bindCartRepository(
        impl: CartRepositoryImpl
    ): CartRepository
}