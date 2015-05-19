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

    def map[T](f: Inside => T): Container[T] = Container(values.map(f))
    def flatMap[T](f: Inside => Iterable[T]): Container[T] = Container(values.flatMap(f))
    def filter(f: Inside => Boolean): Container[Inside] = Container(values.filter(f))
  }

  case class Container[T](val values: Iterable[T]) extends AnyContainer { type Inside = T }


  final type TitanVertices = Container[titan.TitanVertex]
  final type TitanEdges    = Container[titan.TitanEdge]
  final type TitanQueries  = Container[blueprints.Query]

  final type JIterable[T]  = java.lang.Iterable[T]
  final type TitanUnit     = titan.TitanGraph



  /* Conversions */

  implicit final def jIterableOps[T](ts: JIterable[T]): JIterableOps[T] = JIterableOps[T](ts)
  case class JIterableOps[T](val ts: JIterable[T]) extends AnyVal {

    @inline final def asContainer: Container[T] = Container[T](ts.asScala)
  }

  implicit final def blueprintsVerticesOps(es: JIterable[blueprints.Vertex]):
    BlueprintsVerticesOps =
    BlueprintsVerticesOps(es)
  case class BlueprintsVerticesOps(val es: JIterable[blueprints.Vertex]) extends AnyVal {

    @inline final def asTitanVertices: Iterable[titan.TitanVertex] =
      es.asInstanceOf[JIterable[titan.TitanVertex]].asScala //.asContainer
  }

  implicit final def blueprintsEdgesOps(es: JIterable[blueprints.Edge]):
    BlueprintsEdgesOps =
    BlueprintsEdgesOps(es)
  case class BlueprintsEdgesOps(val es: JIterable[blueprints.Edge]) extends AnyVal {

    @inline final def asTitanEdges: Iterable[titan.TitanEdge] =
      es.asInstanceOf[JIterable[titan.TitanEdge]].asScala //.asContainer
  }

}
