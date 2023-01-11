# memgraph_benchmark

Reproducing the memgraph benchmark using Gatling

See https://maxdemarzi.com/2023/01/11/bullshit-graph-database-performance-benchmarks/ for details

Download and install Neo4j
Login using neo4j/neo4j and change the password to swordfish

Create an index on the id property of User:

    CREATE INDEX pokec IF NOT EXISTS  FOR (u:User) ON (u.id);

Download the Cypher file: https://s3.eu-west-1.amazonaws.com/deps.memgraph.io/dataset/pokec/benchmark/pokec_medium_import.cypher

From the command line import the data:

    cypher-shell -u neo4j -p swordfish < "/home/max/Downloads/pokec_medium_import.cypher"
    
Wait a few hours for this idiotic way to import data to finish.

Bring up this project in IntelliJ. Make sure you have the Scala plugin installed and set the scala version to something recent.

Run the "Engine" file, it will ask you which query you want to run, pick one... add a description if you want, wait 60 seconds. 
Click the URL to see the results.
