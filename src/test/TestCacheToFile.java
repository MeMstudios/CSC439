package test;

import org.junit.Before;
import org.junit.Test;
import lruproxy.CacheToFile;
import java.io.*;

import static junit.framework.Assert.assertEquals;

/**
 * com.CacheToFile Tester.
 *
 * @author yuyang liu
 * @version 1.0
 * @since <pre>十月 20, 2016</pre>
 */
public class TestCacheToFile {
    CacheToFile cacheToFile;
    String d = /*"LRUProxy" + File.separator +*/ "src" + File.separator + "test" + File.separator;

    @Before
    public void before() throws Exception {

        cacheToFile = new CacheToFile(d);
        File fileIsCached = new File(d, "twitter");
        fileIsCached.createNewFile();
        File fileWrite = new File(d, "google");
        fileWrite.delete();
        File fileIsRemoved = new File(d, "facebook");
        fileIsRemoved.createNewFile();

        StringBuffer sb = new StringBuffer();
        sb.append("www.read.com");
        cacheToFile.write("read", sb);

    }

    /**
     * Method: remove(String cachedURL)
     */
    @Test
    public void testRemove() throws Exception {
        cacheToFile.remove("twitter");
        assertEquals(false, cacheToFile.isCached("twitter"));
//TODO: Test goes here... 
    }

    /**
     * Method: write(String url, StringBuffer sb)
     */
    @Test
    public void testWrite() throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append("www.google.com");
        cacheToFile.write("google", sb);

        BufferedReader reader = new BufferedReader(new FileReader(d + "google"));
        assertEquals("www.google.com", reader.readLine());
    }

    /**
     * Method: read(String url)
     */
    @Test
    public void testRead() throws Exception {
        System.setOut(new PrintStream(new FileOutputStream(new File(d, "test"))));
        cacheToFile.read("read");
        BufferedReader reader = new BufferedReader(new FileReader(d + "test"));
        assertEquals("www.read.com", reader.readLine());
    }

    /**
     * Method: isCached(String cachedURL)
     */
    @Test
    public void testIsCached() throws Exception {
        assertEquals(true, cacheToFile.isCached("facebook"));


//TODO: Test goes here...
    }

} 
