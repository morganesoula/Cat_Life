package com.msoula.catlife.feature_calendar.custom_places.data.state

sealed interface State<out T> {
    data object LoadingState : State<Nothing>
    data class ErrorState(var exception: Throwable) : State<Nothing>
    data class DataState<T>(var data: T) : State<T>
}
