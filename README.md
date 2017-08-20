* Tutorial practice based on https://youtu.be/YrO4YSRkc-U?list=PLCpTH9CC0WwZlwiefbaUlG9jp344RedMd

Tutorial 1
- Setting up maven project based on Spring Boot
- Using Lombok plugin to avoid typing getter/setter methods manually
  * Lombok does not work with Eclipse by just adding dependency into pom.xml
    so, need to install to Eclipse manually (Refer to http://www.vogella.com/tutorials/Lombok/article.html)

Tutorial 2
- Using DTO for Request/Response is better because developer makes clear 
  1. which fields will be needed from Request
  2. which fields would be passed to Response
     * @JsonIgnore could be another option, but it is too strict, 
        which means it is always blocked. 

-http://www.joda.org/joda-time-hibernate/userguide.html