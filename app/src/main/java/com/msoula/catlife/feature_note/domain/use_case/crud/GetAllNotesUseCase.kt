package com.msoula.catlife.feature_note.domain.use_case.crud

import com.msoula.catlife.feature_note.data.repository.NoteDataSource

class GetAllNotesUseCase(private val noteDataSource: NoteDataSource) {

    operator fun invoke() = noteDataSource.getAllNotes()
}