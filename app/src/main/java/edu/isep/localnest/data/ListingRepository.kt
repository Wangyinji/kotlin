package edu.isep.localnest.data

import android.content.Context

/**
 * UP STARTER: the UI uses temporary memory only.
 * Complete UP-1/2/3 to replace preview with Room persistence.
 * MainActivity already calls these functions on a single worker executor.
 */
class ListingRepository(context: Context) {
    private val dao = AppDatabase.getInstance(context).listingDao()

    fun getListings(): List<Listing> {
        return dao.getAll()
    }

    fun cacheSamples() {
        dao.insertAll(SampleData.create())
    }

    fun setFavorite(id: Long, favorite: Boolean) {
        dao.setFavorite(id,favorite)
    }
}
