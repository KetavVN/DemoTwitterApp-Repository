package com.redis.test.web;

import java.util.Properties;

import javax.inject.Inject;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Java based root application context
 * 
 * @author ketav
 */

@Configuration
@EnableTransactionManagement
@EnableSpringConfigured
@EnableAspectJAutoProxy
//@EnableIntegration
@ImportResource("classpath:/com/test/twitter/twitter.xml")
@PropertySource("classpath:/com/test/twitter/environment.properties")
@ComponentScan(basePackages={"com.redis.test.dao", "com.redis.test.service",
	"com.redis.test.aspects", "com.redis.test.annotations"})
public class AppContext {
	
	//1st way of injecting .properties values env.getProperty("name")
	@Inject private Environment env;
	
	//2nd way to inject .properties values using @Value annotation
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	@Bean
	public BasicDataSource dataSource(@Value("${database.url}") String url,
			@Value("${database.username}") String userName,
			@Value("${database.password}") String password,
			@Value("${database.driver.classname}") String driverClassName,
			@Value("${database.connection.pool.max}") int maxConnection){
		
		BasicDataSource ds = new BasicDataSource();
		ds.setUrl(url);
		ds.setUsername(userName);
		ds.setPassword(password);
		ds.setDriverClassName(driverClassName);
		ds.setMaxActive(maxConnection);
		return ds;
	}
	
	@Bean @Inject
	public LocalSessionFactoryBean sessionFactory(BasicDataSource dataSource,
			@Value("${hibernate.scan.packages}") String packages,
			@Value("${hibernate.dialect}") String dialect,
			@Value("${hibernate.show_sql}") String showSql,
			@Value("${hibernate.default_shema}") String schema,
			@Value("${hibernate.hbm2ddl.auto}") String dbm2ddl){
		
		Properties p = new Properties();
		p.setProperty("hibernate.dialect", dialect);
		p.setProperty("hibernate.show_sql", showSql);
		p.setProperty("hibernate.default_shema", schema);
		p.setProperty("hibernate.hbm2ddl.auto", dbm2ddl);
		p.setProperty("hibernate.packagesToScan", packages);
		
		LocalSessionFactoryBean sf = new LocalSessionFactoryBean();
		sf.setDataSource(dataSource);
		sf.setPackagesToScan(packages);
		sf.setHibernateProperties(p);
		
		return sf;
	}
	
	@Bean @Inject
	public HibernateTransactionManager transactionManager(LocalSessionFactoryBean sf){
		return new HibernateTransactionManager(sf.getObject());
	}
	
	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslator(){
		return new PersistenceExceptionTranslationPostProcessor();
	}
	
	@Bean
	public JedisConnectionFactory jedisConnectionFactory(){
		JedisConnectionFactory cf = new JedisConnectionFactory();
		cf.setHostName("localhost");
		cf.setPort(6379);
		return cf;
	}
	
	@Bean @Inject
	public RedisTemplate<?, ?> redisTemplate(JedisConnectionFactory jedisConnectionFactory){
		RedisTemplate<?, ?> t = new RedisTemplate<>();
		t.setConnectionFactory(jedisConnectionFactory);
		return t;
	}
	
	@Bean
	public TwitterTemplate twitterTemplate(){
		String consumerKey = env.getProperty("twitter.consumer-key");
		String consumerSecret = env.getProperty("twitter.consumer-secret");
		String accessToken = env.getProperty("twitter.access-token");
		String accessTokenSecret = env.getProperty("twitter.access-token-secret");
				
		return new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);
	}
		
	public static void main(String ...args){
		ApplicationContext context = new AnnotationConfigApplicationContext(AppContext.class);
		for(int i=0;i<10;i++){
			try{
				//System.out.println(context.getBean(TweetServiceImpl.class).getTweets());
				Thread.sleep(5000);
			} catch (Exception e){
				e.printStackTrace(System.out);
			}			
		}

		//context.getBean(TweetController.class).tweet(Calendar.getInstance().getTime()+" New tweet from test");
		((AbstractApplicationContext)context).close();
	}

}
