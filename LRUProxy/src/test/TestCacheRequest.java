import org.junit.*;
import static org.junit.Assert.*;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runner.RunWith;
import java.util.Arrays;
import java.util.Collection;

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
		
	 // Initialize the class by create a CacheRequest object with a directory.
	 @BeforeClass
	 public static void getDirectory() {
		 directory = System.getProperty("user.dir") +"\\data\\";
		 cacheRequest = new CacheRequest(directory);
		 System.out.println("Assumed directory of input.txt: "+directory);
	 }
	
	 // Check that the input read in equals the assumed value.
	 @Test
	 public void TestRead() {
	     assertEquals("Read value not equal to expected value;", expected, value);
	 }
}
