package me.wkai.prac_character.data.db

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
	suspend fun getCharaById(actor:String):Chara?

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertChara(chara:Chara)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertCharaList(charaList:List<Chara>)

	@Delete
	suspend fun deleteChara(chara:Chara)
}