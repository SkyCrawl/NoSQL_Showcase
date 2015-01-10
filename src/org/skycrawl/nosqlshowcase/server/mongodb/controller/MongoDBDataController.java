package org.skycrawl.nosqlshowcase.server.mongodb.controller;

import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.skycrawl.nosqlshowcase.server.root.common.db.AbstractDataController;
import org.skycrawl.nosqlshowcase.server.root.common.model.WebsiteToCertDataModel;
import org.skycrawl.nosqlshowcase.server.root.common.sample.DefaultCertObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.MongoException;

public class MongoDBDataController extends AbstractDataController<DB>
{
	private static final String COLLECTION_CERTCHAINS = "certs";
	private static final String COLLECTION_DOMAINS = "domains";
	
	public MongoDBDataController(DB connection)
	{
		super(connection);
	}
	
	@Override
	public void init() throws Exception
	{
		getConnection().getCollection(COLLECTION_DOMAINS).createIndex(new BasicDBObject("tld", 1));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Set<String> getTLDs() throws Exception
	{
		List result = getConnection().getCollection(COLLECTION_DOMAINS).distinct("tld");
		return new LinkedHashSet<String>(result);
	}

	@Override
	public WebsiteToCertDataModel getSetDomainAndIntersections(Set<String> tlds) throws Exception
	{
		DBCollection collCerts = getConnection().getCollection(COLLECTION_CERTCHAINS);
		WebsiteToCertDataModel result = new WebsiteToCertDataModel();
		for(String tld : tlds)
		{
			// register the current TLD
			result.registerSet(tld);
			
			// and process domains individually
			DBCursor domainCursor = getConnection().getCollection(COLLECTION_DOMAINS).find(new BasicDBObject("tld", tld));
			while(domainCursor.hasNext())
			{
				DBObject currentDomain = domainCursor.next();
				DBObject certChain = collCerts.findOne(new BasicDBObject("_id", ((DBRef) currentDomain.get("certs")).getId()));
				if(certChain != null)
				{
					DefaultCertObject rootCA = fromBSON(certChain);
					
					// register the certificate
					result.registerSet(rootCA);
					
					// and register the current found overlap
					result.registerOverlap((String) currentDomain.get("domain"), tld, rootCA);
				}
				else
				{
					throw new MongoException(String.format("Broken certificate chain reference at domain '%s'.", (String) currentDomain.get("domain")));
				}
			}
		}
		return result;
	}
	
	@Override
	public boolean store(URL website, List<DefaultCertObject> certificateChain) throws Exception
	{
		String domain = website.getHost();
		
		/*
		 * Root authorities should be first so that we don't have to
		 * use more complex code to dig it out later from BSON.
		 */
		Collections.reverse(certificateChain);
		
		// store the certificate chain as a recursive object (using embedded documents)
		BasicDBObject master = null, current = null;
		for(DefaultCertObject cert : certificateChain)
		{
			if(master == null)
			{
				master = toBSON(cert);
				current = master;
			}
			else
			{
				BasicDBObject next = toBSON(cert);
				current.append("signs", next);
				current = next;
			}
		}
		getConnection().getCollection(COLLECTION_CERTCHAINS).insert(master);
		
		// store domain information into a collection dedicated to TLD, with a reference to the above stored certificate chain
		BasicDBObject domainInfo = new BasicDBObject(3);
		domainInfo.append("domain", domain);
		domainInfo.append("tld", StringUtils.substringAfterLast(domain, "."));
		domainInfo.append("certs", new DBRef(getConnection(), COLLECTION_CERTCHAINS, master.get("_id")));
		getConnection().getCollection(COLLECTION_DOMAINS).insert(domainInfo);
		
		// and return
		return true;
	}

	//----------------------------------------------------------------
	// INHERITED MASSIVE MANIPULATION ROUTINES
	
	@Override
	public void clearDatabase()
	{
		// don't drop database (could invalidate our {@link DB} instance...)
		getConnection().getCollection(COLLECTION_CERTCHAINS).drop();
		getConnection().getCollection(COLLECTION_DOMAINS).drop();
	}
	
	//----------------------------------------------------------------
	// PRIVATE INTERFACE
	
	private static BasicDBObject toBSON(DefaultCertObject cert)
	{
		BasicDBObject result = new BasicDBObject(5);
		result.append("cn", cert.getCommonName());
		result.append("on", cert.getOrganizationName());
		result.append("ou", cert.getOrganizationUnit());
		result.append("pka", cert.getPubKeyAlg());
		result.append("version", cert.getVersion());
		return result;
	}
	
	private static DefaultCertObject fromBSON(DBObject object)
	{
		DefaultCertObject result = new DefaultCertObject();
		result.setCommonName((String) object.get("cn"));
		result.setOrganizationName((String) object.get("on"));
		result.setOrganizationUnit((String) object.get("ou"));
		result.setPubKeyAlg((String) object.get("pka"));
		result.setVersion((Integer) object.get("version"));
		return result;
	}
}