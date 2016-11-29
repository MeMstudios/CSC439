package test;

/*
        * Created by ethan on 11/26/16.
*/
        import java.io.File;
        import java.util.Arrays;
        import java.util.Collection;
        import java.util.Random;

        import org.junit.AfterClass;
        import org.junit.Test;
        import static org.junit.Assert.*;
        import org.junit.Before;
        import org.junit.experimental.runners.Enclosed;
        import org.junit.runner.RunWith;
        import org.junit.runners.Parameterized;
        import org.junit.runners.Parameterized.Parameters;
        import lruproxy.CacheList;

/**
 * Modified from TestCacheList
 * Randomness is difficult to test for, so this file tests for divergence in behavior
 * from an LRU replacement strategy. We find this value by running a parameterized test
 * where the CacheList is filled to capacity, then forcing a replacement by adding one more
 * item to the list. We check the removed URL against an expected value for LRU replacement.
 * After running all the parameterized tests we divide the number of times our CacheList acted like a
 * LRU list by the number of times we forced a replacement and subtract this from one.
 * This gives us the percentage of time our CacheList did not act like an LRU list, and if it is greater than zero
 * our test passes.
 */
@RunWith(Enclosed.class)
public class TestRR {

    @RunWith(value = Parameterized.class)
    public static class CacheListParam {

        private static double hits = 0; //number of replacements that behaved like LRU
        private static double repl = 0; //number of replacements
        private int CacheSize;
        private int CurrentSize;
        private String ExpectedVal;
        private CacheList instance;
        private String directory = "src" + File.separator + "test" + File.separator +
                "testdir";
        //added a few websites to get a larger CacheList
        private final String[] Websites = {"www.bleh.com", "www.cnn.com" , "www.google.com", "www.facebook.com", "www.youtube.com",
                "www.nku.edu", "www.amazon.com", "www.someothersite.com"};

        public CacheListParam(String cacheSize, String currentSize, String exp) {
            this.CacheSize = Integer.parseInt(cacheSize);
            this.CurrentSize = Integer.parseInt(currentSize);
            this.ExpectedVal = exp;
        }

        @Parameters
        /**
         * For random replacement we don't want to use smaller sized caches
         * because they will behave like LRU caches more often
         */
        public static Collection<String[]> getTestParameters() {
            String[][] params = { {"5", "5", "www.facebook.com"},
                    {"6", "6", "www.google.com"},
                    {"7", "7", "www.cnn.com"},
                    {"8", "8", "www.bleh.com"},
                    {"8", "8", "www.bleh.com"},
                    {"8", "8", "www.bleh.com"},
                    {"8", "8", "www.bleh.com"},
                    {"8", "8", "www.bleh.com"},
                    {"8", "8", "www.bleh.com"},
                    {"8", "8", "www.bleh.com"},
                    {"8", "8", "www.bleh.com"},
                    {"8", "8", "www.bleh.com"},
                    {"8", "8", "www.bleh.com"},
                    {"8", "8", "www.bleh.com"},
                    {"9", "8", ""}};

            return Arrays.asList(params);
        }

        @Before
        public void setUp() {
            boolean hit = false; // RR does nothing if boolean is true
            this.instance = new CacheList(directory, this.CacheSize, 3);
            for (String Website : this.Websites) {
                instance.addNewObject(Website, hit);
            }
        }

        /**
         * Test of addNewObject method, of class CacheList.
         */
        @Test
        public void testAddNewObject() {
            String URL = "www.google.com";
            boolean hit = false; // hit is always false, if hit is true addNewObject does nothing in RR
            System.out.println("addNewObject: CacheSize = " + this.CacheSize + " | Expected return to be in LRU " + this.ExpectedVal);
            String ret = instance.addNewObject( URL, hit );
            if ( ret.equals(this.ExpectedVal) ){
                //if the return value from adding "www.google.com" is what we would expect from LRU
                hits += 1.0;//increment hits
            }
            if ( !ret.equals("") ) { //if something was replaced in the list
                repl += 1.0;//increment accesses everytime we add a new object
            }
        }

        /**
         * Test of getCacheSize method, of class CacheList.
         */
        @Test
        public void testGetCacheSize() {
            System.out.println("getCacheSize: CacheSize = " + this.CurrentSize + " | Expected return to be " + this.CurrentSize
                    + " and got " + instance.getCacheSize());

            assertEquals(this.CurrentSize, instance.getCacheSize());
        }

        /**
         * Test of getHead method, of class CacheList.
         */
        @Test
        public void testGetHead() {
            System.out.println("getHead: Should always be \"www.someothersite.com\"");

            assertEquals("www.someothersite.com", instance.getHead());
        }

        /**
         * Test for divergence from LRU after all parameterized tests have run
         */
        @AfterClass
        public static void testRandomness(){
            //We would not expect divergence to be zero after so many tests
            double divergence = 1.0 - hits/repl;
            assertNotEquals( 0.0, divergence );
            System.out.println("Divergence from LRU : " + divergence );
        }

    }

    public static class CacheListSingleTests {

        private final int CacheSize = 10;
        private CacheList instance;
        private final String[] Websites = {"www.google.com", "www.facebook.com", "www.youtube.com",
                "www.nku.edu", "www.amazon.com", "www.someothersite.com"};

        @Before
        public void setUp() {
            boolean hit = false; //RR does nothing if hit = true
            this.instance = new CacheList("", this.CacheSize);
            for (String Website : this.Websites) {
                instance.addNewObject(Website, hit);
            }
        }



        /**
         * Test of get method, of class CacheList.
         */
        @Test
        public void testGet() {
            Random rn = new Random();
            String expResult = "";
            int i = rn.nextInt(11);
            if (i < 6) {
                expResult = this.Websites[5 - i];
            }

            System.out.println("get: (" + i + ") I should get \"" + expResult + "\" and got \"" + instance.get(i) + "\"");

            assertEquals(expResult, instance.get(i));
        }

        /**
         * Test of traverseTest method, of class CacheList.
         */
        @Test
        public void testTraverseTest() {
            System.out.println("traverseTest: Just looking for no exceptions to be thrown since this function only prints to the console.");
            instance.traverseTest();
        }
    }

}


