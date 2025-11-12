package net.eniehack.habitrecorder.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStore
import androidx.datastore.dataStoreFile
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.eniehack.habitrecorder.snippets.proto.PixelaUserCredentials
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object PixelaCredentialModule {
    @Singleton
    @Provides
    fun provideCredentialDataStore(
        @ApplicationContext context: Context,
        pixelaCredentialSerializer: PixelaCredentialSerializer
    ): DataStore<PixelaUserCredentials> = DataStoreFactory.create(
            serializer = pixelaCredentialSerializer,
        ) {
        context.dataStoreFile("pixela_auth_data.pb")
    }
}