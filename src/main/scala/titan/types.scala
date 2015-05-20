package ohnosequences.scala.titan

case object types {

  import com.tinkerpop.blueprints
  import com.thinkaurelius.titan.{ core => titan }
  import scala.collection.JavaConverters.{ asJavaIterableConverter, iterableAsScalaIterableConverter }

  sealed trait AnyTitanType

  trait AnyContainer extends AnyTitanType {

    type Inside
    type Values = Iterable[Inside]
    val  values: Values

    def map[T](f: Inside => T): Container[T] = new Container(values.map(f))
    def flatMap[T](f: Inside => Iterable[T]): Container[T] = new Container(values.flatMap(f))
    def filter(f: Inside => Boolean): Container[Inside] = new Container(values.filter(f))
  }

  case class Container[T](val values: Iterable[T]) extends AnyContainer { type Inside = T }


  type TitanGraph    = titan.TitanGraph
  type TitanElements = Container[titan.TitanElement]
  type TitanVertices = Container[titan.TitanVertex]
  type TitanEdges    = Container[titan.TitanEdge]

  // case class TitanVertices(vals: Iterable[titan.TitanVertex]) extends Container[titan.TitanVertex](vals)
  // case class TitanEdges(vals: Iterable[titan.TitanEdge]) extends Container[titan.TitanEdge](vals)

  type JIterable[T]  = java.lang.Iterable[T]

  type EdgeLabel = String
  type PropertyLabel = String


  /* Conversions */

  implicit def jIterableOps[T](ts: JIterable[T]): JIterableOps[T] = JIterableOps[T](ts)
  case class JIterableOps[T](val ts: JIterable[T]) extends AnyVal {

    def asContainer: Container[T] = new Container[T](ts.asScala)
  }

  implicit def blueprintsVerticesOps(es: JIterable[blueprints.Vertex]):
    BlueprintsVerticesOps =
    BlueprintsVerticesOps(es)
  case class BlueprintsVerticesOps(val es: JIterable[blueprints.Vertex]) extends AnyVal {

    @inline final def asTitanVertices: Iterable[titan.TitanVertex] =
      es.asInstanceOf[JIterable[titan.TitanVertex]].asScala //.asContainer
  }

  implicit def blueprintsEdgesOps(es: JIterable[blueprints.Edge]):
    BlueprintsEdgesOps =
    BlueprintsEdgesOps(es)
  case class BlueprintsEdgesOps(val es: JIterable[blueprints.Edge]) extends AnyVal {

    @inline final def asTitanEdges: Iterable[titan.TitanEdge] =
      es.asInstanceOf[JIterable[titan.TitanEdge]].asScala //.asContainer
  }

}
