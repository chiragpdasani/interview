package com.interview;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.interview.utils.Utils;

public class WebCrawler implements Callable<WebCrawler> {

	static final int TIMEOUT = 60000; // one minute

	private URL url;
	private int depth;
	private Set<URL> urlList = new HashSet<>();
	private Set<String> imageUrlList = new HashSet<>();

	public WebCrawler(URL url, int depth) {
		this.url = url;
		this.depth = depth;
	}

	@Override
	public WebCrawler call() throws Exception {
		Document document = null;
		System.out.println("Visiting (" + depth + "): " + url.toString());
		document=Jsoup.connect(url.toString()).ignoreContentType(true).get();
		//document = Jsoup.parse(url, TIMEOUT);

		processLinks(document.select("a[href]"));
		processImageLinks(document.select("[src]"));
		return this;
	}

	private void processImageLinks(Elements imageLinks) {
		imageLinks.stream()
				  .filter(src -> src.tagName().equals("img"))
				  .forEach(src->{

					  imageUrlList.add(Utils.getFormattedString(" * %s: <%s> ", src.tagName(), src.attr("src")));
				  });
	}

	private void processLinks(Elements links) {
		links.stream().filter(link -> {
			String href = link.attr("href");
			return (StringUtils.isNotBlank(href)) && !href.startsWith("#");
		}).forEach(link -> {
			try {
				String href = link.attr("href");
				URL nextUrl = new URL(url, href);
				urlList.add(nextUrl);
			} catch (MalformedURLException e) { 
			
			}
		});
	}

	public Set<String> getImageUrlList() {
		return imageUrlList;
	}

	public Set<URL> getUrlList() {
		return urlList;
	}

	public int getDepth() {
		return depth;
	}

	public URL getUrl() {
		return url;
	}

}
