package ohnosequences.scala.titan

case object graph {

  import ohnosequences.scala.graph._
  import ohnosequences.scala.titan.types._

  import com.tinkerpop.blueprints.Direction
  import scala.collection.JavaConverters.{ asJavaIterableConverter, iterableAsScalaIterableConverter }

  implicit def titanVertex:
        Vertex[TitanVertices, String] =
    new Vertex[TitanVertices, String] {

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

}
