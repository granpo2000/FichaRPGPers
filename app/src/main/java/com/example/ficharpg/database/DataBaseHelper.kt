package com.example.ficharpg.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.ficharpg.models.Character
import models.CharacterClass


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "characters.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_CHARACTERS = "characters"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_RACE = "race"
        private const val COLUMN_SUBRACE = "subrace"
        private const val COLUMN_CLASS = "class" // Aqui se refere à classe do personagem, que será uma string
    }

    private val characterClassDAO = CharacterClassDAO(context)

    override fun onCreate(db: SQLiteDatabase) {
        val createTableStatement = """
            CREATE TABLE $TABLE_CHARACTERS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_RACE TEXT NOT NULL,
                $COLUMN_SUBRACE TEXT,
                $COLUMN_CLASS TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(createTableStatement)

        populateCharacterClasses(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CHARACTERS")
        onCreate(db)
    }

    private fun populateCharacterClasses(db: SQLiteDatabase) {

        CharacterClass.values().forEach { characterClass ->
            characterClassDAO.addCharacterClass(characterClass)
        }
    }




    fun addCharacter(character: Character): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_NAME, character.name)
            put(COLUMN_RACE, character.race)
            put(COLUMN_SUBRACE, character.subRace)
            put(COLUMN_CLASS, character.characterClass.name) // Armazena o nome da enumeração
        }
        val result = db.insert(TABLE_CHARACTERS, null, contentValues)
        db.close()
        return result != -1L
    }


    fun getAllCharacters(): List<Character> {
        val characters = mutableListOf<Character>()
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_CHARACTERS", null)

        if (cursor.moveToFirst()) {
            do {
                val character = Character(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    race = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RACE)),
                    subRace = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUBRACE)),
                    characterClass = CharacterClass.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLASS)).toUpperCase()) // Converte a string de volta para a enumeração
                )
                characters.add(character)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return characters
    }


    fun updateCharacter(character: Character): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_NAME, character.name)
            put(COLUMN_RACE, character.race)
            put(COLUMN_SUBRACE, character.subRace)
            put(COLUMN_CLASS, character.characterClass.name) // Armazena o nome da enumeração
        }
        val result = db.update(TABLE_CHARACTERS, contentValues, "$COLUMN_ID=?", arrayOf(character.id.toString()))
        db.close()
        return result > 0
    }


    fun deleteCharacter(name: String): Boolean {
        val db = this.writableDatabase
        val result = db.delete(TABLE_CHARACTERS, "$COLUMN_NAME=?", arrayOf(name))
        db.close()
        return result > 0
    }
}
