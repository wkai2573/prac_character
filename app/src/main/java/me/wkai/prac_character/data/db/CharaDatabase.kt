package me.wkai.prac_character.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import me.wkai.prac_character.data.model.Chara

@Database(
	entities = [Chara::class],
	version = 1
)
abstract class CharaDatabase: RoomDatabase() {

	abstract val charaDao:CharaDao

	companion object {
		const val DATABASE_NAME = "character_db"
	}
}