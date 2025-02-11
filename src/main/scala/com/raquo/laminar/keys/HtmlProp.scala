package com.raquo.laminar.keys

import com.raquo.airstream.core.Source
import com.raquo.laminar.DomApi
import com.raquo.laminar.api.Laminar.{HtmlElement, optionToSetter}
import com.raquo.laminar.codecs.Codec
import com.raquo.laminar.modifiers.KeySetter.PropSetter
import com.raquo.laminar.modifiers.KeyUpdater.PropUpdater
import com.raquo.laminar.modifiers.{KeySetter, KeyUpdater, Modifier, Setter}

/**
  * This class represents a DOM Element Property. Meaning the key that can be set, not a key-value pair.
  *
  * Note: following the Javascript DOM Spec, Properties are distinct from Attributes even when they share a name.
  *
  * @tparam V type of values that this Property can be set to
  * @tparam DomV type of values that this Property holds in the native Javascript DOM
  */
class HtmlProp[V, DomV](
  override val name: String,
  val codec: Codec[V, DomV]
) extends Key { self =>

  def :=(value: V): PropSetter[V, DomV] = {
    new KeySetter[HtmlProp[V, DomV], V, HtmlElement](this, value, DomApi.setHtmlProperty)
  }

  @inline def apply(value: V): PropSetter[V, DomV] = {
    this := value
  }

  def maybe(value: Option[V]): Setter[HtmlElement] = {
    optionToSetter(value.map(v => this := v))
  }

  def <--(values: Source[V]): PropUpdater[V, DomV] = {
    val update = if (name == "value") {
      (element: HtmlElement, nextValue: V, reason: Modifier.Any) =>
        // Checking against current DOM value prevents cursor position reset in Safari
        val currentDomValue = DomApi.getHtmlProperty(element, this)
        if (nextValue != currentDomValue) {
          DomApi.setHtmlProperty(element, this, nextValue)
        }
    } else {
      (element: HtmlElement, nextValue: V, reason: Modifier.Any) => {
        DomApi.setHtmlProperty(element, this, nextValue)
      }
    }
    new KeyUpdater[HtmlElement, HtmlProp[V, DomV], V](
      this,
      values.toObservable,
      update
    )
  }

}
