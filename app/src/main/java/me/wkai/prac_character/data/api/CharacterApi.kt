package me.wkai.prac_character.data.api

import android.content.res.Resources
import androidx.compose.material.MaterialTheme.colors
import me.wkai.prac_character.MainActivity
import me.wkai.prac_character.data.api.model.Character
import retrofit2.http.GET

//todo 須研究 Retrofit
interface CharacterApi {

	@GET(ApiConstants.END_POINTS)
	suspend fun getCharacter():List<Character>

}
