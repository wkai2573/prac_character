package me.wkai.prac_character.data.repository

import kotlinx.coroutines.delay
import me.wkai.prac_character.data.api.CharacterApi
import me.wkai.prac_character.data.model.Character
import javax.inject.Inject

//@Inject 在class用表示此class是可被注入的

//角色存儲庫
class CharacterRepo @Inject constructor(
	//注入api_自動找有@Module+@Provides的fun,產生對應物件 -> 在CharacterApiModule
	private val characterApi:CharacterApi
) {

	suspend fun getCharacters():List<Character> {
		delay(1000) //測試用加的延遲
		return characterApi.getCharacter()
	}

}