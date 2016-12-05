package test;

import java.io.*;
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
 * @author: Ethan Gallagher ( modified 12/4 )
 *
 * Test program for non-functional testing. Measures the amount of time
 * taken for each replacement strategy with the same cache size and
 * time interval between reads.
 *
 * These test methods do not register as "passed" in JUnit. This issue appears to be related to
 * the calls made to Proxy.main() . However, this is irrelevant for this test class as all the code
 * for the non-functional tests is executed, with the results stored in /data/nonfunc_output.txt 
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
		clearLog();
		String[] args = { directory, String.valueOf(cacheSize), String.valueOf(interval), String.valueOf(CacheList.LRU) };
		startTime = System.currentTimeMillis();
		Proxy.main(args);
		endTime = System.currentTimeMillis();
		recordTime( endTime - startTime, "LRU", HMRatio() );

	}
	
	/* MRU non-functional test. */
	@Test
	public void testMRU() {
		clearLog();
		String[] args = { directory, String.valueOf(cacheSize), String.valueOf(interval), String.valueOf(CacheList.MRU) };
		startTime = System.currentTimeMillis();
		Proxy.main(args);
		endTime = System.currentTimeMillis();
		recordTime( endTime - startTime, "MRU", HMRatio() );
	}
	
	/* LFU non-functional test. */
	@Test
	public void testLFU() {
		clearLog();
		String[] args = { directory, String.valueOf(cacheSize), String.valueOf(interval), String.valueOf(CacheList.LFU) };
		startTime = System.currentTimeMillis();
		Proxy.main(args);
		endTime = System.currentTimeMillis();
		recordTime( endTime - startTime, "LFU", HMRatio() );
	}
	
	/* RR non-functional test. */
	@Test
	public void testRR() {
		clearLog();
		String[] args = { directory, String.valueOf(cacheSize), String.valueOf(interval), String.valueOf(CacheList.RANDOM) };
		startTime = System.currentTimeMillis();
		Proxy.main(args);
		endTime = System.currentTimeMillis();
		recordTime (endTime - startTime, "RR", HMRatio() );
	}
	
	/* Records the results of a non-functional test into the output file. */
	public void recordTime(long ms, String method, double ratio ) {
		try {
			out.write( method + "\t" + ms + " ms" + "\t" + " hit/miss ratio: "
					+ ratio + System.getProperty("line.separator") );
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

	/**
	 * Truncates log file in /data
	 * @throws IOException
	 */
	public void clearLog(){
		try{
		BufferedWriter out = new BufferedWriter( new FileWriter( directory + "output.log", false));//truncate
		out.close();}
		catch( IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * Counts hits and misses by parsing log file. Returns a double value
	 * @return
	 * @throws IOException
	 */
	public double HMRatio(){
		double hits = 0.0;
		double misses = 0.0;

		try {
			BufferedReader in = new BufferedReader(new FileReader(directory + "output.log"));

			String line;
			while ((line = in.readLine()) != null) {
				if (line.substring(line.lastIndexOf(" ") + 1).equals("hit")) {
					hits += 1.0;
				} else if (line.substring(line.lastIndexOf(" ") + 1).equals("miss")) {
					misses += 1.0;
				}
			}


		}
		catch (IOException e ){
			e.printStackTrace();
		}

		return hits / misses;
	}
}
