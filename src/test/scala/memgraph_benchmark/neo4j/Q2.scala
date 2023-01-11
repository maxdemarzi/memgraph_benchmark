package memgraph_benchmark.neo4j

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import io.gatling.http.request.builder.HttpRequestBuilder

class Q2 extends Simulation {
  val params = new TestParameters

  before {
    println(s"URL: ${params.URL}")
    println(s"Running test with ${params.userCount} users")
    println(s"Total test duration ${params.testDuration} seconds")
  }

  val httpProtocol: HttpProtocolBuilder =
    http
      .baseUrl(params.URL)
      .acceptHeader("application/json")

  val query = """MATCH (n) RETURN count(n), count(n.age)"""
  val cypherQuery: String = """{"statements" : [{"statement" : "%s", "parameters" : { } }]}""".format(query)
  val queryName = "Q2 - aggregate_count"

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
      exec(request)
    }

  setUp(scn.inject(atOnceUsers(params.userCount))).protocols(httpProtocol).disablePauses
}
