package edu.isep.localnest

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import edu.isep.localnest.data.AppDatabase
import edu.isep.localnest.data.SampleData
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/** Checks supplied infrastructure, not the unfinished repository. */
@RunWith(AndroidJUnit4::class)
class RoomDaoTest {
    private lateinit var database: AppDatabase
    @Before fun open() {
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext, AppDatabase::class.java).build()
    }
    @After fun close() = database.close()

    @Test fun importIsSortedAndIdempotent() {
        val dao = database.listingDao()
        dao.insertAll(SampleData.create())
        dao.insertAll(SampleData.create())
        assertEquals(6, dao.count())
        assertEquals(520, dao.getAll().first().monthlyRent)
        assertEquals(1120, dao.getAll().last().monthlyRent)
    }

    @Test fun reimportPreservesFavoriteAndUpdateTargetsOneRow() {
        val dao = database.listingDao()
        dao.insertAll(SampleData.create())
        dao.setFavorite(1, true)
        dao.insertAll(SampleData.create())
        assertEquals(listOf(1L), dao.getAll().filter { it.favorite }.map { it.id })
        dao.setFavorite(1, false)
        assertTrue(dao.getAll().none { it.favorite })
    }
}
