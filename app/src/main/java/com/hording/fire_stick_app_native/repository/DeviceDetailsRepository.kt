package com.hording.fire_stick_app_native.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DeviceDetailsRepository @Inject constructor(private val dataStore: DataStore<Preferences>) {
    companion object {
        val TOKEN = stringPreferencesKey("token")
        val BASE_URL = stringPreferencesKey("baseUrl")
        val DEVICE_ID = stringPreferencesKey("device_Id")
        val STATUS = stringPreferencesKey("status")
        val IS_ASSIGNED = booleanPreferencesKey("isAssigned")
        val ASSIGNED_TO = stringPreferencesKey("assignedTo")
    }

    val token = dataStore.data.map { prefs ->
        prefs[TOKEN] ?: ""
    }
    val baseUrl = dataStore.data.map { prefs ->
        prefs[BASE_URL] ?: ""
    }
    val deviceId = dataStore.data.map { prefs ->
        prefs[DEVICE_ID] ?: ""
    }
    val isAssigned = dataStore.data.map { prefs ->
        prefs[IS_ASSIGNED] ?: ""
    }
    val assignedTo = dataStore.data.map { prefs ->
        prefs[ASSIGNED_TO] ?: ""
    }
    val status = dataStore.data.map { prefs ->
        prefs[STATUS] ?: ""
    }

    suspend fun saveToken(value: String) {
        dataStore.edit { prefs ->
            prefs[TOKEN] = value
        }
    }
    suspend fun saveBaseUrl(value: String) {
        dataStore.edit { prefs ->
            prefs[BASE_URL] = value
        }
    }
    suspend fun saveDeviceID(value: String) {
        dataStore.edit { prefs ->
            prefs[DEVICE_ID] = value
        }
    }
    suspend fun saveIsAssigned(value: Boolean) {
        dataStore.edit { prefs ->
            prefs[IS_ASSIGNED] = value
        }
    }
    suspend fun saveAssignedTo(value: String) {
        dataStore.edit { prefs ->
            prefs[ASSIGNED_TO] = value
        }
    }
}