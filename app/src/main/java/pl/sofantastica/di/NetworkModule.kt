package pl.sofantastica.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import pl.sofantastica.data.api.RetrofitApiService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://flato.q11.jvmhost.net/api/"


    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    var httpClient = OkHttpClient.Builder().apply {
        addInterceptor(logging)
    }


    @Singleton
    @Provides
    fun provideRetrofitService(): RetrofitApiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(httpClient.build())
        .build()
        .create(RetrofitApiService::class.java)
   }
