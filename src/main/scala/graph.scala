package ohnosequences.scala

case object schema {

  trait AnyLabeledType {

    val label: String
  }


  trait AnyElementType extends AnyLabeledType

  case class VertexType(val label: String) extends AnyElementType
  case class EdgeType(val label: String)(val src: VertexType, val tgt: VertexType) extends AnyElementType


  trait AnyPropertyType extends AnyLabeledType {

    type ValueType
  }

  case class VertexPropertyType[VT](val label: String)(val vertex: VertexType)
    extends AnyPropertyType { type ValueType = VT }

  case class EdgePropertyType[VT](val label: String)(val edge: EdgeType)
    extends AnyPropertyType { type ValueType = VT }

}


case object graph {

  import schema._

  type Many[X] = Iterable[X]


  trait AnyVertexEdgeOps {

    type Vertex
    type Edge

    def outV(vertex: Vertex, edgeType: EdgeType): Many[Vertex]
    def inV(vertex: Vertex, edgeType: EdgeType): Many[Vertex]

    def outE(vertex: Vertex, edgeType: EdgeType): Many[Edge]
    def inE(vertex: Vertex, edgeType: EdgeType): Many[Edge]

    def source(edge: Edge): Vertex
    def target(edge: Edge): Vertex
  }

  trait VertexEdgeOps[V, E] extends AnyVertexEdgeOps {

    type Vertex = V
    type Edge = E
  }


  implicit def addVerticesSyntax[V, E](vs: Many[V])(implicit veops: VertexEdgeOps[V, E]):
    VerticesSyntax[V, E] =
    VerticesSyntax[V, E](vs)(veops)

  case class VerticesSyntax[V, E](vs: Many[V])(veops: VertexEdgeOps[V, E]) {

    def outV(et: EdgeType): Many[V] = vs flatMap { veops.outV(_, et) }
    def  inV(et: EdgeType): Many[V] = vs flatMap { veops.inV(_, et) }

    def outE(et: EdgeType): Many[E] = vs flatMap { veops.outE(_, et) }
    def  inE(et: EdgeType): Many[E] = vs flatMap { veops.inE(_, et) }
  }


  implicit def addEdgesSyntax[V, E](es: Many[E])(implicit veops: VertexEdgeOps[V, E]):
    EdgesSyntax[V, E] =
    EdgesSyntax[V, E](es)(veops)

  case class EdgesSyntax[V, E](es: Many[E])(veops: VertexEdgeOps[V, E]) {

    def source: Many[V] = es map { veops.source(_) }
    def target: Many[V] = es map { veops.target(_) }
  }


  trait AnyElementOps {

    type Element

    def get[P <: AnyPropertyType](element: Element, property: P): P#ValueType
  }

  trait ElementOps[E] extends AnyElementOps { type Element = E }


  implicit def addElementSyntax[E](es: Many[E])(implicit eops: ElementOps[E]):
    ElementSyntax[E] =
    ElementSyntax[E](es)(eops)

  case class ElementSyntax[E](es: Many[E])(eops: ElementOps[E]) {

    def get[P <: AnyPropertyType](p: P): Many[P#ValueType] = es map { eops.get(_, p) }
  }



  trait AnyGraphOps {

    type Graph
    type Vertex
    type Edge

    def vertices[VT](
      graph: Graph,
      property: VertexPropertyType[VT],
      values: Many[VT]
    ): Many[Vertex]

    def edges[VT](
      graph: Graph,
      property: EdgePropertyType[VT],
      values: Many[VT]
    ): Many[Edge]
  }

  trait GraphOps[G, V, E] extends AnyGraphOps {

    type Graph = G
    type Vertex = V
    type Edge = E
  }


  implicit def addGraphSyntax[G, V, E](g: G)(implicit gops: GraphOps[G, V, E]):
    GraphSyntax[G, V, E] =
    GraphSyntax[G, V, E](g)(gops)

  case class GraphSyntax[G, V, E](g: G)(gops: GraphOps[G, V, E]) {

    def vertices[VT]
      (p: VertexPropertyType[VT], vs: Many[VT]): Many[V] =
        gops.vertices(g, p, vs)

    def edges[VT]
      (p: EdgePropertyType[VT], vs: Many[VT]): Many[E] =
        gops.edges(g, p, vs)
  }
}
