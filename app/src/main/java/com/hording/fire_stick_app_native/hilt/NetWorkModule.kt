package com.hording.fire_stick_app_native.hilt

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.hording.fire_stick_app_native.Constants
import com.hording.fire_stick_app_native.WebSocketManager
import com.hording.fire_stick_app_native.netWork.ApiService
import com.hording.fire_stick_app_native.netWork.AuthInterceptor
import com.hording.fire_stick_app_native.repository.DeviceDetailsRepository
import com.hording.fire_stick_app_native.repository.FetchAdsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


@Module
@InstallIn(SingletonComponent::class)
object NetWorkModule {
    private const val DATASTORE_NAME = "app_preferences"

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(scope = CoroutineScope(Dispatchers.IO + SupervisorJob())) {
            context.preferencesDataStoreFile(DATASTORE_NAME)
        }
    }

    @Provides
    @Singleton
    fun getRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val retrofit = lazy {
            Retrofit.Builder().baseUrl(Constants.BASE_URL_)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        }
        return retrofit.value
    }

    @Provides
    @Singleton
    fun getApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun getInterceptors(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
          level=  HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun getOkhttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

    }
    @Provides
    @Singleton
    fun getWebSocketManager(fetchAdsRepository: FetchAdsRepository, deviceDetailsRepository: DeviceDetailsRepository): WebSocketManager {
        return WebSocketManager(fetchAdsRepository = fetchAdsRepository, deviceDetailsRepository =deviceDetailsRepository )
    }
    @Provides
    @Singleton
    fun getAdsRepository(apiService: ApiService): FetchAdsRepository {
        return FetchAdsRepository(apiService = apiService )
    }
    @Provides
    @Singleton
    fun getDeviceDetailsRepository(dataStore: DataStore<Preferences>): DeviceDetailsRepository {
        return DeviceDetailsRepository(dataStore = dataStore )
    }



}