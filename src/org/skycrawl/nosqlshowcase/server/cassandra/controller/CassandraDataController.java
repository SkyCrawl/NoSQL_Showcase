package org.skycrawl.nosqlshowcase.server.cassandra.controller;

import java.net.URL;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.skycrawl.nosqlshowcase.server.root.common.db.AbstractDataController;
import org.skycrawl.nosqlshowcase.server.root.common.exceptions.DuplicateItemException;
import org.skycrawl.nosqlshowcase.server.root.common.model.WebsiteToCertDataModel;
import org.skycrawl.nosqlshowcase.server.root.common.sample.DefaultCertObject;
import org.skycrawl.nosqlshowcase.server.root.util.CustomOrderSet;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class CassandraDataController extends AbstractDataController<Session>
{
	/*
	 * Various static fields.
	 */
	private static final String NAME_KEYSPACE = "cassandra_showcase";
	private static final String NAME_TABLE_CA = "table_cas";
	private static final String NAME_TABLE_DOMAIN = "table_domains";
	
	/*
	 * Prepared statements.
	 */
	private PreparedStatement insertDomainStatement;
	private PreparedStatement insertCAStatement;
	private PreparedStatement existsDomainStatement;
	private PreparedStatement getCertificateStatement;
	
	public CassandraDataController(Session connection)
	{
		super(connection);
		
		this.insertDomainStatement = null;
		this.insertCAStatement = null;
	}
	
	@Override
	public void init() throws Exception
	{
		// first setup database
		getConnection().execute(String.format("CREATE KEYSPACE IF NOT EXISTS %s WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 3};", NAME_KEYSPACE));
		getConnection().execute(String.format("USE %s;", NAME_KEYSPACE));
		getConnection().execute(String.format("CREATE TABLE IF NOT EXISTS %s (" +
				"id text PRIMARY KEY," + // TODO: probably better to use UUID
				"commonName text," +
				"organizationName text," +
				"organizationUnit text," +
				"pubKeyAlg text," +
				"version int," +
				");",
			    
			    NAME_TABLE_CA
		));
		getConnection().execute(String.format("CREATE TABLE IF NOT EXISTS %s (" +
				"domain text," +
				"tld text," +
			    "caKey text," +
			    "PRIMARY KEY (tld, domain)" +
			    ");",
			    
			    NAME_TABLE_DOMAIN
		));
		
		// and then prepare queries
		insertCAStatement = getConnection().prepare(String.format("INSERT INTO %s (id, commonName, organizationName, organizationUnit, pubKeyAlg, version) VALUES (?, ?, ?, ?, ?, ?);", NAME_TABLE_CA));
		insertDomainStatement = getConnection().prepare(String.format("INSERT INTO %s (domain, tld, caKey) VALUES (?, ?, ?);", NAME_TABLE_DOMAIN));
		existsDomainStatement = getConnection().prepare(String.format("SELECT * FROM %s WHERE domain=? ALLOW FILTERING;", NAME_TABLE_DOMAIN));
		getCertificateStatement = getConnection().prepare(String.format("SELECT * FROM %s WHERE id=?;", NAME_TABLE_CA));
	}
	
	@Override
	public Set<String> getTLDs() throws Exception
	{
		Set<String> result = new CustomOrderSet<String>();
		for(Row row : getConnection().execute(String.format("SELECT DISTINCT tld FROM %s;", NAME_TABLE_DOMAIN)).all())
		{
			result.add(row.getString("tld"));
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
			for(Row row : getConnection().execute(String.format("SELECT * FROM %s WHERE tld='%s';", NAME_TABLE_DOMAIN, tld)).all())
			{
				String domain = row.getString("domain");
				String rootCAKey = row.getString("caKey");
				DefaultCertObject rootCA = getCertificate(rootCAKey);
					
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
		String domain = website.getHost();
		if(existsDomain(domain)) // TODO: perhaps use "IF NOT EXISTS" and batch processing?
		{
			throw new DuplicateItemException();
		}
		else
		{
			DefaultCertObject rootCA = certificateChain.get(certificateChain.size() - 1);
			String rootCAKey = String.valueOf(rootCA.hashCode());
			if(!existsCA(rootCAKey)) // TODO: perhaps use "IF NOT EXISTS" and batch processing?
			{
				BoundStatement boundStatement = new BoundStatement(insertCAStatement);
				getConnection().execute(boundStatement.bind(
						rootCAKey,
						rootCA.getCommonName(),
						rootCA.getOrganizationName(),
						rootCA.getOrganizationUnit(),
						rootCA.getPubKeyAlg(),
						rootCA.getVersion()
				));
			}
			
			String tld = StringUtils.substringAfterLast(domain, ".");
			BoundStatement boundStatement = new BoundStatement(insertDomainStatement);
			getConnection().execute(boundStatement.bind(
					domain,
					tld,
					rootCAKey
			));
			return true;
		}
	}
	
	//----------------------------------------------------------------
	// QUERIES AND STATEMENTS, WRAPPED IN METHODS
	
	private boolean existsDomain(String domain)
	{
		BoundStatement boundStatement = new BoundStatement(existsDomainStatement);
		return !getConnection().execute(boundStatement.bind(domain)).isExhausted();
	}

	private boolean existsCA(String rootCAKey)
	{
		BoundStatement boundStatement = new BoundStatement(getCertificateStatement);
		return !getConnection().execute(boundStatement.bind(rootCAKey)).isExhausted();
	}
	
	private DefaultCertObject getCertificate(String rootCAKey)
	{
		BoundStatement boundStatement = new BoundStatement(getCertificateStatement);
		ResultSet query = getConnection().execute(boundStatement.bind(rootCAKey));
		if(query.isExhausted())
		{
			throw new IllegalStateException("No row (CA) found for key: " + rootCAKey);
		}
		else
		{
			DefaultCertObject result = new DefaultCertObject();
			Row row = query.one();
			result.setCommonName(row.getString("commonName"));
			result.setOrganizationName(row.getString("organizationName"));
			result.setOrganizationUnit(row.getString("organizationUnit"));
			result.setPubKeyAlg(row.getString("pubKeyAlg"));
			result.setVersion(row.getInt("version"));
			return result;
		}
	}

	//----------------------------------------------------------------
	// INHERITED MASSIVE MANIPULATION ROUTINES

	@Override
	public void clearDatabase()
	{
		// debug:
		// getConnection().execute(String.format("DROP KEYSPACE %s;", NAME_KEYSPACE));
		
		// production:
		getConnection().execute(String.format("TRUNCATE %s;", NAME_TABLE_DOMAIN));
		getConnection().execute(String.format("TRUNCATE %s;", NAME_TABLE_CA));
	}
}