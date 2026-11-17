package edu.isep.localnest.data

import android.content.Context

class UserPreferences(context: Context) {
    private val storage = context.applicationContext
        .getSharedPreferences("localnest_settings", Context.MODE_PRIVATE)

    var language = "en"
        private set
    var favoritesOnly = false
        private set
    var budgetOnly = false
        private set

    init {
        language = if (storage.getString("language", "en") == "fr") "fr" else "en"
        favoritesOnly = storage.getBoolean("favorites_only", false)
        budgetOnly = storage.getBoolean("budget_only", false)
    }

    fun save(language: String, favoritesOnly: Boolean, budgetOnly: Boolean) {
        this.language = if (language == "fr") "fr" else "en"
        this.favoritesOnly = favoritesOnly
        this.budgetOnly = budgetOnly

        storage.edit()
            .putString("language", this.language)
            .putBoolean("favorites_only", this.favoritesOnly)
            .putBoolean("budget_only", this.budgetOnly)
            .apply()
    }
}
