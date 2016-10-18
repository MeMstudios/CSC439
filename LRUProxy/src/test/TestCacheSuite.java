import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith( value=org.junit.runners.Suite.class )

/* All test classes to be included in the suite are listed here. Add
 * other test classes as necessary. */
@SuiteClasses( value={
		TestCacheList.class,
		TestCacheLog.class,
		TestCacheRequest.class,
		TestCacheToFile.class
	})

public class TestCacheSuite { 
	public TestSuite() {
		System.out.println("Running test suite.");
	}
}
