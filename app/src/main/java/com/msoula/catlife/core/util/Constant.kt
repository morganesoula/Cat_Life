package com.msoula.catlife.core.util
object Constant {
    const val PLACE_BASE_URL = "https://maps.googleapis.com/maps/api/place/textsearch/"
    const val DATABASE_NAME = "catlife_db"

    const val NUMERIC_REGEX = "-?\\d+(\\.\\d+)?"
    const val ONLY_NUMBER_REGEX = "^[0-9]*\$"

    const val INVENTORY_ITEM_ADDED_UPDATED = "Item added/updated"
    const val NOTE_ADDED_UPDATED = "Note added/updated"
    const val EVENT_ADDED_UPDATED = "Event added/updated"
    const val CAT_ADDED_UPDATED = "Cat added/updated"
    const val ITEM_DELETED = "Item deleted"
    const val NOTE_DELETED = "Note deleted"
    const val EVENT_BY_ID_DELETE = "Event by id deleted"
    const val CAT_BY_ID_DELETE = "Cat by id deleted"

    // Test Tag
    const val CAT_NAME_TEXT_FIELD = "CAT_NAME_TEXT_FIELD"
    const val CAT_WEIGHT_FIELD = "CAT_WEIGHT_FIELD"
    const val CAT_COAT_FIELD = "CAT_COAT_FIELD"
    const val SUBMIT_CAT_BUTTON = "SUBMIT_CAT_BUTTON"
}