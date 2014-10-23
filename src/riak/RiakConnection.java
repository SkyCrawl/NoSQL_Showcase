package riak;

import java.util.HashSet;
import java.util.Set;

import com.basho.riak.client.IRiakClient;
import com.basho.riak.client.IRiakObject;
import com.basho.riak.client.RiakException;
import com.basho.riak.client.RiakFactory;
import com.basho.riak.client.RiakRetryFailedException;
import com.basho.riak.client.bucket.Bucket;
import com.basho.riak.client.cap.UnresolvedConflictException;
import com.basho.riak.client.convert.ConversionException;
import com.example.nosql_homework.MyDBConnection;

public class RiakConnection extends MyDBConnection
{
	private IRiakClient riakClient;
	
	public RiakConnection()
	{
		this.riakClient = null;
	}
	
	@Override
	protected void connectPrivate(String hostname, int port) throws Exception
	{
		if(riakClient != null)
		{
			destroyCurrentSession();
		}
		
		riakClient = RiakFactory.httpClient(hostname);
	}
	
	@Override
	protected void destroyCurrentSession()
	{
		riakClient.shutdown();
		riakClient = null;
	}
	
	// --------------------------------------------------------------------------------------------
	// MAIN DB OPERATIONS
	
	@SuppressWarnings("deprecation")
	public Set<String> getBuckets() throws RiakException
	{
		try
		{
			return riakClient.listBuckets();
		}
		catch (RiakException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public Set<String> getKeysInBucket(String bucketID)
	{
		try
		{
			Set<String> result = new HashSet<String>();
			for(String s : getBucket(bucketID).keys())
			{
				result.add(s);
			}
			return result;
		}
		catch (RiakRetryFailedException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (RiakException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public IRiakObject getValue(String bucketID, String itemID)
	{
		try
		{
			return getBucket(bucketID).fetch(itemID).execute();
		}
		catch (UnresolvedConflictException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (RiakRetryFailedException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (ConversionException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public Bucket createBucket(String bucketID) throws RiakRetryFailedException
	{
		return riakClient.createBucket(bucketID).execute();
	}
	
	public void add(String bucketID, String key, String value) throws RiakRetryFailedException, UnresolvedConflictException, ConversionException
	{
		getBucket(bucketID).store(key, value).execute();
	}
	
	public IRiakObject fetch(String bucketID, String key) throws UnresolvedConflictException, RiakRetryFailedException, ConversionException
	{
		return getBucket(bucketID).fetch(key).execute();
	}
	
	public void remove(String bucketID, String key) throws RiakException, RiakRetryFailedException
	{
		getBucket(bucketID).delete(key).rw(3).execute();
	}
	
	// --------------------------------------------------------------------------------------------
	// PRIVATE INTERFACE
	
	private Bucket getBucket(String key) throws RiakRetryFailedException
	{
		return riakClient.fetchBucket(key).execute();
	}
}
