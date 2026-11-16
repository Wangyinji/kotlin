package edu.isep.localnest.data

import android.content.Context

/** UP-4: replace temporary state with SharedPreferences reads and writes. */
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
        // TODO UP-4a: restore language, favorites_only, budget_only from storage.
        // Defaults: "en", false, false. Only accept language codes "en" and "fr".
    }

    fun save(language: String, favoritesOnly: Boolean, budgetOnly: Boolean) {
        this.language = if (language == "fr") "fr" else "en"
        this.favoritesOnly = favoritesOnly
        this.budgetOnly = budgetOnly
        // TODO UP-4b: persist these fields with storage.edit() and apply().
    }
}
