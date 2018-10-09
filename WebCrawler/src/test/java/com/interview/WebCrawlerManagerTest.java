package com.interview;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static org.junit.Assert.*;


import static org.mockito.Mockito.when;

public class WebCrawlerManagerTest {
	private static final int VISITED_LINKED_SIZE = 4;
	private static final int EXTERNAL_LINKED_SIZE = 1;
	@Mock
	private ExecutorService mockExecutor;

	@Mock
	private WebCrawler mockWebCrawler;

	@Mock
	private Future<WebCrawler> mockFuture;

	@InjectMocks
	private WebCrawlerManager webCrawlerManager = new WebCrawlerManager(4l);

	private Set<URL> urlList;
	private Set<String> imageUrlList;
	private URL url;
	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

	@Before
	public void setUp() throws MalformedURLException {
		urlList = new HashSet<>();
		urlList.add(new URL("http://www.prudential.co.uk/"));
		urlList.add(new URL("http://www.eastspring.com"));
		urlList.add(new URL("http://www.prudential.co.uk/insights/attitudes-to-retirement-in-east-asia"));
		urlList.add(new URL("http://www.prudential.co.uk/insights/nopdfs.pdf"));
		urlList.add(new URL("http://www.prudential.co.uk/insights/the-prudential-relationship-index"));
		urlList.add(new URL("http://www.prudential.co.uk/investors/shareholder-information/buying-and-selling-prudential-shares"));
		urlList.add(new URL("http://www.prudential.co.uk/insights/the-prudential-relationship-index/1"));
		urlList.add(new URL("http://www.prudential.co.uk/insights/the-prudential-relationship-index/2"));
		urlList.add(new URL("http://www.prudential.co.uk/insights/the-prudential-relationship-index/3"));
		urlList.add(new URL("http://www.prudential.co.uk/insights/the-prudential-relationship-index/3"));
		urlList.add(new URL("http://www.prudential.co.uk/insights/nopdfs.zip"));
		 url= new URL("http://www.prudential.co.uk/");
		 
		imageUrlList = new HashSet<>();
		imageUrlList.add("https://www.prudential.co.uk/~/media/Images/P/Prudential-V2/logo/prudential-logo.png?h=68&la=en&w=160");

	}


	@Test
	public void testStartForCheckVisitedCorrectURL() throws InterruptedException, ExecutionException, MalformedURLException, IOException {

		when(mockWebCrawler.getUrlList()).thenReturn(urlList);
		when(mockWebCrawler.getImageUrlList()).thenReturn(imageUrlList);
		when(mockWebCrawler.getUrl()).thenReturn(url);

		when(mockFuture.isDone()).thenReturn(true);
		when(mockFuture.get()).thenReturn(mockWebCrawler);
		when(mockExecutor.submit(Mockito.any(WebCrawler.class))).thenReturn(mockFuture);
		

		webCrawlerManager.start(new URL("http://www.prudential.co.uk/"));

		Set<URL> visited = webCrawlerManager.getVisitedLinkList();
		assertEquals(VISITED_LINKED_SIZE, visited.size());
		assertTrue(visited.contains(new URL("http://www.prudential.co.uk/")));
		assertFalse(visited.contains(new URL("http://www.prudential.co.uk/insights/nopdfs.pdf")));
		Map<URL, Set<String>> actualImageList=webCrawlerManager.getImageList();
		assertEquals(imageUrlList.size(),actualImageList.size());
		
		Map<URL, Set<URL>> actualExternalList=webCrawlerManager.getExternalLinks();
		assertEquals(EXTERNAL_LINKED_SIZE,actualExternalList.size());
	}
	
	@After
	public void resetMocks() {
	    Mockito.reset(mockWebCrawler,mockExecutor,mockFuture);
	}

}
