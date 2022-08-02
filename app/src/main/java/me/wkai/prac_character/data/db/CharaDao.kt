package me.wkai.prac_character.data.db

import android.database.Cursor
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import me.wkai.prac_character.data.model.Chara

@Dao
interface CharaDao {
	@Query("SELECT * FROM chara")
	fun getCharaList():Flow<List<Chara>>

	@Query("SELECT * FROM chara")
	fun getCharaList_noFlow():List<Chara>

	@Query("SELECT * FROM chara WHERE actor = :actor")
	fun getCharaById(actor:String):Chara?

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertChara(chara:Chara):Long

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertCharaList(charaList:List<Chara>)

	@Delete
	fun deleteChara(chara:Chara)


	//==ContentProvider用==

	@Query("SELECT * FROM chara")
	fun getCharaList_cursor():Cursor

}