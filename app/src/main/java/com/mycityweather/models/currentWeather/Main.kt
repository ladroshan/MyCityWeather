package com.mycityweather.models.currentWeather

import java.io.Serializable

data class Main (
    var temp:Double,
    var temp_min:Double,
    var grnd_level:Double,
    var humidity:Int,
    var pressure:Double,
    //var main:Main
    var sea_level:Double,
    var temp_max:Double


):Serializable


