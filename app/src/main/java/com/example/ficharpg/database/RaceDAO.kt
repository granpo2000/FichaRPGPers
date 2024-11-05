package com.example.ficharpg.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import models.Race

class RaceDAO(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "characters.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_RACES = "races"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_BONUS = "bonus"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableStatement = """
            CREATE TABLE $TABLE_RACES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_BONUS TEXT NOT NULL
            )
        """.trimIndent()
        db?.execSQL(createTableStatement)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_RACES")
        onCreate(db)
    }

    fun addRace(name: String, bonus: String): Long {
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_BONUS, bonus)
        }
        return writableDatabase.insert(TABLE_RACES, null, values)
    }

    fun getAllRaces(): List<Race> {
        val races = mutableListOf<Race>()
        val cursor: Cursor = readableDatabase.query(TABLE_RACES, null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            val race = Race.fromName(name) // Usando o nome para obter a raça do enum
            if (race != null) {
                races.add(race)
            }
        }
        cursor.close()
        return races
    }

    fun updateRace(id: Long, name: String, bonus: String): Int {
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_BONUS, bonus)
        }
        return writableDatabase.update(TABLE_RACES, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    fun deleteRace(id: Long): Int {
        return writableDatabase.delete(TABLE_RACES, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }
}
