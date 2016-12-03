package test;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import org.junit.Test;
import lruproxy.CacheList;
import lruproxy.Proxy;

/**
 * @author Mikael
 *
 */
public class TestProxyLFU {

	String inDirectory;
	Proxy proxy;
	long startTime, endTime;

	@Test
	public void test() {
		inDirectory = "data" + File.separator;
		String[] args = { inDirectory, "3", "0", ""+CacheList.LFU};
		
		System.out.println("Executing TestProxyLFU");
		startTime = System.currentTimeMillis();
		Proxy.main(args);
		endTime = System.currentTimeMillis();
		
		try{
		    PrintWriter writer = new PrintWriter("data"+File.separator+"nonfunc_output.txt", "UTF-8");
		    writer.println("LFU Time: "+(endTime-startTime));
		    writer.close();
		} catch (IOException e) {}
		
		System.out.println("\nTime to execute: " + String.valueOf(endTime - startTime));
	}

}
