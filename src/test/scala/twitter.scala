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

  //implicit def toContainer[T](v: Seq[T]): Container[T] = v*/

  test("eval basic queries over sample twitter graph") {
    import twitterSchema._

    lazy val query = graph
      // FIXME: without name.type it doesn't infer property's type
      .vertices(name, Seq("@laughedelic", "@eparejatobes", "@evdokim"))
      .outV(posted)
      .inV(posted)
      .outV(follows)
      .get(name)

    println("----------------\n")
    query.foreach(println)
    println("\n----------------")
  }
}
