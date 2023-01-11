package memgraph_benchmark.neo4j

import com.github.javafaker.Faker
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import io.gatling.http.request.builder.HttpRequestBuilder

import java.util.{Locale, Random}

class Q15 extends Simulation {
  val params = new TestParameters

  before {
    println(s"URL: ${params.URL}")
    println(s"Running test with ${params.userCount} users")
    println(s"Total test duration ${params.testDuration} seconds")
  }
  val usFaker = new Faker(new Locale("en-US"), new Random(1234))

  val fakeFeeder: Iterator[Map[String, Any]] = Iterator.continually(
    Map(
      "id" -> usFaker.number().numberBetween(1, 100000)
    )
  )
  
  val httpProtocol: HttpProtocolBuilder =
    http
      .baseUrl(params.URL)
      .acceptHeader("application/json")

  val query = """MATCH (s:User {id: $id})-[*1..2]->(n:User) RETURN DISTINCT n.id, n"""
  val cypherQuery: String = """{"statements" : [{"statement" : "%s", "parameters" : { "id": #{id} } }]}""".format(query)
  val queryName = "Q15 - neighbours_2_with_data"

  def request: HttpRequestBuilder = {
    http(queryName)
      .post("/db/neo4j/tx/commit")
      .basicAuth("neo4j", params.password)
      .body(StringBody(cypherQuery))
      .asJson
      .check(status.is(200))
  }

  val scn: ScenarioBuilder = scenario(queryName)
    .during(params.testDuration) {
      feed(fakeFeeder)
      .exec(request)
    }

  setUp(scn.inject(atOnceUsers(params.userCount))).protocols(httpProtocol).disablePauses
}
