package me.wkai.prac_character.util

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.util.Log
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import me.wkai.prac_character.data.db.CharaDao
import me.wkai.prac_character.data.model.Chara

class CharaProvider : ContentProvider() {

	//Hilt 在ContentProvider 不能直接用構造函數(不能使用@AndroidEntryPoint)
	// 需要宣告進入點(@EntryPoint)
	// https://developer.android.com/training/dependency-injection/hilt-android#not-supported
	@EntryPoint
	@InstallIn(SingletonComponent::class)
	interface CharaProviderEntryPoint {
		fun charaDao():CharaDao
	}
	private val charaDao:CharaDao by lazy {
		val appContext = context?.applicationContext ?: throw IllegalStateException()
		val hiltEntryPoint = EntryPointAccessors.fromApplication(appContext, CharaProviderEntryPoint::class.java)
		hiltEntryPoint.charaDao()
	}

	companion object {
		private const val AUTHORITY:String = "me.wkai.prac_character"
		val uri:Uri = Uri.parse("content://$AUTHORITY/${Chara.TABLE_NAME}")
	}

	override fun onCreate():Boolean {
		return true
	}

	override fun query(
		uri:Uri,
		projection:Array<out String>?,
		selection:String?,
		selectionArgs:Array<out String>?,
		sortOrder:String?
	):Cursor? {
		var localSortOrder:String = sortOrder ?: ""
		var localSelection:String = selection ?: ""
		return charaDao.getCharaList_cursor()
	}

	override fun getType(uri:Uri):String? {
		return null
	}

	override fun insert(uri:Uri, values:ContentValues?):Uri? {
		return null
	}

	override fun delete(uri:Uri, selection:String?, selectionArgs:Array<out String>?):Int {
		return 0
	}

	override fun update(uri:Uri, values:ContentValues?, selection:String?, selectionArgs:Array<out String>?):Int {
		return 0
	}

}