package test;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
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
	
	@Before
	public void setUp() {
		inDirectory = "data" + File.separator;
		proxy = new Proxy(inDirectory, 5, 1, CacheList.LFU);
	}

	@Test
	public void test() {
		System.out.println("Executing TestProxyLFU");
		startTime = System.currentTimeMillis();
		proxy.run();
		endTime = System.currentTimeMillis();
		System.out.println("\nTime to execute: " + String.valueOf(endTime - startTime));
	}

}
