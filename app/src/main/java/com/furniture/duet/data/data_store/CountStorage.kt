package com.furniture.duet.data.data_store

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map

object CountStorage {

    private val CART_COUNT = intPreferencesKey("cart_count")
    private val Context.dataStore by preferencesDataStore(name = "datastore_prefs")

    suspend fun increaseCartCount(context: Context) {
        context.dataStore.edit { it[CART_COUNT] = (it[CART_COUNT] ?: 0) + 1 }
    }

    suspend fun decreaseCartCount(context: Context) {
        context.dataStore.edit { it[CART_COUNT] = (it[CART_COUNT] ?: 0) - 1 }
    }

    suspend fun setCartCount(context: Context, newValue: Int) {
        context.dataStore.edit { it[CART_COUNT] = newValue }
    }

    fun getCartCount(context: Context) = context.dataStore.data.map {
        it[CART_COUNT] ?: 0
    }
}