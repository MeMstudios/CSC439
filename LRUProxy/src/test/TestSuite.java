import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith( value=org.junit.runners.Suite.class )

/* All test classes to be included in the suite are listed here. Add
 * other test classes as necessary. */
@SuiteClasses( value={
		CacheListTest.class,
		CacheLogTest.class,
		CacheRequestTest.class,
		CacheToFileTest.class
	})

public class TestSuite { 
	public TestSuite() {
		System.out.println("Running test suite.");
	}
}
