package org.skycrawl.nosqlshowcase.server.neo4j;

import javax.script.ScriptEngine;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.traversal.TraversalDescription;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;
import com.tinkerpop.gremlin.groovy.jsr223.GremlinGroovyScriptEngine;

public class Neo4jQueryWrapper
{
	/**
	 * Currently, the only way to communicate with remote Neo4j database using Java is the REST API.
	 * Since there're no decent and up-to-date wrappers for it and all the Neo4j tutorials only focus
	 * on the "Embedded framework" (for Neo4j instances running locally on the same machine), let's
	 * use it too. 
	 */
	private static final String LOCAL_DB_PATH = "/Users/SkyCrawl/Shared/_misc/neo4j-community-2.1.6";
	
	/*
	 * Global database variables.
	 */
	private final GraphDatabaseService neo4jService;
	
	/*
	 * Cypher-specific variables.
	 */
	private final ExecutionEngine cypherEngine;
	
	/*
	 * Gremlin-specific variables. See also:
	 * https://github.com/tinkerpop/gremlin/wiki/Using-Gremlin-through-Java
	 * http://gremlindocs.com/
	 */
	private final ScriptEngine gremlinEngine;
	private final Graph gremlinGraphBlueprint;
	
	public Neo4jQueryWrapper()
	{
		super();
		this.neo4jService = new GraphDatabaseFactory().newEmbeddedDatabase(LOCAL_DB_PATH);
		
		// cypher init
		this.cypherEngine = new ExecutionEngine(neo4jService);
		
		// gremlin init
		this.gremlinEngine = new GremlinGroovyScriptEngine();
		this.gremlinGraphBlueprint = new Neo4jGraph(LOCAL_DB_PATH);
	}
	
	//----------------------------------------------------------------------------------------
	// GLOBAL METHODS
	
	public String getDBVersion()
	{
		/*
		 * TODO: there was probably no reason to add this function when dealing with local
		 * database instances.
		 */
		return null;
	}
	
	public boolean isAlive()
	{
		return neo4jService.isAvailable(1500);
	}

	public void close()
	{
		gremlinGraphBlueprint.shutdown();
		neo4jService.shutdown();
	}
	
	//----------------------------------------------------------------------------------------
	// QUERY-SPECIFIC METHODS
	
	public TraversalDescription newTraverser()
	{
		return neo4jService.traversalDescription();
	}
	
	public ExecutionResult executeCypherQuery(String query)
	{
		return cypherEngine.execute(query);
	}
}