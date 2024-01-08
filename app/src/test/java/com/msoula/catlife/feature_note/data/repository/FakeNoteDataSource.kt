package com.msoula.catlife.feature_note.data.repository

import commsoulacatlifedatabase.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeNoteDataSource : NoteDataSource {

    private val noteList = mutableListOf<Note>()

    override suspend fun insertNote(
        catId: Int,
        catProfilePath: String,
        catName: String,
        date: Long,
        title: String,
        content: String,
        id: Long?
    ) {
        noteList.add(
            Note(
                id ?: 0, catId.toLong(), catProfilePath, catName, date, title, content
            )
        )
    }

    override suspend fun deleteNoteById(noteId: Int) {
        noteList.removeIf { it.id == noteId.toLong() }
    }

    override suspend fun getNoteById(noteId: Int): Note? =
        noteList.firstOrNull { it.id == noteId.toLong() }

    override fun getAllNotes(): Flow<List<Note>> = flow { emit(noteList) }

    override fun getAllCatNotes(idCat: Int): Flow<List<Note>> =
        flow { emit(noteList.filter { it.catId == idCat.toLong() }) }

    override suspend fun deleteNoteByCatId(catId: Int) {
        noteList.removeAll { it.catId == catId.toLong() }
    }

    override suspend fun getLastInsertId(): Long? {
        println("Notes are: $noteList")
        return noteList.lastOrNull()?.id
    }
}