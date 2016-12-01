package test;

import lruproxy.CacheList;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/*
 * @author Nick Borne
 * @since Nov 28, 2016
 *
 * MRU CacheList Unit Test
 *
 */

import java.io.File;
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
import lruproxy.CacheList;

@RunWith(Enclosed.class)
public class TestMRU {

    @RunWith(value = Parameterized.class)
    public static class CacheListParam {

        private int CacheSize;
        private int CurrentSize;
        private String ExpectedVal;
        private CacheList instance;
        private String directory = "src" + File.separator + "test" + File.separator +
                "testdir";
        private final String[] Websites = {"www.google.com", "www.facebook.com", "www.youtube.com",
                "www.nku.edu", "www.amazon.com", "www.someothersite.com"};

        public CacheListParam(String cacheSize, String currentSize, String exp) {
            this.CacheSize = Integer.parseInt(cacheSize);
            this.CurrentSize = Integer.parseInt(currentSize);
            this.ExpectedVal = exp;
        }

        @Parameterized.Parameters
        public static Collection<String[]> getTestParameters() {

            String[][] params = {{"0", "1", "www.someothersite.com"}, {"1", "1", "www.someothersite.com"},
                    {"2", "2", "www.someothersite.com"}, {"3", "3", "www.someothersite.com"}, {"4", "4", "www.someothersite.com"},
                    {"5", "5", "www.someothersite.com"}, {"6", "6", "www.someothersite.com"}, {"7", "6", ""}, {"8", "6", ""}};
            return Arrays.asList(params);
        }

        @Before
        public void setUp() {
            boolean hit = false;
            this.instance = new CacheList(directory, this.CacheSize, 1);
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
            boolean hit = false;
            System.out.println("addNewObject: CacheSize = " + this.CacheSize + " | Expected return to be " + this.ExpectedVal);
            assertEquals(this.ExpectedVal, instance.addNewObject(URL, hit));

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
    }

    public static class CacheListSingleTests {

        private final int CacheSize = 10;
        private CacheList instance;
        private final String[] Websites = {"www.google.com", "www.facebook.com", "www.youtube.com",
                "www.nku.edu", "www.amazon.com", "www.someothersite.com"};

        @Before
        public void setUp() {
            boolean hit = false; // Ignoreing hit because CacheLog broken
            this.instance = new CacheList("", this.CacheSize, 1);
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
