## DemoTwitterApp
This app was created to demonstrate several technologies. App connects with a "Demo" Twitter account (using OAuth). Prior set up is required for connection (Connection details file, environment.properties, is ignored in git check-in).

Read more about spring integration with twitter [here](http://docs.spring.io/spring-integration/reference/html/twitter.html).

###Functions:

1. User service to demonstrate caching using Redis.
  - Custom implemented RCached and RCacheable annotations to cache objects in different regions.
2. Restful services to:
  - Post a new Tweet.
  - Retrieve Timeline updates.

###Technologies/Tools Used:

- Framework	: Spring (Beans, Transactions, MVC, ORM, Integration)
- ORM		: Hibernate
- Caching	: Redis
- Database	: MySQL

