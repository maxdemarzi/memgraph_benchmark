package memgraph_benchmark.neo4j

class TestParameters {
  def getProperty(propertyName: String, defaultValue: String): String = {
    Option(System.getenv(propertyName))
      .orElse(Option(System.getProperty(propertyName)))
      .getOrElse(defaultValue)
  }

  def URL: String = getProperty("URL", "http://localhost:7474")

  def password: String = getProperty("PASSWORD", "swordfish")

  def userCount: Int = getProperty("USERS", "8").toInt

  def testDuration: Int = getProperty("DURATION", "60").toInt
}
