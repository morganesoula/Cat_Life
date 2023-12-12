package com.msoula.catlife.feature_note.domain.use_case.crud

import com.msoula.catlife.feature_note.data.repository.NoteDataSource

class GetNoteByIdUseCase(private val noteDataSource: NoteDataSource) {

    suspend operator fun invoke(noteId: Int) = noteDataSource.getNoteById(noteId)
}