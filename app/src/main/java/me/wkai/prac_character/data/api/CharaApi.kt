package me.wkai.prac_character.data.api

import me.wkai.prac_character.data.model.Chara
import retrofit2.Response
import retrofit2.http.*

//Retrofit2:
// https://ithelp.ithome.com.tw/articles/10237402
// https://blog.csdn.net/qiang_xi/article/details/53959437

//角色Api(也有人會用Service)
interface CharaApi {

	@GET(ApiConst.END_POINTS)
	suspend fun getCharaList():List<Chara>

	@GET(ApiConst.END_POINTS)
	suspend fun getChara_request():Response<List<Chara>>

}