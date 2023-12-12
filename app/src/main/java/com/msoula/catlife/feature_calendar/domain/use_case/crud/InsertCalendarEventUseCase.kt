package com.msoula.catlife.feature_calendar.domain.use_case.crud

import com.msoula.catlife.core.util.AddEditEventError
import com.msoula.catlife.core.util.Constant
import com.msoula.catlife.core.util.Resource
import com.msoula.catlife.feature_calendar.data.repository.CalendarEventDataSource

class InsertCalendarEventUseCase(private val repository: CalendarEventDataSource) {

    suspend operator fun invoke(
        title: String,
        description: String?,
        place: String,
        placeLat: String,
        placeLng: String,
        startDate: Long,
        endDate: Long,
        startTime: String,
        endTime: String,
        allDay: Boolean,
        id: Long?
    ): Resource<String> {
        return try {
            repository.insertCalendarEventRepository(
                title,
                description,
                place,
                placeLat,
                placeLng,
                startDate,
                endDate,
                startTime,
                endTime,
                allDay,
                id
            )
            Resource.Success(Constant.EVENT_ADDED_UPDATED)
        } catch (e: AddEditEventError) {
            e.printStackTrace()
            Resource.Error(e)
        }
    }
}
