package com.marschen.architectureexample.db

import android.content.Context

object PreferencesManager {
    private const val ARCH_SHAREDPREFERENCES = "ArchSharedPreferences"
    private const val KEY_SEARCH_KEYWORD = "key_search_keyword"
    fun setLastSearchKeyword(
        context: Context,
        keyword: String
    ) {
        val sharedPreferences = context.getSharedPreferences(
            ARCH_SHAREDPREFERENCES,
            Context.MODE_PRIVATE
        )
        sharedPreferences.edit().putString(KEY_SEARCH_KEYWORD, keyword).commit()
    }

    fun getLastSearchKeyword(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(
            ARCH_SHAREDPREFERENCES,
            Context.MODE_PRIVATE
        )
        return sharedPreferences.getString(KEY_SEARCH_KEYWORD, "")!!
    }
}