package com.msoula.catlife.feature_calendar.domain.use_case.crud

import com.msoula.catlife.core.util.Constant
import com.msoula.catlife.core.util.DeleteEventByIdError
import com.msoula.catlife.core.util.Resource
import com.msoula.catlife.feature_calendar.data.repository.CalendarEventDataSource

class DeleteCalendarEventByIdUseCase(private val repository: CalendarEventDataSource) {

    suspend operator fun invoke(eventId: Int): Resource<String> {
        return try {
            repository.deleteCalendarEventByIdRepository(eventId)
            Resource.Success(Constant.EVENT_BY_ID_DELETE)
        } catch (e: DeleteEventByIdError) {
            e.printStackTrace()
            Resource.Error(e)
        }
    }
}