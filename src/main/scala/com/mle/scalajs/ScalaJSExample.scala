package com.mle.scalajs

import org.scalajs.dom
import org.scalajs.dom.{CanvasRenderingContext2D, HTMLCanvasElement}

import scala.scalajs.js.annotation.JSExport

@JSExport
object ScalaJSExample {
  @JSExport
  def main(canvas: HTMLCanvasElement): Unit = {
    val renderer = canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
    canvas.width = canvas.parentElement.clientWidth
    canvas.height = canvas.parentElement.clientHeight

    paint(canvas, renderer)
  }

  def clock(canvas: HTMLCanvasElement, renderer: CanvasRenderingContext2D) = {
    val gradient = renderer.createLinearGradient(canvas.width / 2 - 100, 0, canvas.width / 2 + 100, 0)
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
