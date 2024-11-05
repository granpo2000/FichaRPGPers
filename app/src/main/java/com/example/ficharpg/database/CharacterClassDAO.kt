package com.example.ficharpg.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import models.CharacterClass

class CharacterClassDAO(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "characters.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_CHARACTER_CLASSES = "character_classes"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_ABILITIES = "abilities" // Representação das habilidades em formato JSON ou similar
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableStatement = """
            CREATE TABLE $TABLE_CHARACTER_CLASSES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_ABILITIES TEXT NOT NULL
            )
        """.trimIndent()
        db?.execSQL(createTableStatement)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CHARACTER_CLASSES")
        onCreate(db)
    }

    fun addCharacterClass(characterClass: CharacterClass): Long {
        val values = ContentValues().apply {
            put(COLUMN_NAME, characterClass.name)
            put(COLUMN_ABILITIES, characterClass.bonus.toString()) // Você pode converter o mapa para string, se necessário
        }
        return writableDatabase.insert(TABLE_CHARACTER_CLASSES, null, values)
    }

    fun getAllCharacterClasses(): List<CharacterClass> {
        val characterClasses = mutableListOf<CharacterClass>()
        val cursor: Cursor = readableDatabase.query(TABLE_CHARACTER_CLASSES, null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            val abilities = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ABILITIES))

            // Aqui você pode converter a string de volta para o formato desejado
            // Para simplificar, vou usar o nome diretamente.
            // Você pode precisar de uma lógica para mapear de volta para a enumeração se necessário.
            val bonusMap = parseAbilities(abilities) // Implementar esta função para converter a string de volta para o mapa
            characterClasses.add(CharacterClass.valueOf(name.toUpperCase())) // Converter para enum
        }
        cursor.close()
        return characterClasses
    }

    fun updateCharacterClass(id: Long, characterClass: CharacterClass): Int {
        val values = ContentValues().apply {
            put(COLUMN_NAME, characterClass.name)
            put(COLUMN_ABILITIES, characterClass.bonus.toString())
        }
        return writableDatabase.update(TABLE_CHARACTER_CLASSES, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    fun deleteCharacterClass(id: Long): Int {
        return writableDatabase.delete(TABLE_CHARACTER_CLASSES, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    private fun parseAbilities(abilities: String): Map<String, Int> {
        // Implemente a lógica para converter a string de volta para o mapa
        // Exemplo: {"Força":2, "Sabedoria":2} para Map<String, Int>
        return emptyMap() // Retorne o mapa correspondente
    }
}
