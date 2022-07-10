package me.wkai.prac_character.data.api

import me.wkai.prac_character.data.model.Character
import retrofit2.Call
import retrofit2.http.*

//Retrofit2:
// https://ithelp.ithome.com.tw/articles/10237402
// https://blog.csdn.net/qiang_xi/article/details/53959437

//角色Api(也有人會用Service)
interface CharacterApi {

	@GET(ApiConstants.END_POINTS)
	suspend fun getCharacter():List<Character>

}