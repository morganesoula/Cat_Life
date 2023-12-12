package com.msoula.catlife.core.presentation
sealed interface OnLifecycleEvent {
    data object OnBackPressed : OnLifecycleEvent
}
