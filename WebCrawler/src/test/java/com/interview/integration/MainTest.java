package com.interview.integration;


import org.junit.Test;

import com.interview.WebCrawlerManager;

import java.io.IOException;
import java.net.URL;

public class MainTest {
	@Test
	public void testWebCrawler() throws IOException, InterruptedException {
		WebCrawlerManager webCrawlerManager = new WebCrawlerManager(50l);
		webCrawlerManager.start(new URL("http://www.prudential.co.uk/"));
	}
}
