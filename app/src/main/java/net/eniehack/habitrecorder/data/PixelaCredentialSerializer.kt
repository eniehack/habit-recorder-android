package net.eniehack.habitrecorder.data

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.RegistryConfiguration
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.google.protobuf.InvalidProtocolBufferException
import dagger.hilt.android.qualifiers.ApplicationContext
import net.eniehack.habitrecorder.snippets.proto.PixelaUserCredentials
import java.io.InputStream
import java.io.OutputStream
import java.security.GeneralSecurityException
import javax.inject.Inject

class PixelaCredentialSerializer @Inject constructor(
    @ApplicationContext context: Context,
) :Serializer<PixelaUserCredentials> {
    private val aead: Aead by lazy {
        val prefFileName = "pixela_auth_prefs"
        val keysetName = "pixela_user_credentials_keyset"
        val masterKeyUri = "android-keystore://pixela-auth-master-key"
        AeadConfig.register()
        AndroidKeysetManager.Builder()
            .withSharedPref(context, keysetName, prefFileName)
            .withKeyTemplate(KeyTemplates.get("AES256_GCM"))
            .withMasterKeyUri(masterKeyUri)
            .build()
            .keysetHandle
            .getPrimitive(RegistryConfiguration.get(), Aead::class.java)
    }

    override val defaultValue: PixelaUserCredentials = PixelaUserCredentials.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): PixelaUserCredentials {
        val encrypted = input.readBytes()
        try {
            val decrypted = aead.decrypt(encrypted, null)
            return PixelaUserCredentials.parseFrom(decrypted)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        } catch (exception: GeneralSecurityException) {
            throw CorruptionException("Cannot decrypt.", exception)
        }
    }

    override suspend fun writeTo(t: PixelaUserCredentials, output: OutputStream) {
        val encrypted = aead.encrypt(t.toByteArray(), null)
        output.write(encrypted)
    }
}