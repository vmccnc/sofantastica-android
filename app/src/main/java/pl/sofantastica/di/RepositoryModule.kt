package pl.sofantastica.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pl.sofantastica.data.repository.FurnitureRepository
import pl.sofantastica.data.repository.FurnitureRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindFurnitureRepository(
        impl: FurnitureRepositoryImpl
    ): FurnitureRepository
}