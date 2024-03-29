package me.wkai.prac_character.data.model

import android.content.ContentValues
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(tableName = Chara.TABLE_NAME)
@JsonClass(generateAdapter = true)
data class Chara(

	@PrimaryKey
	@ColumnInfo(name = "actor")
	@Json(name = "actor")
	val actor:String,

	@ColumnInfo(name = "alive")
	@Json(name = "alive")
	val alive:Boolean,

	@Ignore
	@Json(name = "alternate_actors")
	val alternateActors:List<String>,

	@Ignore
	@Json(name = "alternate_names")
	val alternateNames:List<String>,

	@ColumnInfo(name = "ancestry")
	@Json(name = "ancestry")
	val ancestry:String,

	@ColumnInfo(name = "dateOfBirth")
	@Json(name = "dateOfBirth")
	val dateOfBirth:String,

	@ColumnInfo(name = "eyeColour")
	@Json(name = "eyeColour")
	val eyeColour:String,

	@ColumnInfo(name = "gender")
	@Json(name = "gender")
	val gender:String,

	@ColumnInfo(name = "hairColour")
	@Json(name = "hairColour")
	val hairColour:String,

	@ColumnInfo(name = "hogwartsStaff")
	@Json(name = "hogwartsStaff")
	val hogwartsStaff:Boolean,

	@ColumnInfo(name = "hogwartsStudent")
	@Json(name = "hogwartsStudent")
	val hogwartsStudent:Boolean,

	@ColumnInfo(name = "house")
	@Json(name = "house")
	val house:String,

	@ColumnInfo(name = "image")
	@Json(name = "image")
	val image:String,

	@ColumnInfo(name = "name")
	@Json(name = "name")
	val name:String,

	@ColumnInfo(name = "patronus")
	@Json(name = "patronus")
	val patronus:String,

	@ColumnInfo(name = "species")
	@Json(name = "species")
	val species:String,

	@Ignore
	@Json(name = "wand")
	val wand:Wand,

	@ColumnInfo(name = "wizard")
	@Json(name = "wizard")
	val wizard:Boolean,

	@ColumnInfo(name = "yearOfBirth")
	@Json(name = "yearOfBirth")
	val yearOfBirth:String?
) {
	companion object {
		const val TABLE_NAME = "chara"
	}

	constructor(
		actor:String,
		alive:Boolean,
		ancestry:String,
		dateOfBirth:String,
		eyeColour:String,
		gender:String,
		hairColour:String,
		hogwartsStaff:Boolean,
		hogwartsStudent:Boolean,
		house:String,
		image:String,
		name:String,
		patronus:String,
		species:String,
		wizard:Boolean,
		yearOfBirth:String,
	) : this(
		actor = actor,
		alive = alive,
		alternateActors = listOf(),
		alternateNames = listOf(),
		ancestry = ancestry,
		dateOfBirth = dateOfBirth,
		eyeColour = eyeColour,
		gender = gender,
		hairColour = hairColour,
		hogwartsStaff = hogwartsStaff,
		hogwartsStudent = hogwartsStudent,
		house = house,
		image = image,
		name = name,
		patronus = patronus,
		species = species,
		wand = Wand("", "", ""),
		wizard = wizard,
		yearOfBirth = yearOfBirth
	)

	constructor(cv:ContentValues) : this(
		actor = cv.getAsString("actor") ?: "",
		alive = cv.getAsBoolean("alive") ?: false,
		alternateActors = listOf(),
		alternateNames = listOf(),
		ancestry = cv.getAsString("ancestry") ?: "",
		dateOfBirth = cv.getAsString("dateOfBirth") ?: "",
		eyeColour = cv.getAsString("eyeColour") ?: "",
		gender = cv.getAsString("gender") ?: "",
		hairColour = cv.getAsString("hairColour") ?: "",
		hogwartsStaff = cv.getAsBoolean("hogwartsStaff") ?: false,
		hogwartsStudent = cv.getAsBoolean("hogwartsStudent") ?: false,
		house = cv.getAsString("house") ?: "",
		image = cv.getAsString("image") ?: "",
		name = cv.getAsString("name") ?: "",
		patronus = cv.getAsString("patronus") ?: "",
		species = cv.getAsString("species") ?: "",
		wand = Wand("", "", ""),
		wizard = cv.getAsBoolean("wizard") ?: false,
		yearOfBirth = cv.getAsString("yearOfBirth") ?: ""
	)
}