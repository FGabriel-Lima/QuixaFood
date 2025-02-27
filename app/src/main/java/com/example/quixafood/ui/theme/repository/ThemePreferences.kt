import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Criando um DataStore para armazenar as preferências do usuário
val Context.dataStore by preferencesDataStore("settings")

class ThemePreferences(private val context: Context) {
    companion object {
        private val COLOR_SCHEME_KEY = stringPreferencesKey("color_scheme_key")  // Chave para o esquema de cores
        private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")            // Chave para o modo escuro
        private val AUTO_DARK_MODE_KEY = booleanPreferencesKey("auto_dark_mode")  // Chave para o modo automático
    }

    // Lê o estado do modo escuro (Flow permite reatividade automática)
    val darkModeFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[DARK_MODE_KEY] ?: false // Se não existir, retorna false
        }

    // Lê o estado do modo automático
    val autoDarkModeFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[AUTO_DARK_MODE_KEY] ?: false  // Se não existir, retorna false
        }

    // Função para salvar o tema escolhido pelo usuário
    suspend fun saveDarkMode(isDarkMode: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DARK_MODE_KEY] = isDarkMode
            Log.d("ThemePreferences", "Dark Mode saved: $isDarkMode")  // Log para verificar o valor salvo
        }
    }

    // Função para salvar o estado do modo automático
    suspend fun saveAutoDarkMode(isAutoDarkMode: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[AUTO_DARK_MODE_KEY] = isAutoDarkMode
            Log.d("ThemePreferences", "Auto Dark Mode saved: $isAutoDarkMode")  // Log para verificar o valor salvo
        }
    }

    // Função para redefinir todas as preferências
    suspend fun resetPreferences() {
        context.dataStore.edit { preferences ->
            preferences.clear() // Isso irá limpar todas as preferências
        }
    }

    // Função para salvar o esquema de cores
    suspend fun saveColorScheme(scheme: String) {
        context.dataStore.edit { preferences ->
            preferences[COLOR_SCHEME_KEY] = scheme
            Log.d("ThemePreferences", "Color Scheme saved: $scheme")  // Log para verificar o valor salvo
        }
    }

    // Lê o esquema de cores do DataStore
    val colorSchemeFlow: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[COLOR_SCHEME_KEY] ?: "light" // valor padrão
        }
}
