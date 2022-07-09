package me.wkai.prac_character.di

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.wkai.prac_character.data.api.ApiConstants
import me.wkai.prac_character.data.api.CharacterApi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

//todo 須研究(Hilt)
//@Module : 標注在 class 上，代表這個 class 包含著一到多個標註了 @Provide 的 function。
//@Provide : 標注在 function 上，代表這個 function 會提供 dependency。
//@Inject : 這其實是 Java 的物件，代表物件會被外部設定。
//@Component : @Module 跟需要 dependency 的物件中間的橋樑。
@Module
@InstallIn(SingletonComponent::class) //SingletonComponent = 與Application共生死
object CharacterApiModule {

	@Provides
	@Singleton
	fun provideApi(builder:Retrofit.Builder): CharacterApi {
		return builder
			.build()
			.create(CharacterApi::class.java)
	}

	@Provides
	@Singleton
	fun provideRetrofit(): Retrofit.Builder {
		return Retrofit.Builder()
			.baseUrl(ApiConstants.BASE_URL)
			.addConverterFactory(MoshiConverterFactory.create())
	}

}
