package com.mle.scalajs

import org.scalajs.dom.extensions.Ajax
import org.scalajs.dom.{Event, HTMLDivElement, XMLHttpRequest}

import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow
import scala.scalajs.js.annotation.JSExport
import scalatags.JsDom.all._

/**
 * @author Michael
 */
@JSExport
object MyExample {
  @JSExport
  def main(divElem: HTMLDivElement): Unit = {
    webFuture(divElem)
  }

  def webFuture(divElem: HTMLDivElement) = {
    val url = "http://api.openweathermap.org/data/2.5/weather?q=Innsbruck"
    Ajax.get(url).foreach(req => divElem.appendChild(pre(req.responseText).render))
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

  def better(divElem: HTMLDivElement) = {
    divElem.appendChild(div(h1("Hello, World!")).render)
  }

  def bad(divElem: HTMLDivElement) = {
    divElem.innerHTML = s"""
     |<div>
     |<h1>Hello, World!!!</h1>
     |</div>
   """.stripMargin
  }
}
