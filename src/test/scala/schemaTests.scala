package ohnosequences.scala.titan.test

import com.thinkaurelius.titan.{ core => titan }
import titan.Multiplicity._
import java.io.File

class TitanSuite extends org.scalatest.FunSuite with org.scalatest.BeforeAndAfterAll {

  val graph = titan.TitanFactory.open("inmemory")

  override final def beforeAll() {

    // graph.makePropertyKey("name").dataType(classOf[String]).make()
    // graph.makePropertyKey("age").dataType(classOf[Integer]).make()
    // graph.makePropertyKey("text").dataType(classOf[String]).make()
    // graph.makePropertyKey("url").dataType(classOf[String]).make()
    // graph.makePropertyKey("time").dataType(classOf[String]).make()

    // graph.makeVertexLabel("user").make()
    // graph.makeVertexLabel("tweet").make()

    // graph.makeEdgeLabel("posted").multiplicity(ONE2MANY).make()
    // graph.makeEdgeLabel("follows").multiplicity(MULTI).make()

    import com.tinkerpop.blueprints.util.io.graphson._
    GraphSONReader.inputGraph(graph, this.getClass.getResource("/twitter_graph.json").getPath)

    println("Loaded sample Twitter data")
  }

  override final def afterAll() {
    graph.shutdown

    println("Shutdown Titan graph")
  }


  import ohnosequences.scala.graph._
  import ohnosequences.scala.titan.types._
  import ohnosequences.scala.titan.graph._

  test("eval basic queries over sample twitter graph") {

    val names = Seq("@laughedelic", "@eparejatobes", "@evdokim")

    lazy val query = graph
      .lookup("name", names)
      .outV("posted")
      .inV("posted")
      .outV("follows")
      .get("name")

    println("----------------\n")
    query.values.foreach(println)
    println("\n----------------")
  }

}
