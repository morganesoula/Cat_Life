package com.msoula.catlife.di

import com.msoula.catlife.feature_note.data.repository.NoteDataSource
import com.msoula.catlife.feature_note.domain.use_case.crud.CrudNoteUseCase
import com.msoula.catlife.feature_note.domain.use_case.crud.DeleteNoteByCatIdUseCase
import com.msoula.catlife.feature_note.domain.use_case.crud.DeleteNoteByIdUseCase
import com.msoula.catlife.feature_note.domain.use_case.crud.FetchLastInsertedNoteIdUseCase
import com.msoula.catlife.feature_note.domain.use_case.crud.GetAllCatNotesUseCase
import com.msoula.catlife.feature_note.domain.use_case.crud.GetAllNotesUseCase
import com.msoula.catlife.feature_note.domain.use_case.crud.GetNoteByIdUseCase
import com.msoula.catlife.feature_note.domain.use_case.crud.InsertNoteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NoteUseCaseModule {
    @Provides
    @Singleton
    fun provideNoteCrudUseCase(repository: NoteDataSource) = CrudNoteUseCase(
        InsertNoteUseCase(repository),
        GetNoteByIdUseCase(repository),
        GetAllNotesUseCase(repository),
        GetAllCatNotesUseCase(repository),
        DeleteNoteByIdUseCase(repository),
        DeleteNoteByCatIdUseCase(repository),
        FetchLastInsertedNoteIdUseCase(repository)
    )
}