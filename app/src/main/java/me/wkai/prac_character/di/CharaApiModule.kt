package me.wkai.prac_character.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.wkai.prac_character.data.api.ApiConst
import me.wkai.prac_character.data.api.CharaApi
import me.wkai.prac_character.data.db.CharaDao
import me.wkai.prac_character.data.db.CharaDatabase
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

//Hilt:
// https://www.carhy.com.tw/%E7%99%BC%E7%87%92%E8%BB%8A%E8%A8%8A/android%E5%AE%98%E6%96%B9%E6%96%B0%E6%8E%A8%E7%9A%84di%E5%BA%AB-hilt/
// @Module: 標記這是一個module, 裡面提供了@Provides (在Kotlin,module可以是object)
// @InstallIn: 使用Hilt時,所有Dagger模塊需要此註釋,通常用單例(SingletonComponent),也可指定要共用此模塊的Component
//  @Provides: 標記方法, 提供"返回值類型"的依賴 (fun名不重要,他會根據回傳的型別自動找要用的fun呼叫)
//  @Binds: 標記抽象方法, 返回接口類型, 實現是方法的唯一參數.
//  @Singleton: application container的scope, 說明是application範圍內的單例.
//  @ActivityScoped對應activity component.
@Module
@InstallIn(SingletonComponent::class)
object CharaApiModule {

	//==Retrofit==

	//生成 builder(請求器), 在此文件有呼叫
	@Provides
	@Singleton
	fun provideRetrofit():Retrofit.Builder {
		return Retrofit.Builder()
			.baseUrl(ApiConst.BASE_URL)
			.addConverterFactory(MoshiConverterFactory.create())
	}

	//生成_角色Api, 在charaRepo有呼叫
	@Provides
	@Singleton
	fun provideApi(builder:Retrofit.Builder):CharaApi {
		return builder
			.build()
			.create(CharaApi::class.java)
	}

	//==Room==

	//生成db
	@Provides
	@Singleton
	fun provideCharaDatabase(app:Application):CharaDatabase {
		return Room.databaseBuilder(
			app,
			CharaDatabase::class.java,
			CharaDatabase.DATABASE_NAME
		)
			.build()
	}

	//生成dao
	@Provides
	@Singleton
	fun provideCharaDao(db:CharaDatabase):CharaDao {
		return db.charaDao
	}
}
