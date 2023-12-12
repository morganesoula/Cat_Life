package com.msoula.catlife.feature_inventory.data.state

import kotlinx.datetime.LocalDate

sealed interface UiActionEvent {
    data class OnSwipeDelete(val item: Any) : UiActionEvent
    data class OnCatFiltered(val catId: Int?) : UiActionEvent
    data class OnDeleteUi(
        val deleteData: Boolean,
        val elementId: Int
    ) : UiActionEvent
    data class OpenDeleteAlertDialog(val itemId: Int) : UiActionEvent
    data class OnCurrentDaySelected(val selectedDate: LocalDate) : UiActionEvent
    data object OnDismissRequest : UiActionEvent

}