package com.msoula.catlife.feature_note.domain.use_case.crud

import com.msoula.catlife.feature_note.data.repository.NoteDataSource
import commsoulacatlifedatabase.Note
import kotlinx.coroutines.flow.Flow

class GetAllCatNotesUseCase(private val noteDataSource: NoteDataSource) {

    operator fun invoke(catId: Int?): Flow<List<Note>> {
        return catId?.let { id: Int ->
            noteDataSource.getAllCatNotes(id)
        } ?: noteDataSource.getAllNotes()
    }
}