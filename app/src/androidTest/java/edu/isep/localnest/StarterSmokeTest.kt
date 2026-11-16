package edu.isep.localnest

import android.os.SystemClock
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StarterSmokeTest {
    @get:Rule val activity = ActivityTestRule(MainActivity::class.java)
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
        waitFor { activity.activity.findViewById<TextView>(R.id.list_summary).text.startsWith("6 ") }
        instrumentation.runOnMainSync {
            val save = findDescription(activity.activity.window.decorView, "Save favorite 4")
            assertNotNull(save)
            save!!.performClick()
        }
        waitFor { findDescription(activity.activity.window.decorView, "Remove favorite 4") != null }
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
