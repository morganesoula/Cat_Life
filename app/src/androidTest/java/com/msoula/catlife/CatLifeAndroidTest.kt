package com.msoula.catlife

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.base.DefaultFailureHandler
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.msoula.CatLifeDatabase
import com.msoula.catlife.core.adapter.QuantityAdapter
import com.msoula.catlife.core.adapter.WeightAdapter
import com.msoula.catlife.core.data.CatDataSource
import com.msoula.catlife.core.domain.CatDataSourceSQLImpl
import com.msoula.catlife.core.preview.cats
import com.msoula.catlife.core.preview.event
import com.msoula.catlife.core.preview.note
import com.msoula.catlife.feature_calendar.data.repository.CalendarEventDataSource
import com.msoula.catlife.feature_calendar.domain.CalendarEventDataSourceSQLImpl
import com.msoula.catlife.feature_note.data.repository.NoteDataSource
import com.msoula.catlife.feature_note.domain.NoteDataSourceSQLImpl
import commsoulacatlifedatabase.CatEntity
import commsoulacatlifedatabase.InventoryItemEntity
import dagger.hilt.android.testing.HiltAndroidRule
import okhttp3.internal.format
import org.junit.Before
import org.junit.Rule
import java.util.Locale

abstract class CatLifeAndroidTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    protected lateinit var context: Context
    private lateinit var db: CatLifeDatabase
    lateinit var catDataSource: CatDataSource
    lateinit var noteDataSource: NoteDataSource
    lateinit var calendarEventDataSource: CalendarEventDataSource

    private var anrCount = 0
    private val rootViewWithoutFocusExceptionMsg = format(
        Locale.ROOT.country,
        "Waited for the root of the view hierarchy to have "
                + "window focus and not request layout for 10 seconds. If you specified a non "
                + "default root matcher, it may be picking a root that never takes focus. "
                + "Root:"
    )

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()

        hiltRule.inject()

        db = CatLifeDatabase(
            createTestDriver(),
            CatEntity.Adapter(WeightAdapter()),
            InventoryItemEntity.Adapter(QuantityAdapter())
        )

        catDataSource = CatDataSourceSQLImpl(db)
        noteDataSource = NoteDataSourceSQLImpl(db)
        calendarEventDataSource = CalendarEventDataSourceSQLImpl(db)

        populateCatDb()
        populateNoteDb()
        populateEventDb()

        Espresso.setFailureHandler { error, viewMatcher ->
            if (error.message!!.contains(rootViewWithoutFocusExceptionMsg) && anrCount < 3) {
                anrCount++
                handleAnrDialog()
            } else {
                DefaultFailureHandler(context).handle(error, viewMatcher)
            }
        }
    }

    private fun createTestDriver(): SqlDriver {
        return AndroidSqliteDriver(CatLifeDatabase.Schema, context = context, null)
    }

    private fun populateCatDb() {
        db.catQueries.insertCat(
            cats().first().id,
            cats().first().name,
            cats().first().profilePicturePath,
            cats().first().gender,
            cats().first().neutered,
            cats().first().birthdate,
            cats().first().weight,
            cats().first().race,
            cats().first().coat,
            cats().first().diseases,
            cats().first().vaccineDate,
            cats().first().dewormingDate,
            cats().first().fleaDate
        )
    }

    private fun populateNoteDb() {
        db.noteQueries.insertNote(
            note().id,
            note().catId,
            note().catProfilePath,
            note().catName,
            note().date,
            note().title,
            note().content
        )
    }

    private fun populateEventDb() {
        db.eventQueries.insertEvent(
            event().id,
            event().title,
            event().description,
            event().place,
            event().placeLat,
            event().placeLng,
            event().startDate,
            event().endDate,
            event().startTime,
            event().endTime,
            event().allDay
        )
    }

    private fun handleAnrDialog() {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val waitButton = device.findObject(UiSelector().textContains("wait"))

        if (waitButton.exists()) waitButton.click()
    }
}