package org.skycrawl.nosqlshowcase.server.neo4j.controller;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.traversal.Evaluators;
import org.skycrawl.nosqlshowcase.server.neo4j.Neo4jQueryWrapper;
import org.skycrawl.nosqlshowcase.server.root.common.db.AbstractDataController;
import org.skycrawl.nosqlshowcase.server.root.common.exceptions.DuplicateItemException;
import org.skycrawl.nosqlshowcase.server.root.common.model.WebsiteToCertDataModel;
import org.skycrawl.nosqlshowcase.server.root.common.sample.DefaultCertObject;
import org.skycrawl.nosqlshowcase.server.root.util.CustomOrderSet;

public class Neo4jDataController extends AbstractDataController<Neo4jQueryWrapper>
{
	private enum MyRelations implements RelationshipType
	{
		INCLUDES,
		SIGNEDBY
	}
	
	public Neo4jDataController(Neo4jQueryWrapper connection)
	{
		super(connection);
	}
	
	@Override
	public void init() throws Exception
	{
	}
	
	@Override
	public Set<String> getTLDs() throws Exception
	{
		// cypher is more suited for this simple task
		Set<String> result = new CustomOrderSet<String>();
		for(Map<String, Object> row : getConnection().executeCypherQuery("MATCH (tld)-[:INCLUDES]->(domain) RETURN tld.tld"))
		{
			result.add((String) row.get("tld")); // TODO: untested
		}
		return result;
	}

	@Override
	public WebsiteToCertDataModel getSetDomainAndIntersections(Set<String> tlds) throws Exception
	{
		WebsiteToCertDataModel result = new WebsiteToCertDataModel();
		for(String tld : tlds)
		{
			result.registerSet(tld);
			
			/*
			 * Determine domain and CA pairs for this TLD, include them in the result;
			 */
			
			// cypher version
			/*
			for(Map<String, Object> row : getConnection().executeCypherQuery(String.format("MATCH (tld { tld: '%s' })-[:INCLUDES]->(domain)-[:SIGNEDBY]->(ca) RETURN domain.domain, ca", tld)))
			{
				String domain = (String) row.get("domain");
				Node caNode = (Node) row.get("ca");
				
				DefaultCertObject rootCA = nodeToCert(caNode);
					
				// register the certificate
				result.registerSet(rootCA);
				
				// and register the current found overlap
				result.registerOverlap(domain, tld, rootCA);
			}
			*/
			
			// traversal version
			Map<String, Object> tldNodeRow = getConnection().executeCypherQuery(String.format("MATCH (tld { tld: '%s' }) RETURN tld", tld)).iterator().next();
			Node tldNode = (Node) tldNodeRow.get("tld");
			for(Path position : getConnection().newTraverser()
					.evaluator(Evaluators.atDepth(2)) // only pass paths which have exactly 3 nodes (2 relationships)
					.relationships(MyRelations.INCLUDES).relationships(MyRelations.SIGNEDBY)
					.breadthFirst() // should make things a bit faster
					.traverse(tldNode)
					)
			{
				String domain = (String) position.lastRelationship().getStartNode().getProperty("domain");
				Node caNode = position.endNode();
				
				DefaultCertObject rootCA = nodeToCert(caNode);
					
				// register the certificate
				result.registerSet(rootCA);
				
				// and register the current found overlap
				result.registerOverlap(domain, tld, rootCA);
			}
		}
		return result;
	}
	
	@Override
	public boolean store(URL website, List<DefaultCertObject> certificateChain) throws Exception
	{
		/*
		 * Traversal framework can not add nodes/relationships so we must use Cypher.
		 */
		
		String domain = website.getHost();
		String tld = StringUtils.substringAfterLast(domain, ".");
		
		// use properties instead of labels (there might be syntax problems if labels are used)
		if(existsMatch(String.format("MATCH (domain { domain: '%s' }) RETURN domain", domain)))
		{
			throw new DuplicateItemException();
		}
		else // the current domain has not been processed (stored) yet
		{
			/*
			 * Create a node designating the currently processed domain (not TLD) and
			 * connect it to its corresponding TLD node, all in one statement.
			 */
			
			getConnection().executeCypherQuery(String.format("MATCH (tld { tld: '%s' }) CREATE UNIQUE (tld)-[:INCLUDES]-(domain { domain: '%s' })", tld, domain));
			
			/*
			 * Create a node designating the currently processed root CA and
			 * connect it to its corresponding domain node, all in one statement.
			 */
			DefaultCertObject rootCA = certificateChain.get(certificateChain.size() - 1);
			getConnection().executeCypherQuery(String.format("MATCH (domain { domain: '%s' }) CREATE UNIQUE (domain)-[:SIGNEDBY]-(ca { %s })", domain, caToCypherPropList(rootCA)));
			
			return true;
		}
	}
	
	private String caToCypherPropList(DefaultCertObject cert)
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("cn:'");
		sb.append(cert.getCommonName());
		sb.append("',");
		
		sb.append("on:'");
		sb.append(cert.getOrganizationName());
		sb.append("',");
		
		sb.append("ou:'");
		sb.append(cert.getOrganizationUnit());
		sb.append("',");
		
		sb.append("pka:'");
		sb.append(cert.getPubKeyAlg());
		sb.append("',");
		
		sb.append("ver:'");
		sb.append(cert.getVersion());
		
		sb.append('\'');
		return sb.toString();
	}
	
	private DefaultCertObject nodeToCert(Node node)
	{
		DefaultCertObject result = new DefaultCertObject();
		result.setCommonName((String) node.getProperty("cn"));
		result.setOrganizationName((String) node.getProperty("on"));
		result.setOrganizationUnit((String) node.getProperty("ou"));
		result.setPubKeyAlg((String) node.getProperty("pka"));
		result.setVersion(Integer.parseInt((String) node.getProperty("ver")));
		return result;
	}
	
	private boolean existsMatch(String query)
	{
		return getConnection().executeCypherQuery(query).iterator().hasNext();
	}
	
	//----------------------------------------------------------------
	// INHERITED MASSIVE MANIPULATION ROUTINES
	
	@Override
	public void clearDatabase()
	{
		// deletes all nodes and relationships - cypher is more suited for this
		getConnection().executeCypherQuery("MATCH (n) OPTIONAL MATCH (n)-[r]-() DELETE n,r");
	}
}