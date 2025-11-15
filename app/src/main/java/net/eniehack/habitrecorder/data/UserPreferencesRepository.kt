package net.eniehack.habitrecorder.data

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import net.eniehack.habitrecorder.snippets.proto.UserPreferences
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<UserPreferences>
) {
    val preferenceFlow: Flow<UserPreferences> = dataStore.data

    suspend fun updateEnablePixela(flag: Boolean) {
        dataStore.updateData { current ->
            current.toBuilder()
                .setEnablePixela(flag)
                .build()
        }
    }
}