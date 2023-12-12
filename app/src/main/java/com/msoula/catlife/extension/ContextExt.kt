package com.msoula.catlife

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File

fun Context.getFileName(uri: Uri): String? = when (uri.scheme) {
    ContentResolver.SCHEME_CONTENT -> getContentFileName(uri)
    else -> uri.path?.let(::File)?.name
}

private fun Context.getContentFileName(uri: Uri): String? = kotlin.runCatching {
    contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        cursor.moveToFirst()
        return@use cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME).let(cursor::getString)
    }
}.getOrNull()

fun Context.getAllRaces(): List<String> = listOf(
    this.getString(R.string.abyssinian),
    this.getString(R.string.american_bobtail),
    this.getString(R.string.american_curl),
    this.getString(R.string.american_shorthair),
    this.getString(R.string.american_wirehair),
    this.getString(R.string.arabian_mau),
    this.getString(R.string.australian_mist),
    this.getString(R.string.balinese),
    this.getString(R.string.bambino),
    this.getString(R.string.bengal),
    this.getString(R.string.birman),
    this.getString(R.string.bombay),
    this.getString(R.string.british_longhair),
    this.getString(R.string.british_shorthair),
    this.getString(R.string.burmese),
    this.getString(R.string.california_spangled),
    this.getString(R.string.chantilly_tiffany),
    this.getString(R.string.chartreux),
    this.getString(R.string.chausie),
    this.getString(R.string.cheetoh),
    this.getString(R.string.colorpoint_shorthair),
    this.getString(R.string.cornish_rex),
    this.getString(R.string.cymric),
    this.getString(R.string.cyprus),
    this.getString(R.string.devon_rex),
    this.getString(R.string.donskoy),
    this.getString(R.string.dragon_li),
    this.getString(R.string.egyptian_mau),
    this.getString(R.string.european),
    this.getString(R.string.european_burmese),
    this.getString(R.string.exotic_shorthair),
    this.getString(R.string.havana_brown),
    this.getString(R.string.himalayan),
    this.getString(R.string.japanese_bobtail),
    this.getString(R.string.javanese),
    this.getString(R.string.khao_manee),
    this.getString(R.string.korat),
    this.getString(R.string.kurilian),
    this.getString(R.string.laperm),
    this.getString(R.string.maine_coon),
    this.getString(R.string.malayan),
    this.getString(R.string.manx),
    this.getString(R.string.munchkin),
    this.getString(R.string.nebelung),
    this.getString(R.string.norwegian_forest_cat),
    this.getString(R.string.ocicat),
    this.getString(R.string.oriental),
    this.getString(R.string.persian),
    this.getString(R.string.pixie_bob),
    this.getString(R.string.ragamuffin),
    this.getString(R.string.ragdoll),
    this.getString(R.string.russian_blue),
    this.getString(R.string.savannah),
    this.getString(R.string.scottish_fold),
    this.getString(R.string.selkirk_rex),
    this.getString(R.string.siamese),
    this.getString(R.string.siberian),
    this.getString(R.string.singapura),
    this.getString(R.string.snowshoe),
    this.getString(R.string.somali),
    this.getString(R.string.sphynx),
    this.getString(R.string.tonkinese),
    this.getString(R.string.toyger),
    this.getString(R.string.turkish_angora),
    this.getString(R.string.turkish_van),
    this.getString(R.string.york_chocolate)
)
