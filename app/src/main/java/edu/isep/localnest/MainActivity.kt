package edu.isep.localnest

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import edu.isep.localnest.data.Listing
import edu.isep.localnest.data.ListingRepository
import edu.isep.localnest.data.UserPreferences
import java.util.Locale
import java.util.concurrent.Executors
import kotlin.math.roundToInt

/** Supplied Kotlin UI: the exercise focuses on local storage, not UI construction. */
class MainActivity : Activity() {
    private val ink = Color.rgb(30, 49, 42)
    private val green = Color.rgb(23, 76, 64)
    private val muted = Color.rgb(104, 114, 106)
    private val worker = Executors.newSingleThreadExecutor()
    private lateinit var repository: ListingRepository
    private lateinit var preferences: UserPreferences
    private lateinit var cards: LinearLayout
    private lateinit var summary: TextView
    private lateinit var status: TextView
    private var listings: List<Listing> = emptyList()
    private var loading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = ListingRepository(this)
        preferences = UserPreferences(this)
        buildScreen()
        load(false)
    }

    private fun t(en: String, fr: String) = if (preferences.language == "fr") fr else en

    private fun buildScreen() {
        val root = column().apply {
            setBackgroundColor(Color.rgb(246, 245, 240))
            setPadding(dp(22), 0, dp(22), 0)
            setOnApplyWindowInsetsListener { view, insets ->
                if (Build.VERSION.SDK_INT >= 30) {
                    val bars = insets.getInsets(WindowInsets.Type.systemBars() or WindowInsets.Type.displayCutout())
                    view.setPadding(dp(22) + bars.left, bars.top, dp(22) + bars.right, bars.bottom)
                } else {
                    @Suppress("DEPRECATION")
                    view.setPadding(dp(22) + insets.systemWindowInsetLeft, insets.systemWindowInsetTop,
                        dp(22) + insets.systemWindowInsetRight, insets.systemWindowInsetBottom)
                }
                insets
            }
        }
        setContentView(root)
        root.requestApplyInsets()
        val page = column().apply { setPadding(0, dp(20), 0, dp(24)) }
        root.addView(ScrollView(this).apply {
            isFillViewport = true
            addView(page)
        }, LinearLayout.LayoutParams(-1, -1))

        page.addView(label("LOCALNEST  /  STORAGE LAB", 11, green, true).apply { letterSpacing = 0.12f })
        page.addView(label(t("Find your next\nlittle place.", "Votre prochain\npetit chez-vous."), 34, ink, true)
            .apply { setPadding(0, dp(12), 0, dp(8)) })
        page.addView(label(t("A local-first rental listing exercise.",
            "Un exercice de stockage local de logements."), 14, muted))
        page.addView(label(t("LOCAL STORAGE DEMO\nListings and preferences are saved on this device.",
            "DÉMO DE STOCKAGE LOCAL\nLes annonces et préférences sont enregistrées sur cet appareil."), 12, ink).apply {
            setPadding(dp(14), dp(12), dp(14), dp(12))
            background = background(Color.rgb(239, 228, 199), 14)
        }, full().apply { setMargins(0, dp(20), 0, dp(14)) })

        val actions = LinearLayout(this)
        actions.addView(button(t("Load samples", "Charger les exemples"), true).apply {
            id = R.id.load_samples
            setOnClickListener { load(true) }
        }, LinearLayout.LayoutParams(0, dp(52), 1f))
        actions.addView(button(t("Settings", "Réglages")).apply {
            id = R.id.settings
            setOnClickListener { settingsDialog() }
        }, LinearLayout.LayoutParams(0, dp(52), 0.7f).apply { setMargins(dp(10), 0, 0, 0) })
        page.addView(actions)
        status = label("", 12, muted).apply {
            id = R.id.storage_status
            setPadding(0, dp(10), 0, dp(12))
        }
        page.addView(status)
        summary = label("", 17, ink, true).apply { id = R.id.list_summary }
        page.addView(summary)
        cards = column()
        page.addView(cards)
        page.addView(label("ROOM / SQLITE   +   SHAREDPREFERENCES\nISEP • Skills presentation • Demo", 10, muted)
            .apply { setPadding(0, dp(22), 0, 0) })
        render()
    }

    private fun load(importSamples: Boolean) {
        if (loading) return
        loading = true
        status.text = t("Loading…", "Chargement…")
        worker.execute {
            try {
                if (importSamples) repository.cacheSamples()
                val result = repository.getListings()
                runOnUiThread {
                    if (!isDestroyed) {
                        loading = false
                        listings = result
                        render()
                    }
                }
            } catch (error: Exception) { showError(error) }
        }
    }

    private fun render() {
        cards.removeAllViews()
        val visible = listings.filter {
            (!preferences.favoritesOnly || it.favorite) && (!preferences.budgetOnly || it.monthlyRent <= 800)
        }
        visible.forEach { cards.addView(listingCard(it)) }
        summary.text = resources.getQuantityString(
            if (preferences.language == "fr") R.plurals.summary_fr else R.plurals.summary_en,
            visible.size, visible.size)
        status.text = t("Source: local sample data • no network needed",
            "Source : exemples locaux • aucun réseau requis")
        if (visible.isEmpty()) {
            val message = if (listings.isEmpty())
                t("Your local collection starts here.\nTap Load samples to preview six places.",
                    "Votre collection commence ici.\nChargez les six exemples de logements.")
            else t("No matching places. Try changing your filters.", "Aucun résultat. Modifiez les filtres.")
            cards.addView(label(message, 16, muted).apply { setPadding(dp(18), dp(35), dp(18), dp(35)) })
        }
    }

    private fun listingCard(listing: Listing): View = column().apply {
        setPadding(dp(18), dp(18), dp(18), dp(16))
        background = background(Color.WHITE, 18)
        layoutParams = full().apply { setMargins(0, dp(12), 0, 0) }
        addView(label("${listing.city.uppercase(Locale.ROOT)}  /  ${listing.area} m²", 11, green, true)
            .apply { letterSpacing = 0.08f })
        addView(label(listing.title, 22, ink, true).apply { setPadding(0, dp(10), 0, dp(4)) })
        addView(label("€${listing.monthlyRent}" + t(" / month", " / mois"), 17, muted))
        addView(button(if (listing.favorite) t("★ Saved • remove", "★ Enregistré • retirer")
            else t("☆ Save this place", "☆ Enregistrer")).apply {
            contentDescription = (if (listing.favorite) "Remove favorite " else "Save favorite ") + listing.id
            setOnClickListener {
                if (!loading) {
                    loading = true
                    isEnabled = false
                    worker.execute {
                        try {
                            repository.setFavorite(listing.id, !listing.favorite)
                            val result = repository.getListings()
                            runOnUiThread {
                                if (!isDestroyed) {
                                    loading = false
                                    listings = result
                                    render()
                                }
                            }
                        } catch (error: Exception) { showError(error) }
                    }
                }
            }
        }, full().apply { setMargins(0, dp(12), 0, 0) })
    }

    private fun settingsDialog() {
        val content = column().apply { setPadding(dp(24), dp(8), dp(24), dp(8)) }
        val french = CheckBox(this).apply {
            setText(R.string.french_option)
            isChecked = preferences.language == "fr"
        }
        val favorites = CheckBox(this).apply {
            text = t("Favorites only", "Favoris uniquement")
            isChecked = preferences.favoritesOnly
        }
        val budget = CheckBox(this).apply {
            text = t("Rent up to €800 / month", "Loyer jusqu’à 800 € / mois")
            isChecked = preferences.budgetOnly
        }
        content.addView(french)
        content.addView(favorites)
        content.addView(budget)
        AlertDialog.Builder(this)
            .setTitle(t("Your preferences", "Vos préférences"))
            .setView(content)
            .setNegativeButton(t("Cancel", "Annuler"), null)
            .setPositiveButton(t("Apply", "Appliquer")) { _, _ ->
                preferences.save(if (french.isChecked) "fr" else "en", favorites.isChecked, budget.isChecked)
                buildScreen()
            }.show()
    }

    private fun showError(error: Exception) {
        Log.e("LocalNest", "Storage operation failed", error)
        runOnUiThread {
            if (!isDestroyed) {
                loading = false
                render()
                status.text = t("Storage error. Check Logcat for details.", "Erreur de stockage. Consultez Logcat.")
                Toast.makeText(this, status.text, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun column() = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        layoutParams = full()
    }
    private fun label(value: String, size: Int, color: Int, bold: Boolean = false) = TextView(this).apply {
        text = value
        textSize = size.toFloat()
        setTextColor(color)
        if (bold) setTypeface(Typeface.DEFAULT, Typeface.BOLD)
    }
    private fun button(value: String, primary: Boolean = false) = Button(this).apply {
        text = value
        isAllCaps = false
        textSize = 13f
        setTextColor(if (primary) Color.WHITE else green)
        background = background(if (primary) green else Color.rgb(229, 235, 227), 12)
        setPadding(dp(10), dp(4), dp(10), dp(4))
    }
    private fun background(color: Int, radius: Int) = GradientDrawable().apply {
        setColor(color)
        cornerRadius = dp(radius).toFloat()
    }
    private fun dp(value: Int) = (value * resources.displayMetrics.density).roundToInt()
    private fun full() = LinearLayout.LayoutParams(-1, -2)
    override fun onDestroy() {
        worker.shutdown()
        super.onDestroy()
    }
}
