package com.raquo.laminar.fixtures.example.components

import com.raquo.airstream.eventstream.EventStream
import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.ReactiveNode

class Task (
  val $checked: EventStream[Boolean],
  val node: ReactiveNode
)

object Task {
  def apply(name: String): Task = {
    val toggle = Toggle("")

    val node = div(
      toggle.node,
      name,
      br(),
      br()
    )

    new Task(toggle.$checked, node)
  }
}
