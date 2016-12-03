package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import lruproxy.CacheList;
import lruproxy.CacheRequest;
import lruproxy.CacheToFile;
import lruproxy.Proxy;

/*
 * TestProxy
 * @author: Mikael Soto
 * 
 * Test program for non-functional testing. Measures the amount of time
 * taken for each replacement strategy with the same cache size and
 * time interval between reads.
 */
public class TestProxy {
	public static final int cacheSize = 3, interval = 0;
	public static final String directory = "data" + File.separator, 
			outFilename = "nonfunc_output.txt";

	private long startTime, endTime;
	private static BufferedWriter out;
	
	/* Create file and writer to output test times for every replacement method. */
	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		out = new BufferedWriter(new FileWriter( directory + outFilename, true ));
		out.write( getCurrentTime() + " non-functional test run: " + System.getProperty("line.separator") );
	}

	/* Close the file writer after all testing has finished. */
	@AfterClass
	public static void tearDownAfterClass() {
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/* Clear the cache before each replacement method test. */
	@Before
	public void clearCache() {
		CacheRequest cacheRequest = new CacheRequest(directory);
		CacheToFile cacheToFile = new CacheToFile(directory);
		
		String in;
		while( !(in=cacheRequest.read()).equals("") ) {
			cacheToFile.remove(in);
		}
	}

	/* LRU non-functional test. */
	@Test
	public void testLRU() {
		String[] args = { directory, String.valueOf(cacheSize), String.valueOf(interval), String.valueOf(CacheList.LRU) };
		startTime = System.currentTimeMillis();
		Proxy.main(args);
		endTime = System.currentTimeMillis();
		recordTime( endTime - startTime, "LRU" );
	}
	
	/* MRU non-functional test. */
	@Test
	public void testMRU() {
		String[] args = { directory, String.valueOf(cacheSize), String.valueOf(interval), String.valueOf(CacheList.MRU) };
		startTime = System.currentTimeMillis();
		Proxy.main(args);
		endTime = System.currentTimeMillis();
		recordTime( endTime - startTime, "MRU" );
	}
	
	/* LFU non-functional test. */
	@Test
	public void testLFU() {
		String[] args = { directory, String.valueOf(cacheSize), String.valueOf(interval), String.valueOf(CacheList.LFU) };
		startTime = System.currentTimeMillis();
		Proxy.main(args);
		endTime = System.currentTimeMillis();
		recordTime( endTime - startTime, "LFU" );
	}
	
	/* RR non-functional test. */
	@Test
	public void testRR() {
		String[] args = { directory, String.valueOf(cacheSize), String.valueOf(interval), String.valueOf(CacheList.RANDOM) };
		startTime = System.currentTimeMillis();
		Proxy.main(args);
		endTime = System.currentTimeMillis();
		recordTime (endTime - startTime, "RR" );
	}
	
	/* Records the results of a non-functional test into the output file. */
	public void recordTime(long ms, String method) {
		try {
			out.write( method + "\t" + ms + " ms" + System.getProperty("line.separator") );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* Returns a String of the current date and time. */
	public static String getCurrentTime() {
		SimpleDateFormat format = new SimpleDateFormat("EEE MMMM dd HH:mm:ss yyyy");
		Calendar cal = Calendar.getInstance();
		return format.format(cal.getTime());
	}
}
