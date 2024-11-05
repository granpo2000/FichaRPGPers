package com.example.ficharpg.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import models.SubRace

class SubRaceDAO(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "characters.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_SUBRACES = "subraces"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_RACE_ID = "race_id"
        private const val COLUMN_BONUS = "bonus"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableStatement = """
            CREATE TABLE $TABLE_SUBRACES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_RACE_ID INTEGER,
                $COLUMN_BONUS TEXT NOT NULL,
                FOREIGN KEY($COLUMN_RACE_ID) REFERENCES races($COLUMN_ID)
            )
        """.trimIndent()
        db?.execSQL(createTableStatement)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_SUBRACES")
        onCreate(db)
    }

    // Método para adicionar uma sub-raça no banco de dados
    fun addSubRace(name: String, raceId: Int, bonus: String): Long {
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_RACE_ID, raceId)
            put(COLUMN_BONUS, bonus)
        }
        return writableDatabase.insert(TABLE_SUBRACES, null, values)
    }

    // Método para recuperar todas as sub-raças do banco de dados
    fun getAllSubRaces(): List<SubRace> {
        val subRaces = mutableListOf<SubRace>()
        val cursor: Cursor = readableDatabase.query(TABLE_SUBRACES, null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            val subRace = SubRace.fromName(name) // Usando o nome para obter a sub-raça do enum
            if (subRace != null) {
                subRaces.add(subRace)
            }
        }
        cursor.close()
        return subRaces
    }

    // Método para atualizar uma sub-raça no banco de dados
    fun updateSubRace(id: Long, name: String, raceId: Int, bonus: String): Int {
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_RACE_ID, raceId)
            put(COLUMN_BONUS, bonus)
        }
        return writableDatabase.update(TABLE_SUBRACES, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    // Método para excluir uma sub-raça do banco de dados
    fun deleteSubRace(id: Long): Int {
        return writableDatabase.delete(TABLE_SUBRACES, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }
}
