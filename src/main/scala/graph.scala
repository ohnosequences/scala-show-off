package ohnosequences.scala

case object graph {

  object AnyVertex {

    type of[V,E] = AnyVertex { type Vertices = V; type Edge = E }
  }
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

  implicit def addVertexOps[V, E](v: V)(implicit vo: AnyVertex { type Vertices = V; type Edge = E } ):
    VertexOps[V, E] =
    VertexOps[V, E](v)(vo)

  case class VertexOps[V, E](v: V)(vo: AnyVertex { type Vertices = V; type Edge = E }) {

    def outV(e: E): V = vo.outV(v, e)
    def inV(e: E): V = vo.inV(v, e)
  }



  trait AnyElement {

    type Elements
    type Property
    type Values

    def get(elements: Elements, property: Property): Values
  }

  trait Element[E, P, V] extends AnyElement {

    type Elements = E
    type Property = P
    type Values = V
  }

  implicit def addElementOps[E, P, V](e: E)(implicit eo: Element[E, P, V]):
    ElementOps[E, P, V] =
    ElementOps[E, P, V](e)(eo)

  case class ElementOps[E, P, V](e: E)(eo: Element[E, P, V]) {

    def get(p: P): V = eo.get(e, p)
  }



  trait AnyGraph {

    type Graph
    type Property
    type Values
    type Elements

    def lookup(graph: Graph, property: Property, values: Values): Elements
  }

  trait Graph[G, P, V, E] extends AnyGraph {

    type Graph = G
    type Property = P
    type Values = V
    type Elements = E
  }

  implicit def addGraphOps[G, P, V, E](g: G)(implicit go: Graph[G, P, V, E]):
    GraphOps[G, P, V, E] =
    GraphOps[G, P, V, E](g)(go)

  case class GraphOps[G, P, V, E](g: G)(go: Graph[G, P, V, E]) {

    def lookup(p: P, v: V): E = go.lookup(g, p, v)
  }
}
