# WebCrawler

* Prerequisites

	1. JDK 1.8 or above
	2. Maven 3.5 or above
	
* How to build and run

	1. Window OS
		Double click on run.bat file
	2. Linux OS
		Double click on run.sh file
		
	It will start crawling URL – say http://www.prudential.co.uk/ and visit maximum 1000 pages within the domain. You can change URL or increase/decrease maximum limits by changing arguments in run.bat or run.sh file. Please follow following steps if you want to decrease visit the maximum pages limit.
	
	1. Open run.bat
	2. Update second arguments as 100 instead of 1000

*  Explanation of what could be done with more time 

	1. Could be returned more details in site map
	2. Could be handled more exception handling like url not found or timeout etc..
	3. Could be converted this java application to spring boot profiler application
	
* Project tests as per instruction

	1. Run MainTest class for integration test case, it will start web crawling for default 50 pages
	2. Run WebCrawlerManagerTest and WebCrawlerTest for unit test cases

* Project build 

	1. Generate runnable jar file
	
		Open this WebCrawler maven project in eclipse editor and export as runnable jar file with selects Main class in Launch configuration drop down and enter path with jar file name in Export destination
		
	2. Create snapshot jar
	
		Open command prompt, navigate up to WebCrawler directory (where you have downloaded this project) and run below command. It will generate snapshot jar at project's target directory
		
		mvn clean install
	
	
	
    
