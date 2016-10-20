/*
 * @author Nathen Lyman
 * @since Oct 5, 2016
 * 
 */
package lruproxy;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Enclosed.class)
public class CacheListTest {

    @RunWith(value = Parameterized.class)
    public static class CacheListParam {

        private int CacheSize;
        private int CurrentSize;
        private String ExpectedVal;
        private CacheList instance;
        private final String[] Websites = {"www.google.com", "www.facebook.com", "www.youtube.com",
            "www.nku.edu", "www.amazon.com", "www.someothersite.com"};

        public CacheListParam(String cacheSize, String currentSize, String exp) {
            this.CacheSize = Integer.parseInt(cacheSize);
            this.CurrentSize = Integer.parseInt(currentSize);
            this.ExpectedVal = exp;
        }

        @Parameters
        public static Collection<String[]> getTestParameters() {
            String[][] params = {{"0", "1", "www.someothersite.com"}, {"1", "1", "www.someothersite.com"},
            {"2", "2", "www.amazon.com"}, {"3", "3", "www.nku.edu"}, {"4", "4", "www.youtube.com"},
            {"5", "5", "www.facebook.com"}, {"6", "6", "www.google.com"}, {"7", "6", ""}, {"8", "6", ""}};

            return Arrays.asList(params);
        }

        @Before
        public void setUp() {
            boolean hit = false; // Ignoreing hit because CacheLog broken
            this.instance = new CacheList("", this.CacheSize);
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
            boolean hit = false; // Ignoreing hit because CacheLog broken
            System.out.println("addNewObject: CaheSize = " + this.CacheSize + " | Expected return to be " + this.ExpectedVal);
            assertEquals(this.ExpectedVal, instance.addNewObject(URL, hit));
        }

        /**
         * Test of getCacheSize method, of class CacheList.
         */
        @Test
        public void testGetCacheSize() {
            System.out.println("getCacheSize: CaheSize = " + this.CurrentSize + " | Expected return to be " + this.CurrentSize
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
    }

    public static class CacheListSingleTests {

        private final int CacheSize = 10;
        private CacheList instance;
        private final String[] Websites = {"www.google.com", "www.facebook.com", "www.youtube.com",
            "www.nku.edu", "www.amazon.com", "www.someothersite.com"};

        @Before
        public void setUp() {
            boolean hit = false; // Ignoreing hit because CacheLog broken
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
