package edu.isep.localnest.data

/** A simulated server response bundled with the app. No network required. */
object SampleData {
    fun create(): List<Listing> = listOf(
        Listing(1, "Sunny studio", "Paris", 780, 22),
        Listing(2, "Garden apartment", "Lyon", 650, 35),
        Listing(3, "Riverside loft", "Nantes", 890, 48),
        Listing(4, "Campus hideaway", "Lille", 520, 19),
        Listing(5, "City balcony", "Paris", 1120, 42),
        Listing(6, "Quiet corner", "Lyon", 720, 31)
    )
}
