package edu.isep.localnest.data

import android.content.Context

/**
 * UP STARTER: the UI uses temporary memory only.
 * Complete UP-1/2/3 to replace preview with Room persistence.
 * MainActivity already calls these functions on a single worker executor.
 */
class ListingRepository(context: Context) {
    private val dao = AppDatabase.getInstance(context).listingDao()
    private val preview = mutableListOf<Listing>()

    fun getListings(): List<Listing> {
        // TODO UP-1: return the DAO's cached rows instead of the memory preview.
        return preview.sortedWith(compareBy<Listing> { it.monthlyRent }.thenBy { it.id })
    }

    fun cacheSamples() {
        // TODO UP-2: insert SampleData.create() into Room using the DAO.
        // Repeated imports must not duplicate rows or overwrite favorites.
        if (preview.isEmpty()) preview.addAll(SampleData.create())
    }

    fun setFavorite(id: Long, favorite: Boolean) {
        // TODO UP-3: update the matching Room row instead of the in-memory object.
        preview.find { it.id == id }?.favorite = favorite
    }
}
