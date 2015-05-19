package ohnosequences.scala.titan

case object graph {

  import ohnosequences.scala.graph._
  import ohnosequences.scala.titan.types._

  import com.tinkerpop.blueprints.Direction
  import scala.collection.JavaConverters.{ asJavaIterableConverter, iterableAsScalaIterableConverter }


  implicit def titanVertex:
        Vertex[TitanVertices, EdgeLabel] =
    new Vertex[TitanVertices, EdgeLabel] {

      def outV(vertices: Vertices, edge: Edge): Vertices =
        Container(
          vertices.values flatMap {
            _.query
              .labels(edge)
              .direction(Direction.OUT)
              .vertexIds.asScala
          }
        )

      def inV(vertices: Vertices, edge: Edge): Vertices =
        Container(
          vertices.values flatMap {
            _.query
              .labels(edge)
              .direction(Direction.IN)
              .vertexIds.asScala
          }
        )
  }


  implicit def titanGraphStringVertices[T]:
        Graph[TitanGraph, PropertyLabel, Iterable[T], TitanVertices] =
    new Graph[TitanGraph, PropertyLabel, Iterable[T], TitanVertices] {

    def lookup(graph: Graph, property: Property, values: Values): Elements =
      Container(
        values flatMap { v =>
          graph.query.has(property, v)
          .vertices.asTitanVertices
        }
      )
  }

  implicit def titanGraphStringEdges[T]:
        Graph[TitanGraph, PropertyLabel, Iterable[T], TitanEdges] =
    new Graph[TitanGraph, PropertyLabel, Iterable[T], TitanEdges] {

    def lookup(graph: Graph, property: Property, values: Values): Elements =
      Container(
        values flatMap { v =>
          graph.query.has(property, v)
          .edges.asTitanEdges
        }
      )
  }


  implicit def titanElement[T]:
        Element[TitanElements, PropertyLabel, Iterable[T]] =
    new Element[TitanElements, PropertyLabel, Iterable[T]] {

    def get(elements: Elements, property: Property): Values =
      elements.values.map{ _.getProperty[T](property) }
  }

}
