package com.mle.scalajs

import play.api.libs.json.Json

/**
 * @author Michael
 */
object WeatherParser {

  case class MainWeather(temp: Double, humidity: Int, temp_min: Double, temp_max: Double)

  case class Weather(main: String)

  case class WeatherResponse(name: String, weather: Seq[Weather], main: MainWeather)

  implicit val jsonMw = Json.format[MainWeather]
  implicit val jsonW = Json.format[Weather]
  implicit val jsonWr = Json.format[WeatherResponse]

  def parse(body: String): Option[WeatherResponse] = Json.parse(body).asOpt[WeatherResponse]
}
