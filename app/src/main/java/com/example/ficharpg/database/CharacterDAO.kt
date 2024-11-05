package com.example.ficharpg.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.ficharpg.models.Character

class CharacterDAO(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "characters.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_CHARACTERS = "characters"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_RACE_ID = "race_id"
        private const val COLUMN_SUBRACE_ID = "subrace_id"
        private const val COLUMN_CLASS_ID = "class_id"
        private const val COLUMN_LEVEL = "level"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableStatement = """
            CREATE TABLE $TABLE_CHARACTERS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_RACE_ID INTEGER,
                $COLUMN_SUBRACE_ID INTEGER,
                $COLUMN_CLASS_ID INTEGER,
                $COLUMN_LEVEL INTEGER NOT NULL,
                FOREIGN KEY($COLUMN_RACE_ID) REFERENCES races($COLUMN_ID),
                FOREIGN KEY($COLUMN_SUBRACE_ID) REFERENCES subraces($COLUMN_ID),
                FOREIGN KEY($COLUMN_CLASS_ID) REFERENCES character_classes($COLUMN_ID)
            )
        """.trimIndent()
        db?.execSQL(createTableStatement)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CHARACTERS")
        onCreate(db)
    }

    fun addCharacter(name: String, raceId: Int, subraceId: Int?, classId: Int, level: Int): Long {
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_RACE_ID, raceId)
            put(COLUMN_SUBRACE_ID, subraceId)
            put(COLUMN_CLASS_ID, classId)
            put(COLUMN_LEVEL, level)
        }
        return writableDatabase.insert(TABLE_CHARACTERS, null, values)
    }

    fun getAllCharacters(): List<Character> {
        val characters = mutableListOf<Character>()
        val cursor: Cursor = readableDatabase.query(TABLE_CHARACTERS, null, null, null, null, null, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                val raceId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RACE_ID))
                val subraceId = if (!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_SUBRACE_ID))) {
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SUBRACE_ID))
                } else {
                    null
                }
                val classId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CLASS_ID))
                val level = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LEVEL))

                characters.add(Character(id, name, raceId, subraceId, classId, level))
            } while (cursor.moveToNext())
        }

        cursor.close()
        return characters
    }

    fun updateCharacter(id: Long, name: String, raceId: Int, subraceId: Int?, classId: Int, level: Int): Int {
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_RACE_ID, raceId)
            put(COLUMN_SUBRACE_ID, subraceId)
            put(COLUMN_CLASS_ID, classId)
            put(COLUMN_LEVEL, level)
        }
        return writableDatabase.update(TABLE_CHARACTERS, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    fun deleteCharacter(id: Long): Int {
        return writableDatabase.delete(TABLE_CHARACTERS, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }
}
