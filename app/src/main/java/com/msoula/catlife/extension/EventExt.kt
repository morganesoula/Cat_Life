package com.msoula.catlife.extension

import com.himanshoe.kalendar.KalendarEvent
import commsoulacatlifedatabase.CustomEventEntity
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

fun CustomEventEntity.toCustomKalendarEvent() = CustomKalendarEvent(
    allDay = this.allDay,
    endDate = this.endDate.convertToKotlinLocalDate(),
    endTime = convertStringToLocalDateTime(this.endTime),
    eventDescription = this.description,
    eventId = this.id.toInt(),
    eventName = this.title,
    place = this.place,
    placeLat = this.placeLat.toDouble().toLong(),
    placeLng = this.placeLng.toDouble().toLong(),
    startDate = this.startDate.convertToKotlinLocalDate(),
    startTime = convertStringToLocalDateTime(this.startTime)
)

private fun convertStringToLocalDateTime(value: String): LocalDateTime {
    val (hours, minutes) = value.split(":").map { it.toInt() }
    return LocalDateTime(1970, 1, 1, hours, minutes)
}


data class CustomKalendarEvent(
    val allDay: Boolean,
    override val endDate: LocalDate,
    val endTime: LocalDateTime,
    override val eventDescription: String?,
    val eventId: Int,
    override val eventName: String,
    val place: String,
    val placeLat: Long,
    val placeLng: Long,
    override val startDate: LocalDate,
    val startTime: LocalDateTime
) : KalendarEvent