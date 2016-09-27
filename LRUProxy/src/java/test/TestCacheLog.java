import static org.junit.Assert.*;
import org.junit.Test;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Stream;

/**
 * Created by ethan on 9/21/16.
 * CSC 439 -- Team 3
 */

//NOTE:
    //fixed paths, should work with new directory structure
public class TestCacheLog {

    //Path to whatever folder
    private String srcPath = "LRUProxy/src/java/test";
    private String testdir = "/testdir";
    private String output = "/output.log";

    //method for counting lines in a file
    //we will use this to make sure the logging is occuring properly
    public static long countLines(String filename) {
        long cnt = 0;
        try{
            BufferedReader reader  = new BufferedReader(new FileReader(filename));

            Stream stream = reader.lines();
            cnt = stream.count();

            reader.close();
            return cnt;
        }
        catch ( IOException e){
            e.printStackTrace();
        }
        return cnt;
    }

    //Test that our count lines method works correctly
    @Test
    public void countTest( ){
        assertEquals( countLines( srcPath + testdir + "/counttestfile.txt"), 5);
        assertEquals( countLines (srcPath + testdir + "/counttestfile2.txt"), 6);
    }

    //Test that we can open the log properly
    @Test
    public void openLogForAppendTest(){
        CacheLog test1 = new CacheLog( srcPath + testdir );
        test1.openLogForAppend();
    }

    //Test that the logfile was created properly
    @Test
    public void logFileTest(){
        System.out.println(System.getProperty("user.dir"));
        long cnt = countLines( srcPath + testdir + output);
        CacheLog test = new CacheLog( srcPath + testdir);
        long cnt2 = countLines( srcPath + testdir + output);
        assertEquals( cnt, cnt2);
    }

    @Test
    //Test that logRemoval() logs one line to file
    public void logRemovalTest(){
        long cnt = countLines( srcPath + testdir + output);
        CacheLog test = new CacheLog( srcPath + testdir);
        test.logRemoval( "" );//no url
        long cnt2 = countLines( srcPath + testdir + output);
        assertEquals( cnt, (cnt2 - 1));
    }

    @Test
    //Test that logHit() logs one line to file
    public void logHitTest(){
        long cnt = countLines( srcPath + testdir + output);
        CacheLog test = new CacheLog( srcPath + testdir);
        test.logHit( "");//no url
        long cnt2 = countLines( srcPath + testdir + output);
        assertEquals( cnt, (cnt2 -1));
    }

    @Test
    //Test that logMiss() logs one line to file
    public void logMissTest(){
        long cnt = countLines( srcPath + testdir + output);
        CacheLog test = new CacheLog( srcPath + testdir);
        test.logMiss("");//no url
        long cnt2 = countLines( srcPath + testdir + output);
        assertEquals( cnt, (cnt2 -1 ));
    }

    @Test//combined test
    public void combinedTest(){
        long cnt = countLines( srcPath + testdir + output);
        CacheLog test = new CacheLog( srcPath + testdir );
        test.openLogForAppend();
        test.logHit("");
        test.logMiss("");
        test.logRemoval("");
        long cnt2 = countLines( srcPath + testdir + output );
        assertEquals( cnt, ( cnt2 - 3));//should be three more lines now
    }
}
