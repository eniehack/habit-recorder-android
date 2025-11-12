package net.eniehack.habitrecorder.data

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import net.eniehack.habitrecorder.snippets.proto.PixelaUserCredentials
import net.eniehack.habitrecorder.snippets.proto.copy
import javax.inject.Inject

class PixelaCredentialRepository @Inject constructor(
    private val dataStore: DataStore<PixelaUserCredentials>
) {
    suspend fun save(userId: String, token: String) {
        dataStore.updateData { currentStore ->
            currentStore.toBuilder()
                .clear()
                .setUserId(userId)
                .setToken(token)
                .build()
        }
    }

    fun read(): Flow<PixelaUserCredentials> {
        return dataStore.data
    }
}