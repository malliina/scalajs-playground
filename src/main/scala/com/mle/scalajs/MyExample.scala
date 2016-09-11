package com.mle.scalajs

import org.scalajs.dom.ext.Ajax
import org.scalajs.dom.raw.HTMLDivElement
import org.scalajs.dom.{Event, Node, XMLHttpRequest}

import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow
import scala.scalajs.js.JSON
import scala.scalajs.js.annotation.JSExport
import scalatags.JsDom.all._

@JSExport
object MyExample {
  @JSExport
  def main(divElem: HTMLDivElement): Unit = {
    WeatherSearch(divElem)
    //    webFuture2(divElem)
  }

  def webFuture(divElem: HTMLDivElement) = withResponse(divElem) { str =>
    WeatherSearch.toElem(WeatherSearch.result2(str))
  }

  def webFutureRaw(divElem: HTMLDivElement) = withResponse(divElem) { str =>
    val json = JSON.parse(str)
    val pretty = JSON.stringify(json, space = 4)
    pre(pretty).render
  }

  def withResponse(divElem: HTMLDivElement)(f: String => Node) =
    withResponseAction(divElem)(str => divElem.appendChild(f(str)))

  def withResponseAction(divElem: HTMLDivElement)(f: String => Unit) = {
    val city = "Innsbruck"
    val url = s"http://api.openweathermap.org/data/2.5/weather?q=$city"
    Ajax.get(url) foreach { resp =>
      if (resp.status == 200) {
        f(resp.responseText)
      }
    }
  }

  /** Won't work, can't just use play-json compiled for the JVM.
    *
    * @param divElem
    */
  def webFuturePlayJson(divElem: HTMLDivElement) = withResponse(divElem) { str =>
    val err = div("JSON error")
    WeatherParser.parse(str).fold(err.render) { wr =>
      div(
        b("Weather: "),
        ul(
          li(b("Country: "), wr.name)
        )
      ).render
    }
  }

  def web(divElem: HTMLDivElement) = {
    val xhr = new XMLHttpRequest()
    xhr.open("GET", "http://api.openweathermap.org/data/2.5/weather?q=Helsinki")
    xhr.onload = (e: Event) => {
      if (xhr.status == 200) {
        divElem.appendChild(
          pre(xhr.responseText).render
        )
      }
    }
    xhr.send()
  }

  def list(divElem: HTMLDivElement) = {
    val box = input(
      `type` := "text",
      placeholder := "Type here!"
    ).render
    val listings = Seq(
      "Apple", "Apricot", "Banana", "Cherry",
      "Mango", "Mangosteen", "Mandarin",
      "Grape", "Grapefruit", "Guava"
    )
    def renderListings = ul(listings.filter(f => f.toLowerCase.startsWith(box.value.toLowerCase)).map(li(_))).render
    val output = div(renderListings).render
    box.onkeyup = (e: Event) => {
      output.innerHTML = ""
      output.appendChild(renderListings)
    }
    divElem.appendChild(div(h1("Fruits"), p("Select your fruit"), div(box), div(output)).render)
  }

  def capitalForm(divElem: HTMLDivElement) = {
    val box = input(
      `type` := "text",
      placeholder := "Type here!"
    ).render
    val output = span.render
    box.onkeyup = (e: Event) => {
      output.textContent = box.value.toUpperCase
    }
    divElem.appendChild(div(h1("Capital box!"), p("Type here to capitalize"), div(box), div(output)).render)
  }

  def better(divElem: HTMLDivElement) =
    divElem.appendChild(div(h1("Hello, World!")).render)

  def bad(divElem: HTMLDivElement) = {
    divElem.innerHTML =
      s"""
         |<div>
         |<h1>Hello, World!!!</h1>
         |</div>
   """.stripMargin
  }
}
