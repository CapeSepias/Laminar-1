package com.raquo.laminar.receivers

import com.raquo.domtypes.generic.keys.Attr
import com.raquo.domtypes.generic.Modifier
import com.raquo.laminar.DomApi
import com.raquo.laminar.nodes.ReactiveElement
import com.raquo.xstream.XStream
import org.scalajs.dom

class AttrReceiver[V](val attr: Attr[V]) extends AnyVal {

  def <--($value: XStream[V]): Modifier[ReactiveElement[dom.Element]] = {
    new Modifier[ReactiveElement[dom.Element]] {
      override def apply(element: ReactiveElement[dom.Element]): Unit = {
        element.subscribe($value, onNext)

        @inline def onNext(value: V): Unit ={
          DomApi.elementApi.setAttribute(element, attr, value)
        }
      }
    }
  }
}
