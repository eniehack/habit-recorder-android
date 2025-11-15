package net.eniehack.habitrecorder.data

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import net.eniehack.habitrecorder.snippets.proto.PixelaUserCredentials
import javax.inject.Inject

class PixelaCredentialRepository @Inject constructor(
    private val dataStore: DataStore<PixelaUserCredentials>
) {
    val credentialFlow: Flow<PixelaUserCredentials> = dataStore.data

    suspend fun updateUserId(userId: String) {
        dataStore.updateData { currentStore ->
            currentStore.toBuilder()
                .setUserId(userId)
                .build()
        }
    }

    suspend fun updateToken(token: String) {
        dataStore.updateData { currentStore ->
            currentStore.toBuilder()
                .setToken(token)
                .build()
        }
    }
}