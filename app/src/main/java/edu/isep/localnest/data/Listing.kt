package edu.isep.localnest.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Room creates the SQLite table from this Kotlin entity. */
@Entity(tableName = "listings")
data class Listing(
    @PrimaryKey val id: Long,
    val title: String,
    val city: String,
    val monthlyRent: Int,
    val area: Int,
    var favorite: Boolean = false
)
