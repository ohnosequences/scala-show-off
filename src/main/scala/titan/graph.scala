package ohnosequences.scala.titan

import ohnosequences.scala.graph._
import ohnosequences.scala.titan.types._
import com.thinkaurelius.titan.{ core => titan }

import com.tinkerpop.blueprints.Direction
import scala.collection.JavaConverters.{ asJavaIterableConverter, iterableAsScalaIterableConverter }


case object graph extends OtherPriority {

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


  implicit def titanGraphStringVertices:
        Graph[TitanGraph, PropertyLabel, Seq[String], TitanVertices] =
    new Graph[TitanGraph, PropertyLabel, Seq[String], TitanVertices] {

    def lookup(graph: Graph, property: Property, values: Values): Elements =
      Container(
        values flatMap { v =>
          graph.query.has(property, v)
          .vertices.asTitanVertices
        }
      )
  }

  implicit def titanElement[E <: titan.TitanElement, T]:
        Element[Container[E], PropertyLabel, Container[T]] =
    new Element[Container[E], PropertyLabel, Container[T]] {

    def get(elements: Elements, property: Property): Values =
      elements.map{ _.getProperty[T](property) }
  }

}

trait OtherPriority {

  implicit def titanGraphStringEdges:
        Graph[TitanGraph, PropertyLabel, Seq[String], TitanEdges] =
    new Graph[TitanGraph, PropertyLabel, Seq[String], TitanEdges] {

    def lookup(graph: Graph, property: Property, values: Values): Elements =
      Container(
        values flatMap { v =>
          graph.query.has(property, v)
          .edges.asTitanEdges
        }
      )
  }

}
