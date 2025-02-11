package com.raquo.laminar

import scala.annotation.implicitNotFound

package object api extends Implicits {

  val A: Airstream = new Airstream {}

  val L: Laminar.type = Laminar

  // --

  @implicitNotFound(
    "You must import `com.raquo.laminar.api.features.unitArrows` to allow expressions of type `Unit` " +
    "on the right hand side of `-->` methods, because this is not completely safe in Scala 3. " +
    "Please read the documentation first to learn about the caveats: <#TODO[15.0.0] link>"
  )
  trait UnitArrowsFeature

  object features {

    implicit lazy val unitArrows: UnitArrowsFeature = new UnitArrowsFeature {}
  }
}
