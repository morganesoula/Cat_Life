package com.msoula.catlife.feature_note.domain.use_case.crud

data class CrudNoteUseCase(
    val insertNoteUseCase: InsertNoteUseCase,
    val getNoteByIdUseCase: GetNoteByIdUseCase,
    val getAllNotesUseCase: GetAllNotesUseCase,
    val getAllCatNotesUseCase: GetAllCatNotesUseCase,
    val deleteNoteByIdUseCase: DeleteNoteByIdUseCase,
    val deleteNoteByCatIdUseCase: DeleteNoteByCatIdUseCase,
    val fetchLastInsertedIdUseCase: FetchLastInsertedNoteIdUseCase
)