package lruproxy;

import java.io.File;


/**
 * Proxy
 * @author Ken Cooney
 * @date 06/11/2011
 *
 * This is the class that can be executed.
 */
public class Proxy 
{	
	private CacheLog cacheLog;
	private CacheRequest cacheRequest;
	private CacheList cacheList;
	private MiniHttp miniHttp;
	private CacheToFile cacheToFile;
	
	private String directory;
	private boolean windows;
	private int sleepSeconds;
	private int method;
	
	public Proxy(String inDirectory, int maxCacheSize, int sleepSeconds, int method)
	{
		//add method assignment

		this.method = method;

		if (sleepSeconds<0)
		{
			sleepSeconds=0;
		}
		this.sleepSeconds=sleepSeconds;
		
		if (maxCacheSize<1)
		{
			maxCacheSize=1;
		}
		
		// Determine which way slashes go for directories.
		String os = System.getProperty("os.name").toLowerCase();
		windows=(os.indexOf( "win" ) >= 0); 

		if ( inDirectory != null ){
			this.directory=inDirectory;
		}

		if (windows && ! directory.endsWith("\\"))
		{
			directory=directory+"\\";
		}
		if (! windows && ! directory.endsWith("/"))
		{
			directory=directory+"/";
		}
		
		if (isValidDirectory() && isInputFilePresent())
		{
			try
			{
			
				cacheLog = new CacheLog(directory);
				cacheRequest= new CacheRequest(directory);
				cacheList = new CacheList(directory, maxCacheSize, method);
				cacheToFile = new CacheToFile(directory);
				miniHttp=new MiniHttp();
			
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			if (! isValidDirectory())
			{
				System.out.println("ERROR: "+directory+" is not a valid directory.");
			}
			else
			{
				System.out.println("ERROR: input.txt file not found in specified directory.");
			}
		}
	}
	
	public void run()
	{
		// A normal proxy will remain running
		// and waiting for requests
		// However we are simulated a proxy that
		// reads a set number of requests and the
		// stops.  So, we'll loop through the
		// file and stop when we reach the end.
		String url;
		do
		{
			// Step 1: read request from file.
			url=cacheRequest.read();
			
			// If we have one, proceed.
			if (url.trim().length()>0)
			{
				// Step 2: Check to see if URL is cached
				//         Log this in the cache log.
				boolean hit=cacheToFile.isCached(url);
				if (hit)
				{
					cacheLog.logHit(url);
				}
				else
				{
					cacheLog.logMiss(url);
				}

				// Step 3: Based on hit/miss, add to LRU
				// cache list.  This logs a message if 
				// an old cached object is deleted 				
				String removedURL=cacheList.addNewObject(url, hit);
				if (removedURL.trim().length()>0)
				{
					//webCache.removeCache(removedURL);
					// physically removed the cached file
					cacheToFile.remove(removedURL);
				}

				// Step 4: If hit, send data to output
				//         If miss, pull data and save it
				if (hit)
				{
					// display cached file to System.out
					cacheToFile.read(url);
				}
				else
				{
					StringBuffer data=miniHttp.fetch(url);
					cacheToFile.write(url, data);
				}
				
				// wait a second before next read
				// I just use this as a timer mechanism
				try
				{
					Thread.sleep(sleepSeconds * 1000);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		} while (url.trim().length()>0);

	}
	
	private boolean isValidDirectory()
	{
		boolean returnValue=false;
		File file = new File(directory);
		if (file.exists() && file.isDirectory())
		{
			returnValue=true;
		}
		return returnValue;
	}
	
	private boolean isInputFilePresent()
	{
		boolean returnValue=false;
		File inFile = new File(directory+"input.txt");
		if (inFile.exists())
		{
			returnValue=true;
		}
		return returnValue;
	}

	/**
	 *
	 * @param args
	 * This main method takes four arguments
	 * The first argument is a string specifying the directory where input.txt is located
	 * The second argument is an integer specifying the max cache size
	 * The third argument is an integer specifying the number of seconds to sleep between requests
	 * The fourth argument is an integer specifying which replacement strategy to use
	 * 0 - LRU default
	 * 1 - MRU
	 * 2 - LFU
	 * 3- RR
	 */
	public static void main(String args[])
	{
		if (args.length==4)
		{
			try
			{
				String directory=args[0];
				String temp=args[1];
				int maxCacheSize=Integer.parseInt(temp);
				temp=args[2];
				int sleepSeconds=Integer.parseInt(temp);
				temp=args[3];
				int method = Integer.parseInt(temp);
				Proxy proxy=new Proxy(directory, maxCacheSize, sleepSeconds, method);
				proxy.run();
			}
			catch (Exception e)
			{
				System.out.println("ERROR: could not process parameters");
			}
		}
		else
		{
			System.out.println("Pass in the following parameters:");
			System.out.println("directory where input.txt resides");
			System.out.println("maximum number of cached web pages (integer, minimum is 1)");
			System.out.println("number of seconds to sleep between checking for requests (integer, minimum is 0)");
			System.out.println("The method of replacement to use as an integer -- 0 for LRU, 1 MRU, 2 LFU, 3 RR");
		}
	}
}

