package org.skycrawl.nosqlshowcase.server.riak.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.skycrawl.nosqlshowcase.server.Logger;
import org.skycrawl.nosqlshowcase.server.riak.model.RiakX509Cert;

public class RiakSampleLoader
{
	// main variables
	private final RiakDataController dataController;
	private final RiakSampleLoadResult loadResult;
	
	public RiakSampleLoader(RiakDataController dataController)
	{
		this.dataController = dataController;
		this.loadResult = new RiakSampleLoadResult();
	}
	
	//--------------------------------------------------------------
	// PUBLIC INTERFACE

	public void load(String url)
	{
		// open connection to the given URL and check for some basic problems
		HttpsURLConnection con = null;
		try
		{
			con = (HttpsURLConnection) new URL(url).openConnection();
			if (con.getResponseCode() != 200)
			{
				loadResult.getUrlsWithInvalidResponse().add(url);
				return;
			}
			
			// TODO: check against IP addresses   
		}
		catch (MalformedURLException e)
		{
			loadResult.getMalformedURLs().add(url);
			return;
		}
		catch (IOException e)
		{
			Logger.logThrowable("Not supposed to happen: ", e);
			loadResult.getIgnoredURLs().add(url);
			return;
		}

		// process the certificate chain
		try
		{
			// get it
			X509Certificate[] certs = (X509Certificate[]) con.getServerCertificates(); // SSL certificates are always x509 certificates
			
			// convert it
			List<RiakX509Cert> db_certs = new ArrayList<RiakX509Cert>(); 
			for (X509Certificate cert : certs)
			{
				db_certs.add(toDBObject(cert));
			}
			
			// and store it
			if(!dataController.store(con.getURL(), db_certs))
			{
				loadResult.getFailedToSaveURLs().add(url);
			}
		}
		catch (Exception e)
		{
			Logger.logThrowable("Not supposed to happen: ", e);
			loadResult.getIgnoredURLs().add(url);
			return;
		}
	}
	
	public RiakSampleLoadResult getLoadResult()
	{
		return loadResult;
	}
	
	//--------------------------------------------------------------
	// PRIVATE INTERFACE
	
	private RiakX509Cert toDBObject(X509Certificate cert) throws IOException
	{
		RiakX509Cert result = new RiakX509Cert();
		
		// subject - we don't care (= issuer of the previous certificate)
		// System.out.println("Cert subject (RFC1779): " + cert.getSubjectX500Principal().getName("RFC1779"));
		// System.out.println("Cert subject (RFC2253): " + cert.getSubjectX500Principal().getName("RFC2253"));
		// System.out.println("Cert subject (CANONICAL): " + cert.getSubjectX500Principal().getName("CANONICAL"));
		
		// issuer
		// System.out.println("Cert issuer (RFC1779): " + cert.getIssuerX500Principal().getName("RFC1779")); // human-readable
		// System.out.println("Cert issuer (RFC2253): " + cert.getIssuerX500Principal().getName("RFC2253")); // machine-readable CSV
		// System.out.println("Cert issuer (CANONICAL): " + cert.getIssuerX500Principal().getName("CANONICAL")); // RFC2253 and lowercase
		
		// the rest
		// System.out.println("Cert version: " + cert.getVersion());
		// System.out.println("Cert public key algorithm: " + cert.getPublicKey().getAlgorithm());
		// System.out.println("Cert hash: " + cert.hashCode());
		// System.out.println();
		
		result.setVersion(cert.getVersion());
		result.setPubKeyAlg(cert.getPublicKey().getAlgorithm());
		fillIssuerInfo(result, cert.getIssuerX500Principal().getName("RFC2253"));
		
		return result;
	}
	
	/**
	 * @param to
	 * @param from issuer's x500 principal name, in RFC2253
	 * @throws IOException
	 */
	private void fillIssuerInfo(RiakX509Cert to, String from) throws IOException
	{
		CSVParser parser = CSVParser.parse(from, CSVFormat.RFC4180);
		for (CSVRecord csvRecord : parser) // just 1 in this case anyway
		{
			for(String column : csvRecord)
			{
				if(column.startsWith("O="))
				{
					to.setOrganizationName(column.substring(2));
				}
				else if(column.startsWith("OU="))
				{
					to.setOrganizationUnit(column.substring(3));
				}
				else if(column.startsWith("CN="))
				{
					to.setCommonName(column.substring(3));
				}
			}
		}
	}
}