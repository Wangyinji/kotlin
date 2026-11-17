package edu.isep.localnest

import android.os.SystemClock
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import edu.isep.localnest.data.UserPreferences
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StarterSmokeTest {
    @get:Rule val activity = object : ActivityTestRule<MainActivity>(MainActivity::class.java) {
        private lateinit var originalPreferences: UserPreferences

        override fun beforeActivityLaunched() {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            originalPreferences = UserPreferences(context)
            // Apply deterministic filters before MainActivity reads its preferences.
            UserPreferences(context).save("en", favoritesOnly = false, budgetOnly = false)
        }

        override fun afterActivityFinished() {
            originalPreferences.save(
                originalPreferences.language,
                originalPreferences.favoritesOnly,
                originalPreferences.budgetOnly
            )
        }
    }
    private val instrumentation get() = InstrumentationRegistry.getInstrumentation()

    @Test fun starterLaunchesWithImportAndSettingsControls() {
        assertNotNull(activity.activity.findViewById<View>(R.id.load_samples))
        assertNotNull(activity.activity.findViewById<View>(R.id.settings))
        assertNotNull(activity.activity.findViewById<View>(R.id.list_summary))
    }

    @Test fun samplePreviewAndFavoriteToggleWork() {
        waitFor { activity.activity.findViewById<TextView>(R.id.storage_status).text.startsWith("Source") }
        instrumentation.runOnMainSync {
            activity.activity.findViewById<View>(R.id.load_samples).performClick()
        }
        // The previous six cards can remain visible while the import is running.
        waitFor {
            activity.activity.findViewById<TextView>(R.id.storage_status).text.startsWith("Source") &&
                activity.activity.findViewById<TextView>(R.id.list_summary).text.startsWith("6 ")
        }
        var initiallySaved = false
        instrumentation.runOnMainSync {
            val root = activity.activity.window.decorView
            initiallySaved = findDescription(root, "Remove favorite 4") != null
        }
        val initialDescription = if (initiallySaved) "Remove favorite 4" else "Save favorite 4"
        val toggledDescription = if (initiallySaved) "Save favorite 4" else "Remove favorite 4"

        clickFavorite(initialDescription)
        waitFor { findDescription(activity.activity.window.decorView, toggledDescription) != null }

        // Verify the reverse toggle and leave the original favorite state intact.
        clickFavorite(toggledDescription)
        waitFor { findDescription(activity.activity.window.decorView, initialDescription) != null }
    }

    private fun clickFavorite(description: String) {
        instrumentation.runOnMainSync {
            val button = findDescription(activity.activity.window.decorView, description)
            assertNotNull("Missing favorite button: $description", button)
            assertTrue("Favorite button is disabled: $description", button!!.isEnabled)
            assertTrue("Favorite click was not handled: $description", button.performClick())
        }
    }

    private fun waitFor(condition: () -> Boolean) {
        val deadline = SystemClock.uptimeMillis() + 10000
        while (SystemClock.uptimeMillis() < deadline) {
            var ready = false
            instrumentation.runOnMainSync { ready = condition() }
            if (ready) return
            SystemClock.sleep(100)
        }
        fail("Timed out waiting for starter UI")
    }

    private fun findDescription(view: View, description: String): View? {
        if (view.contentDescription?.toString() == description) return view
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                findDescription(view.getChildAt(i), description)?.let { return it }
            }
        }
        return null
    }
}
