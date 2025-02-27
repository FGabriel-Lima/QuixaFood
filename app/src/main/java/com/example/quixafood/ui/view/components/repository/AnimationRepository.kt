import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Criando um DataStore para armazenar as preferências do usuário
val Context.dataStore2 by preferencesDataStore("settingsAnimation")

class AnimationPreferences(private val context: Context) {
    companion object {
        private val ANIMATION_MODE_KEY = booleanPreferencesKey("animation_mode")
    }

    val AnimationModeFlow: Flow<Boolean> = context.dataStore2.data
        .map { preferences ->
            preferences[ANIMATION_MODE_KEY] ?: false
        }


    suspend fun saveAnimationMode(isAutoDarkMode: Boolean) {
        context.dataStore2.edit { preferences ->
            preferences[ANIMATION_MODE_KEY] = isAutoDarkMode
            Log.d("AnimationsPreferences", "Animation Mode saved: $isAutoDarkMode")  // Log para verificar o valor salvo
        }
    }

    suspend fun resetPreferences() {
        context.dataStore2.edit { preferences ->
            preferences.clear() // Isso irá limpar todas as preferências
        }
    }

}
