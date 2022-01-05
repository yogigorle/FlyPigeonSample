package com.tekkr.flypigeonsample.utils

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(val context: Context) {

    // At the top level of your kotlin file:
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "FLYPIGEONSAMPLE")


    suspend fun putString(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        context.dataStore.edit {
            it[dataStoreKey] = value
            Log.i("token", value)
        }
    }

    suspend fun putBoolean(key: String, value: Boolean = false) {
        val dataStoreKey = booleanPreferencesKey(key)
        context.dataStore.edit {
            it[dataStoreKey] = value
        }
    }

//    suspend fun getBoolean(key: String): Boolean? {
//        val dataStoreKey = booleanPreferencesKey(key)
//        val preferences = context.dataStore.data.first()
//        return preferences[dataStoreKey]
//    }

    fun getString(key: String): Flow<String?> {
        val dataStoreKey = stringPreferencesKey(key)

        return context.dataStore.data.map { preferences ->
            Log.i("token", preferences[dataStoreKey].toString())
            preferences[dataStoreKey]

        }
    }

    fun getBoolean(key: String = ""): Flow<Boolean?> {
        val dataStoreKey = booleanPreferencesKey(key)

        return context.dataStore.data.map { preferences ->
            preferences[dataStoreKey]
        }
    }
}