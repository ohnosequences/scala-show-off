package ohnosequences.scala.test

import com.thinkaurelius.titan.core
import scala.collection.JavaConverters.{ asJavaIterableConverter, iterableAsScalaIterableConverter }

class TitanSuite extends org.scalatest.FunSuite with org.scalatest.BeforeAndAfterAll {

  val graph = core.TitanFactory.open("inmemory")

  override final def beforeAll() {
    import com.tinkerpop.blueprints.util.io.graphson._
    GraphSONReader.inputGraph(graph, this.getClass.getResource("/twitter_graph.json").getPath)

    println("Loaded sample Twitter data")
  }

  override final def afterAll() {
    graph.shutdown

    println("Shutdown Titan graph")
  }


  import ohnosequences.scala.schema._
  import ohnosequences.scala.graph._
  import ohnosequences.scala.titan._

  object twitterSchema {

    val user = VertexType("user")
    val name = VertexPropertyType[String]("name")(user)
    val age = VertexPropertyType[Integer]("age")(user)

    val tweet = VertexType("tweet")
    val text = VertexPropertyType[String]("text")(tweet)
    val url  = VertexPropertyType[String]("url")(tweet)

    val posted = EdgeType("posted")(user, tweet)
    val time = EdgePropertyType[String]("time")(posted) // should have some better raw type

    val follows = EdgeType("follows")(user, user)
  }

  test("eval basic queries over sample twitter graph") {
    import twitterSchema._

    lazy val query = graph
      .vertices(name, Seq("@laughedelic", "@eparejatobes", "@evdokim"))
      .outV(posted)
      .inV(posted)
      .outV(follows)
      .get(name)

    println("----------------\n")
    query.foreach(println)
    println("\n----------------")
  }

  test("eval basic queries with edges") {
    import twitterSchema._

    lazy val query = graph
      .vertices(name, Seq("@laughedelic"))
      .outE(posted)
      .source
      .inE(follows)
      .target
      .outE(posted)
      .get(time)

    println("----------------\n")
    query.foreach(println)
    println("\n----------------")
  }

  test("oh") {

    import ohnosequences.scala.slides._

    println( node("foo", node("buuh", e, e), e).depth)

    println( node("foo", node("buuh", e, e), e).showTree )
  }
}
