package com.msoula.catlife.feature_note.domain.use_case.crud

import com.msoula.catlife.feature_note.data.repository.NoteDataSource

class FetchLastInsertedNoteIdUseCase(private val noteDataSource: NoteDataSource) {

    suspend operator fun invoke() = noteDataSource.getLastInsertId()
}
