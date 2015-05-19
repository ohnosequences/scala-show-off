package ohnosequences.scala

case object graph {

  trait AnyVertex {

    type Vertices
    type Edge

    def outV(vertices: Vertices, edge: Edge): Vertices
    def inV(vertices: Vertices, edge: Edge): Vertices
  }

  trait Vertex[V, E] extends AnyVertex {

    type Vertices = V
    type Edge = E
  }

  implicit def addVertexOps[V, E](v: V)(implicit vo: Vertex[V, E]):
    VertexOps[V, E] =
    VertexOps[V, E](v)(vo)

  case class VertexOps[V, E](v: V)(vo: Vertex[V, E]) {

    def outV(e: E): V = vo.outV(v, e)
    def inV(e: E): V = vo.inV(v, e)
  }
}
