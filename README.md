# 멍분이의 커뮤니티

## Tech
* Spring Boot
* MySQL
* Thymeleaf

## About
* 간단한 관심 주제 기반 커뮤니티 사이트를 개발하였습니다.

## How to run Program

### environment
* jdk-11
* Gradle-6.8


Clone the code. 
```
gradlew build
```
## UI/UX
### Main Page
<img src="https://user-images.githubusercontent.com/53031059/139574832-cdc732c3-0f57-490e-9fb9-dc9308a873b6.png" height="100%" width="100%">

### Search Page
<img src="https://user-images.githubusercontent.com/53031059/139574862-937d7af3-f24c-4489-987b-6b8f77be3ad7.png" height="100%" width="100%">

### Profile (관심 주제 변경)
<img src="https://user-images.githubusercontent.com/53031059/139574896-a767e798-2863-4301-905a-3512bcad1607.png" height="100%" width="100%">

### Notification
<img src="https://user-images.githubusercontent.com/53031059/139574907-fb7e607d-4d5c-47cb-a168-92fd075c27c4.png" height="100%" width="100%">


### SignUp, Login
<img src="https://user-images.githubusercontent.com/53031059/139574912-fe707003-0d66-4747-a5aa-7ee80d4f7016.png" height="100%" width="100%">
<img src="https://user-images.githubusercontent.com/53031059/139574920-cb617ed7-941e-4bf9-b501-cc62077bee57.png" height="100%" width="100%">


## How to connect Database
* in SpringMVCCommunity/src/main/resources/application.properties

change below code
```
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver 
spring.datasource.url={your database server url}
spring.datasource.username={your database username}
spring.datasource.password={your database password}
```

* in my case, I use MySQL. if you don't want to use MySQL, also change spring.datasource.driver-class-name=your database Driver...


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

* in my case. I use google email. 
* if you want to use google email. then you need to 2 step security.
* and then you can use app password.
* write your email and app password.

## FrontEnd Library
* bootstrap
* cropper
* font-awesome
* jdenticon
* jquery
* jquery-cropper

## Asynchronous Infra
```java
@Slf4j
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int processors = Runtime.getRuntime().availableProcessors();
        log.info("creating pool with core count {}", processors);

        executor.setCorePoolSize(processors);
        executor.setMaxPoolSize(processors * 2);
        executor.setQueueCapacity(50);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("AsyncExecutor-");
        executor.initialize();
        return executor;
    }
}
```

### version info
```
"bootstrap": "^4.4.1",
"cropper": "^4.1.0",
"font-awesome": "^4.7.0",
"jdenticon": "^2.2.0",
"jquery": "^3.4.1",
"jquery-cropper": "^1.0.1"
```
