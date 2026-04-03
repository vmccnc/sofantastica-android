package com.furniture.duet.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.furniture.duet.data.repository.FurnitureRepository
import com.furniture.duet.data.repository.FurnitureRepositoryImpl
import com.furniture.duet.data.repository.FabricRepository
import com.furniture.duet.data.repository.FabricRepositoryImpl
import com.furniture.duet.data.repository.OrderRepository
import com.furniture.duet.data.repository.OrderRepositoryImpl
import com.furniture.duet.data.repository.FavoritesRepository
import com.furniture.duet.data.repository.FavoritesRepositoryImpl
import com.furniture.duet.data.repository.CartRepository
import com.furniture.duet.data.repository.CartRepositoryImpl
import com.furniture.duet.data.repository.UserRepository
import com.furniture.duet.data.repository.UserRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository

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