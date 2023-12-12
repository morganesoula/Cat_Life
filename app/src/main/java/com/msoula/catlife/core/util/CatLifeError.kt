package com.msoula.catlife.core.util

class AddEditInventoryItemError: Exception("An error occured when insering or updating inventory item")
class AddEditNoteError: Exception("An error occured when insering or updating a note")
class AddEditEventError : Exception("An error occured when inserting or updating an event")
class AddEditCatError : Exception("An error occured when inserting or updating a cat")
class GetItemByIdError : Exception("Could not find item")
class DeleteItemError : Exception("Could not find item")
class DeleteNoteError : Exception("Could not delete note")
class DeleteEventByIdError : Exception("Could not delete event by id")
class GetEventByIdError : Exception("Could not find event")
class DeleteCatByIdError : Exception("Could not delete cat by id")
