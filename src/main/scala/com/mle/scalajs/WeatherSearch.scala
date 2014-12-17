package com.mle.scalajs

import com.mle.scalajs.WeatherSearch.{CityNotFound, ErrorMessage, GenericError, WeatherResult}
import org.scalajs.dom.extensions.Ajax
import org.scalajs.dom.{Event, HTMLDivElement}

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow
import scala.scalajs.js
import scala.scalajs.js.JSON
import scala.util.{Failure, Success, Try}
import scalatags.JsDom.all._

/**
 * @author Michael
 */
case class WeatherSearch(divElem: HTMLDivElement) {
  lazy val box = input(
    `type` := "text",
    placeholder := "Type here!"
  ).render
  lazy val output = div(
    height := "400px",
    overflowY := "scroll"
  ).render
  box.onkeyup = (e: Event) => {
    execute(box.value, output)
  }
  divElem.appendChild(
    div(
      div(box),
      div(output)
    ).render
  )

  def execute(city: String, target: HTMLDivElement) = {
    renderStatus(s"Loading $city...")
    search(city).map(r => render(r, output)).recover {
      case _: NoSuchElementException => () // outdated response
      case t: Throwable => renderStatus(s"Failure: ${t.getClass.getName}: ${t.getMessage}")
    }
  }

  def renderStatus(status: String) = output.innerHTML = status

  def search(city: String): Future[Either[ErrorMessage, WeatherResult]] = {
    run(city)(toResult)
  }

  def render(r: Either[ErrorMessage, WeatherResult], target: HTMLDivElement) = {
    target.innerHTML = ""
    r.fold(err => target.innerHTML = err.message, res => target.appendChild(WeatherSearch.toElem(res).render))
  }

  def run[E, T](city: String)(f: String => Either[E, T]): Future[Either[E, T]] = {
    val url = s"http://api.openweathermap.org/data/2.5/weather?q=$city"
    Ajax.get(url).filter(r => r.status == 200 && city == box.value).map(r => f(r.responseText))
  }

  def flatten[T](f: Future[Try[T]]): Future[T] = f.flatMap {
    case Success(t) => Future.successful(t)
    case Failure(t) => Future.failed(t)
  }

  def toResult(body: String): Either[ErrorMessage, WeatherResult] = {
    println(s"Parsing $body")
    val json = JSON.parse(body)
    val code = json.cod.toString.toInt
    code match {
      case 200 =>
        Right(WeatherSearch.result(json))
      case 404 =>
        Left(CityNotFound)
      case other =>
        Left(GenericError(s"Error $other"))
    }
  }
}

object WeatherSearch {

  def result2(json: String) = result(JSON.parse(json))

  def result(json: js.Dynamic) = {
    val jsonMain = json.main
    val name = json.name.toString
    val weather = json.weather.pop().main.toString
    val min = celsius(jsonMain.temp_min)
    val max = celsius(jsonMain.temp_max)
    val humid = jsonMain.humidity.toString
    WeatherResult(name, weather, min, max, humid.toInt)
  }

  def toElem(res: WeatherResult) = {
    div(
      b(s"Weather: "),
      ul(
        li(b("City: "), res.city),
        li(b("Weather "), res.weather),
        li(b("Temp "), res.tempMin, " - ", res.tempMax),
        li(b("Humidity "), res.humidity, "%"))).render
  }

  trait ErrorMessage {
    def message: String
  }

  case class GenericError(message: String) extends ErrorMessage

  case object CityNotFound extends ErrorMessage {
    override def message: String = "City not found"
  }

  def celsius(kelvins: js.Dynamic) = (kelvins.asInstanceOf[Double] - 273.15).toInt

  case class WeatherResult(city: String, weather: String, tempMin: Int, tempMax: Int, humidity: Int)

}

