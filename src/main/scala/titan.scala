package ohnosequences.scala

import ohnosequences.scala.graph._
import ohnosequences.scala.schema._

import com.thinkaurelius.titan.core

import com.tinkerpop.blueprints.Direction
import scala.collection.JavaConverters.{ asJavaIterableConverter, iterableAsScalaIterableConverter }


case object titan {

  implicit def titanVertexOps:
        VertexOps[core.TitanVertex] =
    new VertexOps[core.TitanVertex] {

      def outV(vertex: Vertex, edgeType: EdgeType): Container[Vertex] =
        vertex
          .query
          .labels(edgeType.label)
          .direction(Direction.OUT)
          .vertexIds.asScala

      def inV(vertex: Vertex, edgeType: EdgeType): Container[Vertex] =
        vertex
          .query
          .labels(edgeType.label)
          .direction(Direction.IN)
          .vertexIds.asScala
  }


  implicit def titanElementOps[E <: core.TitanElement]:
        ElementOps[E] =
    new ElementOps[E] {

    def get[P <: AnyPropertyType](element: Element, property: P): P#ValueType =
      element.getProperty[P#ValueType](property.label)
  }


  implicit def titanGraphOps:
        GraphOps[core.TitanGraph, core.TitanVertex, core.TitanEdge] =
    new GraphOps[core.TitanGraph, core.TitanVertex, core.TitanEdge] {

    def vertices[VT](
      graph: Graph,
      property: VertexPropertyType[VT],
      values: Container[VT]
    ): Container[Vertex] =
      values flatMap { v =>
        graph.query.has(property.label, v)
          .vertices.asScala.asInstanceOf[Container[core.TitanVertex]]
      }

    def edges[VT](
      graph: Graph,
      property: EdgePropertyType[VT],
      values: Container[VT]
    ): Container[Edge] =
      values flatMap { v =>
        graph.query.has(property.label, v)
          .edges.asScala.asInstanceOf[Container[core.TitanEdge]]
      }
  }

}
