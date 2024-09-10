package com.example.programingdemo.utlis

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.programingdemo.utlis.Const.SHARED_PREF_USER_DETAILS

class SharedPreferencesHelper constructor(context: Context, prefName: String, mode: Int) {
    private val mCsPref: SharedPreferences = context.getSharedPreferences(prefName, mode)

    companion object {
        @Volatile
        private var instance: SharedPreferencesHelper? = null
        fun getInstance(context: Context): SharedPreferencesHelper {
            return instance ?: synchronized(this) {
                instance ?: SharedPreferencesHelper(
                    context,
                    SHARED_PREF_USER_DETAILS,
                    MODE_PRIVATE
                ).also { instance = it }
            }
        }
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return mCsPref.getBoolean(key, defaultValue)
    }

    fun getString(key: String, defaultValue: String?): String? {
        return mCsPref.getString(key, defaultValue)
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return mCsPref.getInt(key, defaultValue)
    }

    fun getLong(key: String, defaultValue: Long): Long {
        return mCsPref.getLong(key, defaultValue)
    }

    fun putString(key: String, defaultValue: String): Int {
        mCsPref.edit().putString(key, defaultValue).apply()
        return 0
    }

    fun putLong(key: String, defaultValue: String): Int {
        mCsPref.edit().putLong(key, 0).apply()
        return 0
    }

    fun putBoolean(key: String, defaultValue: Boolean): Int {
        mCsPref.edit().putBoolean(key, defaultValue).apply()
        return 0
    }

    fun putInt(key: String, defaultValue: Int): Int {
        mCsPref.edit().putInt(key, defaultValue).apply()
        return 0
    }

    fun isContainsKey(key: String): Boolean {
        return mCsPref.contains(key)
    }

    fun removeKey(key: String) {
        mCsPref.edit().remove(key).apply()
    }
}