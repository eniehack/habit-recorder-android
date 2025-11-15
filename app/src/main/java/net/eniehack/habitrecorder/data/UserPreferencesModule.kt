package net.eniehack.habitrecorder.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.eniehack.habitrecorder.snippets.proto.UserPreferences
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserPreferencesModule {
    @Singleton
    @Provides
    fun provideUserPreferencesDataStore(
        @ApplicationContext context: Context,
        userPreferencesSerializer: UserPreferencesSerializer
    ): DataStore<UserPreferences> = DataStoreFactory.create(
        serializer = userPreferencesSerializer,
    ) {
        context.dataStoreFile("preference.pb")
    }
}
