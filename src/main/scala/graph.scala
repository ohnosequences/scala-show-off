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

  trait AnyVertexPropertyType extends AnyPropertyType
  case class VertexPropertyType[VT](val label: String)(val owner: VertexType)
    extends AnyVertexPropertyType { type ValueType = VT }

  trait AnyEdgePropertyType extends AnyPropertyType
  case class EdgePropertyType[VT](val label: String)(val owner: EdgeType)
    extends AnyEdgePropertyType { type ValueType = VT }

}


case object graph {

  import schema._

  type Container[X] = Iterable[X]


  trait AnyVertexOps {

    type Vertex

    def outV(vertex: Vertex, edgeType: EdgeType): Container[Vertex]
    def inV(vertex: Vertex, edgeType: EdgeType): Container[Vertex]
  }

  trait VertexOps[V] extends AnyVertexOps { type Vertex = V }


  implicit def addVerticesSyntax[V](vs: Container[V])(implicit vops: VertexOps[V]):
    VerticesSyntax[V] =
    VerticesSyntax[V](vs)(vops)

  case class VerticesSyntax[V](vs: Container[V])(vops: VertexOps[V]) {

    def outV(et: EdgeType): Container[V] = vs flatMap { vops.outV(_, et) }
    def  inV(et: EdgeType): Container[V] = vs flatMap { vops.inV(_, et) }
  }



  trait AnyElementOps {

    type Element

    def get[P <: AnyPropertyType](element: Element, property: P): P#ValueType
  }

  trait ElementOps[E] extends AnyElementOps { type Element = E }


  implicit def addElementSyntax[E](es: Container[E])(implicit eops: ElementOps[E]):
    ElementSyntax[E] =
    ElementSyntax[E](es)(eops)

  case class ElementSyntax[E](es: Container[E])(eops: ElementOps[E]) {

    def get[P <: AnyPropertyType](p: P): Container[P#ValueType] = es map { eops.get(_, p) }
  }



  trait AnyGraphOps {

    type Graph
    type Vertex
    type Edge

    def vertices[P <: AnyVertexPropertyType](
      graph: Graph,
      property: P,
      values: Container[P#ValueType]
    ): Container[Vertex]

    def edges[P <: AnyEdgePropertyType](
      graph: Graph,
      property: P,
      values: Container[P#ValueType]
    ): Container[Edge]
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

    def vertices[P <: AnyVertexPropertyType]
      (p: P, vs: Container[P#ValueType]): Container[V] =
        gops.vertices(g, p, vs)

    def edges[P <: AnyEdgePropertyType]
      (p: P, vs: Container[P#ValueType]): Container[E] =
        gops.edges(g, p, vs)
  }
}
