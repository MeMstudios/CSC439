package test;

import static org.junit.Assert.*;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import lruproxy.CacheList;

/* Unit test for the CacheList LFU method. Created by Mikael Soto. */
@RunWith(value=Parameterized.class)
public class TestLFU {

	private static CacheList cacheList;
	private static int maxCacheSize, cacheSize;
	private static String directory;
	
	private String url, removedUrl, lfuUrl;
	private boolean hit;
	
	// Test URLs.
	private static final String WEBSITE1="www.website1.com", WEBSITE2="www.website2.org", 
			WEBSITE3="www.website3.net", WEBSITE4="www.website4.edu";
	
	public TestLFU(String url, String hit, String lfuUrl, String removedUrl, String cacheSize) {
		this.url = url;
		this.hit = Boolean.parseBoolean(hit);
		this.lfuUrl = lfuUrl;
		this.removedUrl = removedUrl;
		this.cacheSize = Integer.parseInt(cacheSize);
	}
	
	/* Parameters for testing the CacheList LFU method.
	 * The first parameter is the URL to be run by the cache.
	 * The second parameter is whether the cache should have a hit for the URL. 
	 * The third parameter is which URL should then be the LFU at that time.
	 * The fourth parameter is which URL, if any, was removed from the cache.
	 * The fifth parameter is the current size of the cache. */
	@Parameterized.Parameters
    public static Collection<String[]> testParams() {
    	 return Arrays.asList(new String[][] {
    	 //  { URL, hit, LFU URL, removed URL, cache size }
    		 { WEBSITE1, "false", WEBSITE1, "", "1" },			/* Cache: { W1, empty, empty } */
    		 { WEBSITE2, "false", "", 		"", "2" },			/* Cache: { W2, W1, empty } */
    		 { WEBSITE3, "false", "",		 "", "3" },			/* Cache: { W3, W2, W1 } */
    		 { WEBSITE2, "true",  "",		 "", "3" },			/* Cache: { W2(2), W3, W1 } */
    		 { WEBSITE3, "true",  WEBSITE1, "", "3" },			/* Cache: { W3(2), W2(2), W1 } */
    		 { WEBSITE4, "false", WEBSITE4, WEBSITE1, "3" },	/* Cache: { W4, W3(2), W2(2) } */
    		 { WEBSITE2, "true",  WEBSITE4, "", "3" },			/* Cache: { W2(3), W4, W3(2) } */
    		 { WEBSITE1, "false", WEBSITE1, WEBSITE4, "3" },	/* Cache: { W2(3), W1, W3(2) } */
    		 { WEBSITE1, "true",  "",		 "", "3" }, 		/* Cache: { W1(2), W2(3), W3(2) } */
    		 { WEBSITE1, "true",  WEBSITE3, "", "3" },			/* Cache: { W1(3), W2(3), W3(2) } */
    	 });
    }
	
    /* Set up the tests by initializing the cacheList with the given directory and cache size.
     * Also checks the getLeastFrequent() method on the empty cache. */
	@BeforeClass
	public static void setUpClass() {
		directory = "src" + File.separator + "test" + File.separator + "testdir";
		maxCacheSize = 3;
		cacheList = new CacheList(directory, maxCacheSize, CacheList.LFU);
		
		System.out.println("Running TestLFU JUnit functional test");
		// Check immediately after creating the cacheList that the LFU URL is null:
		assertNull("getLeastFrequent() failed on empty cache", cacheList.getLeastFrequent());
	}

	@Test
	public void testLFU() {
		// Check if the proper URL is removed from the cache when adding the parameter URL.
		assertEquals("addNewObjectLFU(url, hit) failed", removedUrl, cacheList.addNewObjectLFU(url, hit));
		// Update the frequency count.
		assertEquals("getHeadLFU() failed", url, cacheList.getHeadLFU());
		
		// Check that the LFU URL is the proper one.
		if(!lfuUrl.equals(""))
			assertEquals("getLeastFrequent() failed", lfuUrl, cacheList.getLeastFrequent());
		
		// Check that the cacheSize is the proper size.
		assertEquals("getCacheSize()", cacheSize, cacheList.getCacheSize());
		
		// Check that the traverseTest() operates properly.
		try {
			cacheList.traverseTest();
		} catch(Exception ex) {
			System.out.println("Exception in traverseTest().");
			ex.printStackTrace();
		}
	}
}
