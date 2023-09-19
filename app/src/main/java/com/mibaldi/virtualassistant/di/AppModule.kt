package com.mibaldi.virtualassistant.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.mibaldi.virtualassistant.App
import com.mibaldi.virtualassistant.BuildConfig
import com.mibaldi.virtualassistant.data.datasource.ChatGptDataSource
import com.mibaldi.virtualassistant.data.datasource.LocalDataSource
import com.mibaldi.virtualassistant.data.datasource.RemoteDataSource
import com.mibaldi.virtualassistant.data.datasource.RickyMortyDataSource
import com.mibaldi.virtualassistant.data.datasource.UserLocalDataSource
import com.mibaldi.virtualassistant.data.local.SharedPreferencesDataSource
import com.mibaldi.virtualassistant.data.local.database.RoomDataSource
import com.mibaldi.virtualassistant.data.local.database.events.EventDatabase
import com.mibaldi.virtualassistant.data.local.database.notifications.VirtualAssistantDatabase
import com.mibaldi.virtualassistant.data.server.FirebaseDataSource
import com.mibaldi.virtualassistant.data.server.chatgpt.ChatGptServerDataSource
import com.mibaldi.virtualassistant.data.server.chatgpt.ChatGptService
import com.mibaldi.virtualassistant.data.server.rickyMorty.RickyMortyGraphqlDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    @ApiChatGptUrl
    fun provideApiUrl(): String = "https://api.openai.com/v1/"

    val secret = BuildConfig.API_KEY

    @Provides
    @Singleton
    fun provideDatabase(app: Application) = Room.databaseBuilder(
        app,
        EventDatabase::class.java,
        "movie-db"
    ).build()

    @Provides
    @Singleton
    fun provideNotificationsDatabase(app: Application) = Room.databaseBuilder(
        app,
        VirtualAssistantDatabase::class.java,
        "VirtualAssistantDatabase"
    ).build()

    @Provides
    @Singleton
    fun provideEventDao(db: EventDatabase) = db.eventDao()

    @Provides
    @Singleton
    fun provideNotificationsDao(db: VirtualAssistantDatabase) = db.notificacionDao()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = HttpLoggingInterceptor().run {
        level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder().run {
            addInterceptor {
                val original = it.request()
                val request = original.newBuilder()
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer $secret")
                    .method(original.method, original.body).build()
                it.proceed(request)
            }
        }.addInterceptor(this).build()
    }

    @Provides
    @Singleton
    fun provideApolloClient(okHttpClient: OkHttpClient): ApolloClient = ApolloClient.Builder()
        .serverUrl("https://rickandmortyapi.com/graphql")
        .okHttpClient(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideRemoteService(
        @ApiChatGptUrl apiUrl: String,
        okHttpClient: OkHttpClient
    ): ChatGptService {

        return Retrofit.Builder()
            .baseUrl(apiUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideApplicationContext(application: App): Context = application.applicationContext
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AppDataModule {

    @Binds
    abstract fun bindRemoteDataSource(remoteDataSource: FirebaseDataSource): RemoteDataSource

    @Binds
    abstract fun bindChatGptDataSource(chatGptDataSource: ChatGptServerDataSource): ChatGptDataSource

    @Binds
    abstract fun bindLocalRemoteDataSource(localDataSource: SharedPreferencesDataSource): UserLocalDataSource

    @Binds
    abstract fun bindLocalDBDataSource(localDataSource: RoomDataSource): LocalDataSource

    @Binds
    abstract fun bindRickyMortyRemoteDataSource(rickyMortyDataSource: RickyMortyGraphqlDataSource): RickyMortyDataSource
}