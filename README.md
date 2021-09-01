# 멍분이의 커뮤니티


## How to run Program

### SPEC
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


## How to use EmailService
* in in SpringMVCCommunity/src/main/resources/application.properties

change below code
```
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username={your email}
spring.mail.password={your app password}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.timeout=5000
spring.mail.properties.mail.smtp.starttls.enable=true
```

* in My case. I use google email. 
* if you want to use google email. then you need to 2 step sequrity.
* and then you can use app password.
* write your email and app password.
