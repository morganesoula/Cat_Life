package com.msoula.catlife.feature_note.domain

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.msoula.CatLifeDatabase
import com.msoula.catlife.feature_note.data.repository.NoteDataSource
import commsoulacatlifedatabase.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class NoteDataSourceSQLImpl(
    db: CatLifeDatabase
): NoteDataSource {

    private val queries = db.noteQueries

    override suspend fun insertNote(
        catId: Int,
        catProfilePath: String,
        catName: String,
        date: Long,
        title: String,
        content: String,
        id: Long?
    ) {
        queries.insertNote(
            id = id,
            catId = catId.toLong(),
            catProfilePath = catProfilePath,
            catName = catName,
            date = date,
            title = title,
            content = content
        )
    }

    override suspend fun deleteNoteById(noteId: Int) {
        withContext(Dispatchers.IO) {
            queries.deleteNoteById(noteId.toLong())
        }
    }

    override suspend fun deleteNoteByCatId(catId: Int) {
        withContext(Dispatchers.IO) {
            queries.deleteNoteByCatId(catId.toLong())
        }
    }

    override suspend fun getNoteById(noteId: Int): Note? {
        return withContext(Dispatchers.IO) {
            queries.getNoteById(noteId.toLong()).executeAsOneOrNull()
        }
    }

    override suspend fun getLastInsertId(): Long? {
        return queries.lastInsertRowId().executeAsOneOrNull()
    }

    override fun getAllNotes(): Flow<List<Note>> {
        return queries.getAllNotes().asFlow().mapToList(Dispatchers.IO)
    }

    override fun getAllCatNotes(idCat: Int): Flow<List<Note>> {
        return queries.getAllNotesForCat(idCat.toLong()).asFlow().mapToList(Dispatchers.IO)
    }
}