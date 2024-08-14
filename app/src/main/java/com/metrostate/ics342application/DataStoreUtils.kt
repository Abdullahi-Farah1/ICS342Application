package com.metrostate.ics342application

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

object DataStoreUtils {
    val Context.dataStore by preferencesDataStore(name = "user_prefs")
}
