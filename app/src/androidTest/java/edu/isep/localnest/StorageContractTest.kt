package edu.isep.localnest

import android.content.Context
import android.content.ContextWrapper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import edu.isep.localnest.data.ListingRepository
import edu.isep.localnest.data.UserPreferences
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

/** Remove @Ignore after completing the corresponding exercise. */
@RunWith(AndroidJUnit4::class)
class StorageContractTest {
    @Test fun repositoryRestoresRowsAndFavoriteInANewInstance() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val first = ListingRepository(context)
        first.cacheSamples()
        first.setFavorite(1, true)
        first.cacheSamples()
        val reopened = ListingRepository(context)
        assertEquals(6, reopened.getListings().size)
        assertTrue(reopened.getListings().first { it.id == 1L }.favorite)
        first.setFavorite(1, false)
    }

    @Test fun preferencesRestoreInANewInstance() {
        val base = InstrumentationRegistry.getInstrumentation().targetContext
        // Use an isolated preferences file to preserve the student's own settings.
        val isolated = object : ContextWrapper(base) {
            override fun getApplicationContext(): Context = this
            override fun getSharedPreferences(name: String, mode: Int) =
                super.getSharedPreferences("contract_test_$name", mode)
        }
        val storage = isolated.getSharedPreferences("localnest_settings", Context.MODE_PRIVATE)
        storage.edit().clear().commit()
        try {
            val defaults = UserPreferences(isolated)
            assertEquals("en", defaults.language)
            assertFalse(defaults.budgetOnly)
            assertFalse(defaults.favoritesOnly)
            defaults.save("fr", true, true)
            val reopened = UserPreferences(isolated)
            assertEquals("fr", reopened.language)
            assertTrue(reopened.budgetOnly)
            assertTrue(reopened.favoritesOnly)
        } finally { storage.edit().clear().commit() }
    }
}
