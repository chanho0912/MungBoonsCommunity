# SpringMVCCommunity


## How to run Program


* Using Only jdk-11
* Gradle-6.8


Clone the code. 
```
gradlew build
```

## How to connect Database
* in SpringMVCCommunity/src/main/resources/application.properties

change below code
```
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver 
spring.datasource.url={your database server url}
spring.datasource.username={your database username}
spring.datasource.password={your database password}
```

* in my case, I use Mysql. if you don't want to use Mysql... also change spring.datasource.driver-class-name=your database Driver...

