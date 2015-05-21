package ohnosequences.scala

import ohnosequences.scala.graph._
import ohnosequences.scala.schema._

import com.thinkaurelius.titan.core

import com.tinkerpop.blueprints.Direction
import scala.collection.JavaConverters.{ asJavaIterableConverter, iterableAsScalaIterableConverter }


case object titan {

  implicit def titanVertexOps:
        VertexEdgeOps[core.TitanVertex, core.TitanEdge] =
    new VertexEdgeOps[core.TitanVertex, core.TitanEdge] {

      def outV(vertex: Vertex, edgeType: EdgeType): Many[Vertex] =
        vertex
          .query
          .labels(edgeType.label)
          .direction(Direction.OUT)
          .vertexIds.asScala

      def inV(vertex: Vertex, edgeType: EdgeType): Many[Vertex] =
        vertex
          .query
          .labels(edgeType.label)
          .direction(Direction.IN)
          .vertexIds.asScala

      def outE(vertex: Vertex, edgeType: EdgeType): Many[Edge] =
        vertex
          .query
          .labels(edgeType.label)
          .direction(Direction.OUT)
          .titanEdges.asScala

      def inE(vertex: Vertex, edgeType: EdgeType): Many[Edge] =
        vertex
          .query
          .labels(edgeType.label)
          .direction(Direction.IN)
          .titanEdges.asScala

      def source(edge: Edge): Vertex = edge.getVertex(Direction.OUT)

      def target(edge: Edge): Vertex = edge.getVertex(Direction.IN)
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
      values: Many[VT]
    ): Many[Vertex] =
      values flatMap { v =>
        graph.query.has(property.label, v)
          .vertices.asScala.asInstanceOf[Many[core.TitanVertex]]
      }

    def edges[VT](
      graph: Graph,
      property: EdgePropertyType[VT],
      values: Many[VT]
    ): Many[Edge] =
      values flatMap { v =>
        graph.query.has(property.label, v)
          .edges.asScala.asInstanceOf[Many[core.TitanEdge]]
      }
  }

}
