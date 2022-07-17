package me.wkai.prac_character.data.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import me.wkai.prac_character.data.api.CharaApi
import me.wkai.prac_character.data.db.CharaDao
import me.wkai.prac_character.data.model.Chara
import retrofit2.Response
import javax.inject.Inject

//@Inject 在class用表示此class是可被注入的

//角色存儲庫
class CharaRepository @Inject constructor(
	//注入api_自動找有@Module+@Provides的fun,產生對應物件 -> 在charaApiModule
	private val dao:CharaDao,
	private val api:CharaApi
) {

	suspend fun getCharaList():List<Chara> {
		return api.getCharaList()
	}

	suspend fun getCharaList_request():Response<List<Chara>> {
		return api.getChara_request()
	}

	fun getCharaList_flow_noSave():Flow<List<Chara>> =
		flow {
			emit(api.getCharaList())
		}

	fun getCharaList_flow_save():Flow<List<Chara>> =
		flow {
			val charaList = api.getCharaList()
			dao.insertCharaList(charaList)
			emit(charaList)
		}

	//最終整合，有兩個資料來源: API & Room
	//先從API抓資料，若失敗則抓Room的離線資料，並再包成flow
	// (若Room也失敗則觸發外層的catch)
	fun getCharaList_flow_unity(onApiError:() -> Unit = {}):Flow<List<Chara>> =
		flow {
			val charaList = api.getCharaList()
			dao.insertCharaList(charaList)
			emit(charaList)
		}
			.catch {
				Log.i("@@@", "API取失敗: " + it.message)
				onApiError()
				emit(dao.getCharaList_noFlow())
			}
			.flowOn(Dispatchers.IO)
}