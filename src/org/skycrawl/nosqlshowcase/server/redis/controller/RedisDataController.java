package org.skycrawl.nosqlshowcase.server.redis.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.skycrawl.nosqlshowcase.server.root.common.db.AbstractDataController;
import org.skycrawl.nosqlshowcase.server.root.common.exceptions.DuplicateItemException;
import org.skycrawl.nosqlshowcase.server.root.common.model.WebsiteToCertDataModel;
import org.skycrawl.nosqlshowcase.server.root.common.sample.DefaultCertObject;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

public class RedisDataController extends AbstractDataController<JedisPool>
{
	/*
	 * FROM JEDIS'S GITHUB:
	 * 
	 * Sometimes you need to send a bunch of different commands. A very cool way to do that, and have better
	 * performance than doing it the naive way, is to use pipelining. This way you send commands without
	 * waiting for response, and you actually read the responses at the end, which is faster: 
	 * 
	 * Pipeline p = jedis.pipelined();
	 * P.set("fool", "bar");
	 * p.zadd("foo", 1, "barowitch");  p.zadd("foo", 0, "barinsky"); p.zadd("foo", 0, "barikoviev");
	 * Response<String> pipeString = p.get("fool");
	 * Response<Set<String>> sose = p.zrange("foo", 0, -1);
	 * p.sync();
	 * 
	 * int soseSize = sose.get().size();
	 * Set<String> setBack = sose.get();
	 * 
	 * TRANSACTIONS:
	 * 
	 * Transaction t = getConnection().getResource().multi();
	 * t.set("foo", "bar");
	 * Response<String> result1 = t.get("foo"); // only available after the following line: 
	 * t.exec();
	 */
	
	// getConnection().asking();
	// getConnection().auth(null);
	// getConnection().resetState();
	// getConnection().save();
	// getConnection().quit();
	
	// getConnection().dbSize(); // number of keys
	// getConnection().multi(); // transaction
	// getConnection().randomKey();
	
	// getConnection().keys("foo*");
	// getConnection().exists("key");
	// getConnection().get("key");
	// getConnection().set("key", "value");
	// getConnection().sort("key");
	
	private static final String key_tlds = "TLDs"; // maps to a sorted set of TLDs
	
	public RedisDataController(JedisPool connection)
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
		try (Jedis jedis = getConnection().getResource())
		{
			long tldCount = jedis.zcard(key_tlds);
			return jedis.zrange(key_tlds, 0, tldCount);
		}
	}

	@Override
	public WebsiteToCertDataModel getSetDomainAndIntersections(Set<String> tlds) throws Exception
	{
		WebsiteToCertDataModel result = new WebsiteToCertDataModel();
		try (Jedis jedis = getConnection().getResource())
		{
			// and turn them into Venn data
			for(String tld : tlds)
			{
				result.registerSet(tld);
				for(String domain : jedis.smembers(tld))
				{
					String rootCAKey = jedis.get(domain);
					DefaultCertObject rootCA = dbToCert(jedis.hgetAll(rootCAKey));
					
					// register the certificate
					result.registerSet(rootCA);
					
					// and register the current found overlap
					result.registerOverlap(domain, tld, rootCA);
				}
			}
			return result;
		}
	}
	
	@Override
	public boolean store(URL website, List<DefaultCertObject> certificateChain) throws DuplicateItemException, Exception
	{
		String domain = website.getHost();
		String tld = StringUtils.substringAfterLast(domain, ".");
		try (Jedis jedis = getConnection().getResource())
		{
			DefaultCertObject rootCA = certificateChain.get(certificateChain.size() - 1);
			String rootCAKey = String.valueOf(rootCA.hashCode());
			boolean rootCAExists = jedis.exists(rootCAKey);
			
			// first some checks
			if(jedis.sismember(tld, domain))
			{
				throw new DuplicateItemException();
			}
			else
			{
				Transaction t = jedis.multi(); // all or nothing - don't break consistency
				
				// store TLD using a SORTED SET (doesn't overwrite anything)
				t.zadd(key_tlds, 1, tld); // all items have the same score => lexicographical ordering
				
				// store the current domain using a SET
				t.sadd(tld, domain);
				
				// store the root authority using a complex object (HASH)
				if(!rootCAExists)
				{
					t.hmset(rootCAKey, certToDB(rootCA));
				}
				
				// link the domain with the CA
				t.set(domain, rootCAKey);
				
				t.exec();
				return true;
			}
		}
	}

	//----------------------------------------------------------------
	// INHERITED MASSIVE MANIPULATION ROUTINES
	
	@Override
	public void clearDatabase()
	{
		try (Jedis jedis = getConnection().getResource())
		{
			// jedis.flushDB(); // just the currently selected DB
			jedis.flushAll(); // everything
		}
	}
	
	//----------------------------------------------------------------
	// PRIVATE INHERITED
	
	private static Map<String, String> certToDB(DefaultCertObject cert)
	{
		Map<String, String> result = new HashMap<String, String>();
		result.put("common_name", cert.getCommonName() != null ? cert.getCommonName() : "");
		result.put("organization_name", cert.getOrganizationName() != null ? cert.getOrganizationName() : "");
		result.put("organization_unit", cert.getOrganizationUnit() != null ? cert.getOrganizationUnit() : "");
		result.put("pub_key_alg", cert.getPubKeyAlg() != null ? cert.getPubKeyAlg() : "");
		result.put("version", String.valueOf(cert.getVersion()));
		return result;
	}
	
	private static DefaultCertObject dbToCert(Map<String, String> dbObject)
	{
		DefaultCertObject result = new DefaultCertObject();
		result.setCommonName(dbObject.get("common_name"));
		result.setOrganizationName(dbObject.get("organization_name"));
		result.setOrganizationUnit(dbObject.get("organization_unit"));
		result.setPubKeyAlg(dbObject.get("pub_key_alg"));
		result.setVersion(Integer.parseInt(dbObject.get("version")));
		return result;
	}
}