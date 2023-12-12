package com.msoula.catlife.feature_note.data.repository

import commsoulacatlifedatabase.Note
import kotlinx.coroutines.flow.Flow

interface NoteDataSource {
    suspend fun insertNote(catId: Int, catProfilePath: String, catName: String, date: Long, title: String, content: String, id: Long? = null)
    suspend fun deleteNoteById(noteId: Int)
    suspend fun deleteNoteByCatId(catId: Int)
    suspend fun getNoteById(noteId: Int): Note?
    suspend fun getLastInsertId(): Long?
    fun getAllNotes(): Flow<List<Note>>
    fun getAllCatNotes(idCat: Int): Flow<List<Note>>
}