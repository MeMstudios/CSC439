package lruproxy;

import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * CacheList
 * @author Ken Cooney
 * @date 06/09/2011
 * 
 * The most recently requested object is the first entry in
 * the linked list.  MaxSize is variable (assigned in the 
 * constructor)
 * 
 * 
 * Cost associated with scenarios:
 * 1) adding a new cache entry, cached objects < maxSize
 *    cost: 3 (check for cached file, add to start of list, cache file)
 *    
 * 2) adding a new cache entry, cached objects = maxSize
 *    cost: 4 (check for cached file, remove last object in linked list, add to start of list, remove cached file)
 *
 * 3) finding entry in cache
 *    average cost: 2+ceiling(maxSize/2) (check for cached file, remove object in linked list, add to start of list)
 *    worst case cost: 2+maxSize (check for cached file, remove object in linked list, add to start of list)
 *    
 * TESTED via CachListTestSuite.  All tests pass.
 * 
 */
public class CacheList 
{
	
	private CacheLog log; // not used yet
	private LinkedList<String> linkedList;
	private Map<String, Integer> frequency = new HashMap<String, Integer>();
	private int maxSize;

	/**
	 * Constructor.  The minimum cache size is 1.
	 * @param directory - cache log directory for logging 
	 *                    objects removed from cache
	 * @param maxsize - maximum number of objects to cache
	 */
	public CacheList(String directory, int maxsize)
	{
		log = new CacheLog(directory);
		linkedList=new LinkedList<>();
		if (maxsize<1)
		{
			this.maxSize=1;
		}
		else
		{
			this.maxSize=maxsize;
		}
	}
	
	/**
	 * addNewObjectLFU
	 * This puts the object in the front of the queue.
	 * It removes any repeated object and trims the
	 * list if the length exceeds maxSize using LFU
	 * cache replacement.
	 *
	 * @param URL - URL that was just requested
	 * @param hit - true if it was already cached.
	 * @return - object removed, if any.  We'll need
	 *           to remove this from the hash.
	 */
	public String addNewObjectLFU(String URL, boolean hit)
	{
		String removedURL="";

		if (hit)
		{
			linkedList.remove(URL);
		}

		// If size is MAXSIZE, remove last link
		if (linkedList.size()==maxSize)
		{
			removedURL = getLeastFrequent();
			log.logRemoval(removedURL);
			linkedList.removeFirstOccurrence(removedURL);
		}

		// Newest is always the first.
		linkedList.addFirst(URL);
		frequency.put(URL, 0);

		return removedURL;
	}

	/**
	 * Loops through frequency hashtable and
	 * finds the URL with the smallest hit count
	 *
	 * @return - URL of cache element with smallest hit count.
	 *
	 */
	public String getLeastFrequent(){
		Entry<String, Integer> min = null;
		for (Entry<String, Integer> entry : frequency.entrySet()) {
			if (min == null || min.getValue() > entry.getValue()) {
				min = entry;
			}
		}
		return min.getKey();
	}
	
	/**
	 * addNewObjectMRU
	 * Implements an MRU replacement strategy
	 * This puts the object in the front of he queue.
	 * It removes any repeated object and trims the
	 * list if the length exceeds maxSize.
	 *
	 * @param URL - URL that was just requested.
	 * @param hit - true if it was already cached.
	 * @return - object removed, if any. We'll need to remove this from the hash  .
	 */
	public String addNewObjectMRU(String URL, boolean hit)
	{
		String removedURL="";

		if (hit)
		{
			linkedList.remove(URL);
		}


		// If size is MAXSIZE, remove last link
		if (linkedList.size()==maxSize)
		{
			removedURL=linkedList.getFirst();
			log.logRemoval(removedURL);
			linkedList.removeFirst();
		}

		// Newest is always the first.
		linkedList.addFirst(URL);

		//System.out.println("Added "+URL);

		//traverseTest();

		return removedURL;
	}

	/**
	 *
	 * addNewObject
	 * This puts the object in the front of he queue.
	 * It removes any repeated object and trims the
	 * list if the length exceeds maxSize.
	 * 
	 * @param URL - URL that was just requested
	 * @param hit - true if it was already cached.
	 * @return - object removed, if any.  We'll need
	 *           to remove this from the hash.
	 */
	public String addNewObject(String URL, boolean hit)
	{
		String removedURL="";
		
		if (hit)
		{
			linkedList.remove(URL);
		}
		
		
		// If size is MAXSIZE, remove last link
		if (linkedList.size()==maxSize)
		{
			removedURL=linkedList.getLast();
			log.logRemoval(removedURL);
			linkedList.removeLast();
		}

		// Newest is always the first.
		linkedList.addFirst(URL);
		
		//System.out.println("Added "+URL);
		
		//traverseTest();
		
		return removedURL;
	}
	
	/**
	 * getCacheSize
	 * Used by CacheListSizeThreeTests
	 * @return the number of objects cached
	 */
	public int getCacheSize()
	{
		return linkedList.size();
	}
	
	/**
	 * getHead
	 * Used by CacheListSizeThreeTests
	 * @return URL at this location or empty string if 
	 *         linkedlist is empty.
	 */
	public String getHead()
	{
		String returnedURL="";
		if (linkedList.size()>0)
		{
			returnedURL=linkedList.getFirst();
		}
		return returnedURL;
	}

	/**
	 * get
	 * Used by CacheListSizeThreeTests
	 * @param i - index into the linklist.
	 * @return URL at this location or empty string if 
	 *         param exceeds the size of linked list
	 */
	public String get(int i)
	{
		String returnedURL="";
		if (i<linkedList.size())
		{
			returnedURL=linkedList.get(i);
		}
		return returnedURL;
	}
	
	/**
	 * getHeadLFU
	 * Used by CacheListSizeThreeTests
	 * Increments frequency hit count for URL cache entry
	 * @return URL at this location or empty string if
	 *         linkedlist is empty.
	 */
	public String getHeadLFU()
	{
		int hitcount;
		String returnedURL="";
		if (linkedList.size()>0)
		{
			returnedURL=linkedList.getFirst().toString();
			//frequency values cannot exceed maxSize
			if(frequency.get(returnedURL) < maxSize){
				frequency.put(returnedURL, frequency.get(returnedURL) + 1);
			}

		}
		return returnedURL;
	}
	
	/**
	 * getLFU
	 * Increments frequency hit count for URL cache entry
	 * @param i - index into the linklist.
	 * @return URL at this location or empty string if
	 *         param exceeds the size of linked list
	 */
	public String getLFU(int i)
	{
		int hitcount;
		String returnedURL="";
		if (i<linkedList.size())
		{
			returnedURL=linkedList.get(i).toString();
			//frequency values cannot exceed maxSize
			if(frequency.get(returnedURL) < maxSize){
				frequency.put(returnedURL, frequency.get(returnedURL) + 1);
			}
		}
		return returnedURL;
	}
	
	/**
	 * traverseList
	 * For testing purposes only.  This displays the 
	 * linklist of URLs.
	 */
	public void traverseTest()
	{
		for (int i=0; i<linkedList.size();i++)
		{
			System.out.print(linkedList.get(i)+" => ");
		}
		System.out.println("NULL");
	}
	
}
