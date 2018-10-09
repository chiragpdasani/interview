package com.interview;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Jsoup.class)
public class WebCrawlerTest {
	

	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Mock
	private Connection mockConnection;

	@Test
	public void testCall() throws Exception {

		String dummyHTML = new String(Files.readAllBytes(Paths.get(getClass().getResource("/dummy.html").toURI())));
		Document document = Jsoup.parse(dummyHTML);

		URL url = new URL("http://www.prudential.co.uk/");
		mockStatic(Jsoup.class);
		PowerMockito.when(Jsoup.connect(url.toString())).thenReturn(mockConnection);
		
		PowerMockito.when(mockConnection.ignoreContentType(true)).thenReturn(mockConnection);
		PowerMockito.when(mockConnection.get()).thenReturn(document);
	//	PowerMockito.when(Jsoup.parse(eq(url), anyInt())).thenReturn(document);
		
		WebCrawler webCrawler = new WebCrawler(url, 1);
		webCrawler.call();

		Set<URL> urls = webCrawler.getUrlList();
		assertEquals(3, urls.size());
		assertTrue(urls.contains(new URL("http://www.prudential.co.uk/1")));
		assertTrue(urls.contains(new URL("http://www.prudential.co.uk/2")));
		assertTrue(urls.contains(new URL("http://www.prudential.co.uk/../relative")));
		
		Set<String> imageList = webCrawler.getImageUrlList();
		System.out.println(imageList);
		assertEquals(2, imageList.size());
		
		URL actualURL=webCrawler.getUrl();
		assertEquals(url.getHost(), actualURL.getHost());
		int actualDepth = webCrawler.getDepth();
		assertEquals(1, actualDepth);
//		assertTrue(imageList.contains("http://www.prudential.co.uk/~/media/images/P/Prudential-V2/logo/footer-logo.png"));
//		assertTrue(imageList.contains("http://www.prudential.co.uk/~/media/Images/P/Prudential-V2/icons/pdf-icon"));
		
	}

	@After
	public void resetMocks() {
		// Mockito.reset(mockWebCrawler,mockExecutor,mockFuture);
	}

}
