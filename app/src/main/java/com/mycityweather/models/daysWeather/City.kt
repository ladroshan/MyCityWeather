package com.mycityweather.models.daysWeather

import com.google.gson.annotations.SerializedName
import com.mycityweather.models.common.Coord
import java.io.Serializable


data class City (
    var country:String,
    var coord: Coord,
    var name:String,
    var id:Int=0,
    var population: Int=0

): Serializable

