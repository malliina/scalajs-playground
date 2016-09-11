package com.mle.scalajs

import org.scalajs.dom.CanvasRenderingContext2D

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom
import org.scalajs.dom.raw.HTMLCanvasElement
import scala.scalajs.js.timers

@JSExport
object ScalaJSExample {
  @JSExport
  def main(canvas: HTMLCanvasElement): Unit = {
    val renderer = canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
    canvas.width = canvas.parentElement.clientWidth
    canvas.height = canvas.parentElement.clientHeight

    //    paint(canvas, renderer)
    //    clock(canvas, renderer)
    flappyBird(canvas, renderer)
  }

  def flappyBird(canvas: HTMLCanvasElement, renderer: CanvasRenderingContext2D) = {
    new FlappyBird(canvas, renderer)
  }

  def clock(canvas: HTMLCanvasElement, renderer: CanvasRenderingContext2D) = {
    val gradient = renderer.createLinearGradient(canvas.width / 2 - 100, 0, canvas.width / 2 + 100, 0)
    gradient.addColorStop(0, "red")
    gradient.addColorStop(0.5, "green")
    gradient.addColorStop(1, "blue")
    renderer.fillStyle = gradient
    renderer.textAlign = "center"
    renderer.textBaseline = "middle"

    def render() = {
      val date = new js.Date()
      renderer.clearRect(0, 0, canvas.width, canvas.height)

      renderer.font = "75px sans-serif"
      val secs = date.getSeconds()
      val secString = if (secs >= 10) secs else s"0$secs"
      renderer.fillText(
        Seq(
          date.getHours(),
          date.getMinutes(),
          secString
        ).mkString(":"),
        x = canvas.width / 2,
        y = canvas.height / 2
      )
    }
    timers.setInterval(1000)(render())
  }

  def paint(canvas: HTMLCanvasElement, renderer: CanvasRenderingContext2D) = {
    renderer.fillStyle = "#f8f8f8"
    renderer.fillRect(0, 0, canvas.width, canvas.height)

    renderer.fillStyle = "black"
    var down = false
    canvas.onmousedown = (e: dom.MouseEvent) => {
      down = true
    }
    canvas.onmouseup = (e: dom.MouseEvent) => {
      down = false
    }
    canvas.onmousemove = (e: dom.MouseEvent) => {
      val rect = canvas.getBoundingClientRect()
      if (down) {
        renderer.fillRect(e.clientX - rect.left, e.clientY - rect.top, 10, 10)
      }
    }
  }
}
