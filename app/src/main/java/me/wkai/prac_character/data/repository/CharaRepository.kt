package me.wkai.prac_character.data.repository

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.wkai.prac_character.data.api.CharaApi
import me.wkai.prac_character.data.model.Chara
import retrofit2.Response
import javax.inject.Inject

//@Inject 在class用表示此class是可被注入的

//角色存儲庫
class CharaRepository @Inject constructor(
	//注入api_自動找有@Module+@Provides的fun,產生對應物件 -> 在charaApiModule
	private val charaApi:CharaApi
) {

	suspend fun getCharaList():List<Chara> {
		delay(1000) //測試用加的延遲
		return charaApi.getCharaList()
	}

	suspend fun getCharaList_request():Response<List<Chara>> {
		delay(1000) //測試用加的延遲
		return charaApi.getChara_request()
	}

	fun getCharaList_flow(onError:((Throwable) -> Unit)? = null):Flow<List<Chara>> {
		return flow {
			emit(charaApi.getCharaList())
		}
	}
}