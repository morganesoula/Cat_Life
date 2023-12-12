package com.msoula.catlife.feature_note.domain.use_case.crud

import com.msoula.catlife.core.util.AddEditNoteError
import com.msoula.catlife.core.util.Constant
import com.msoula.catlife.core.util.Resource
import com.msoula.catlife.feature_note.data.repository.NoteDataSource

class InsertNoteUseCase(private val noteDataSource: NoteDataSource) {
    suspend operator fun invoke(
        catId: Int,
        catProfilePath: String,
        catName: String,
        date: Long,
        title: String,
        content: String,
        id: Long? = null
    ): Resource<String> {
        return try {
            noteDataSource.insertNote(catId, catProfilePath, catName, date, title, content, id)
            Resource.Success(Constant.NOTE_ADDED_UPDATED)
        } catch (e: AddEditNoteError) {
            e.printStackTrace()
            Resource.Error(e)
        }
    }
}