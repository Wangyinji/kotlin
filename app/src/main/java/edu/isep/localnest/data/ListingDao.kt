package edu.isep.localnest.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/** Supplied SQL operations. The repository is called on a worker thread. */
@Dao
interface ListingDao {
    @Query("SELECT * FROM listings ORDER BY monthlyRent ASC, id ASC")
    fun getAll(): List<Listing>

    // Reimporting a listing must preserve its existing favorite value.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(listings: List<Listing>)

    @Query("UPDATE listings SET favorite = :favorite WHERE id = :id")
    fun setFavorite(id: Long, favorite: Boolean)

    @Query("SELECT COUNT(*) FROM listings")
    fun count(): Int
}
