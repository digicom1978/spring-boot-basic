* Tutorial practice based on https://youtu.be/YrO4YSRkc-U?list=PLCpTH9CC0WwZlwiefbaUlG9jp344RedMd

Tutorial 1
- Setting up maven project based on Spring Boot
- Using Lombok plugin to avoid typing getter/setter methods manually
  * Lombok does not work with Eclipse by just adding dependency into pom.xml
    so, need to install to Eclipse manually (Refer to http://www.vogella.com/tutorials/Lombok/article.html)

Tutorial 2
- It is better way Using DTO for Request/Response instead of Domain Object,
  because developer makes clear 
  1. which fields will be needed from Request
  2. which fields would be passed to Response
     * @JsonIgnore could be another option, but it is too strict, 
        which means it is always blocked. 

-http://www.joda.org/joda-time-hibernate/userguide.html

Tutorial 3
- Exception handling of service layer
  1. Using return type -> check it is object or not
  2. Using parameter   -> pass parameter as function parameter
  3. Using exception   -> throw exception to caller
  4. Using asynchronous way like callback
  
Tutorial 4
- Github address : https://github.com/keesun/amugona
  1. Paging by Pageable
  2. Stream vs ParallelStream
     If function in map is simple or using lazy fatching, then do not use ParallelStream.

Tutorial 5
- Logging functionalities supported by Spring Boot
	slf4j : wrapper for logging libraries such as logback, log4j, jul
		- select logging library during compile time, by checking library on classpath
	xxx-over-slf4j : front library to slf4j
- Set logging option in application.properties under src/main/resources, src/test/resources
	logging.level.org.springframework.web=DEBUG : org springframework web Debug
	logging.level.org.springframework=off : all off
	==> all off except web related log
	
	logging.path=logs : set log folder (if it's not, spring will create)
	logging.file=logs/amugona.log : set logging path with file name (if both are present, then logging.path will be ignored
		* Every 10MB, new file will be created and used (file-appender.xml)
		* By maxHistory property, automatically can delete old log files
		* Some properties would cause heavy load, so not recommended
		
		How to change Log library -> change dependency in pom.xml
	
	https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-configure-logback-for-logging
	
	** Logstash  
- To customize log style, put logback.xml with custom setting into src/main/resources
* with plugin,logback can save logs into Elastic search





- Customizing banner
	-- src/main/resources > banner.txt
	-- ASCII generator
	-- https://docs.spring.io/spring-boot/docs/1.5.7.RELEASE/reference/htmlsingle/#boot-features-banner

Tutorial 6
- Implementing GET, UPDATE API

* http://www.vinaysahni.com/best-practices-for-a-pragmatic-restful-api

