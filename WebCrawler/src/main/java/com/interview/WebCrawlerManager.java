package com.interview;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;

public class WebCrawlerManager {

	private Set<URL> visitedLinkList = new HashSet<>();
	private Map<URL, Set<String>> imageList = new ConcurrentHashMap<URL, Set<String>>();
	private Map<URL, Set<URL>> externalLinks = new ConcurrentHashMap<URL, Set<URL>>();

	private List<Future<WebCrawler>> futures = new CopyOnWriteArrayList<>();
	private ExecutorService executorService = Executors.newWorkStealingPool();

	private String urlBase;

	private Long maxUrls;

	public WebCrawlerManager(Long maxUrls) {
		this.maxUrls = maxUrls;
	}

	public void start(URL url) throws IOException, InterruptedException {

		urlBase = url.getHost();

		StopWatch stopWatch = new StopWatch();

		stopWatch.start();
		submitNewURL(url, 0);

		while (checkPageVisited())
			;

		stopWatch.stop();
		System.out.print("\nVisited " + visitedLinkList.size() + " urls");
		System.out.println(" in " + stopWatch.getTime() / (1000 * 60) + " minutes");
		printSiteMap();

	}

	private void printSiteMap() {
		imageList.forEach((url, imageLinks) -> {
			System.out.printf("URL:: %s  \n", url);
			System.out.println("Image Links");
			for (String img : imageLinks) {
				System.out.println(img);
			}

			System.out.println("External Links");
			Set<URL> externalUrls = externalLinks.get(url);
			externalUrls.stream().filter(it -> it != null).forEach(it -> System.out.println("  " + it.toString()));

			System.out.println("********************************************************************\n");
		});
	}

	private boolean checkPageVisited() throws InterruptedException {
		Set<WebCrawler> webCrawlerList = new HashSet<>();
		futures.forEach(future -> {
			if (future.isDone()) {
				futures.remove(future);
				try {
					webCrawlerList.add(future.get());
				} catch (InterruptedException | ExecutionException e) {
				//	e.printStackTrace();
				}
			}
		});

		for (WebCrawler webCrawler : webCrawlerList) {
			addNewURLs(webCrawler);
		}

		return (futures.size() > 0);
	}

	private void addNewURLs(WebCrawler webCrawler) {
		imageList.put(webCrawler.getUrl(), webCrawler.getImageUrlList());
		for (URL url : webCrawler.getUrlList()) {
			if (url.toString().contains("#")) {
				try {
					url = new URL(StringUtils.substringBefore(url.toString(), "#"));
				} catch (MalformedURLException e) {
//					e.printStackTrace();
//					System.out.println("error in addNewURLs->" + e);
				}
			}
			fillExternalURLs(webCrawler);
			submitNewURL(url, webCrawler.getDepth() + 1);
		}
	}

	private void fillExternalURLs(WebCrawler WebCrawler) {
		Set<URL> externalUrls = new HashSet<URL>();

		for (URL url : WebCrawler.getUrlList()) {
			if (!url.toString().contains(urlBase)) {

				if (url.toString().startsWith("http")) {
					externalUrls.add(url);

				}

			}
		}
		externalLinks.put(WebCrawler.getUrl(), externalUrls);
	}

	private void submitNewURL(URL url, int depth) {
		if (shouldVisit(url)) {
			visitedLinkList.add(url);

			WebCrawler webCrawler = new WebCrawler(url, depth);
			Future<WebCrawler> future = executorService.submit(webCrawler);
			futures.add(future);
		}
	}

	private boolean shouldVisit(URL url) {
		if (visitedLinkList.contains(url)) {
			return false;
		}
		if (!url.toString().contains(urlBase)) {
			return false;
		}
		
		if (url.toString().toUpperCase().endsWith(".PDF") || url.toString().toUpperCase().endsWith(".ZIP") 
				||  url.toString().toUpperCase().endsWith(".DOCX") ||  url.toString().toUpperCase().endsWith(".PPT") ||  url.toString().toUpperCase().endsWith(".DOCX")
				||  url.toString().toUpperCase().endsWith(".XLS")
				) {
			return false;
		}
		if (maxUrls != null && visitedLinkList.size() >= maxUrls) {
			return false;
		}
		return true;
	}

	public Set<URL> getVisitedLinkList() {
		return visitedLinkList;
	}

	public Map<URL, Set<URL>> getExternalLinks() {
		return externalLinks;
	}

	public Map<URL, Set<String>> getImageList() {
		return imageList;
	}
}
