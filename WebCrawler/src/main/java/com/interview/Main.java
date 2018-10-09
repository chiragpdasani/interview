package com.interview;


import java.net.URL;

public class Main {
	private  static String DEFAULT_URL="http://www.prudential.co.uk/";
	private  static Long DEFAULT_MAX_URL=null;
	public static void main(String[] args) throws Exception{
		
		if(args.length>0) {
			int count=1;
			for(String arg:args) {
				System.out.println("arg"+count+"::"+ arg);
				count++;
			}
			DEFAULT_URL=args[0];
		}
		if(args.length>1) {
			DEFAULT_MAX_URL=Long.parseLong(args[1]);
		}
		WebCrawlerManager webCrawlerManager = new WebCrawlerManager(DEFAULT_MAX_URL);
		
		System.out.printf("***** Start Web Crawling for:: %s and Max Visit Urls::%s ******** \n\n",DEFAULT_URL,DEFAULT_MAX_URL);
		webCrawlerManager.start(new URL(DEFAULT_URL));

	}

}
