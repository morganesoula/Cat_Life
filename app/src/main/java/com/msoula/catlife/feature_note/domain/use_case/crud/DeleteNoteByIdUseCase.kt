package com.msoula.catlife.feature_note.domain.use_case.crud

import com.msoula.catlife.core.util.Constant
import com.msoula.catlife.core.util.DeleteNoteError
import com.msoula.catlife.core.util.Resource
import com.msoula.catlife.feature_note.data.repository.NoteDataSource

class DeleteNoteByIdUseCase(private val noteDataSource: NoteDataSource) {

    suspend operator fun invoke(noteId: Int): Resource<String> {
        return try {
            noteDataSource.deleteNoteById(noteId = noteId)
            Resource.Success(Constant.NOTE_DELETED)
        } catch (e: DeleteNoteError) {
            e.printStackTrace()
            Resource.Error(e)
        }
    }
}