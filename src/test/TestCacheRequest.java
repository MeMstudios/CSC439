package test;

import org.junit.*;
import static org.junit.Assert.*;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runner.RunWith;
import lruproxy.CacheRequest;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

/* Tests the CacheRequest class by checking that it properly reads in input. */
@RunWith(value = Parameterized.class)
public class TestCacheRequest {
	
	private String expected, value;
	private static String directory;
	private static CacheRequest cacheRequest;
	
	// The expected first three values from the input.txt file to check for.
	@Parameters
	 public static Collection<String[]> data() {
	   String[][] data = new String[][] { {"www.google.com"}, {"www.yahoo.com"},
		   {"www.wikipedia.com"} };
		   
	   return Arrays.asList(data);
	 }
		
	 public TestCacheRequest(String expected) { 
			this.expected = expected;
			this.value = cacheRequest.read();
	 }
		
	 /* Initialize the class by create a CacheRequest object with a directory. Requires the user to
	  * input the directory of the file, to accommodate different operating systems. */
	 @BeforeClass
	 public static void getDirectory() {
		 Scanner scanner = new Scanner(System.in);


		 /*  String message = "Enter directory of input.txt file: ";
		 
		 do {
			 System.out.print(message);
			 directory = scanner.nextLine();
			 message = "Invalid directory. Please enter an actual directory: ";
		 } while(!(new File(directory)).exists());
		 */

		 directory = "data" + File.separator;
		 
		 cacheRequest = new CacheRequest(directory);
	 }
	
	 // Check that the input read in equals the assumed value.
	 @Test
	 public void TestRead() {
	     assertEquals("Read value not equal to expected value;", expected, value);
	 }
}
