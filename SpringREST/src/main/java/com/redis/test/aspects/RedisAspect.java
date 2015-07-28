package com.redis.test.aspects;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.redis.test.annotations.RCacheable;
import com.redis.test.annotations.RCached;

/**
 * Redis Cache Aspect
 * 
 * @author ketav
 */

@Aspect @Component
public class RedisAspect {
	
	private static final Logger logger = LoggerFactory.getLogger(RedisAspect.class);
	
	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;
	
	@Resource(name="redisTemplate")
	private HashOperations<String, Object, Object> hashOps;

	/*@Pointcut("execution(@com.redis.test.annotations.RCached * saveUser(..))")
	public void savePC(){}*/
	
	@Pointcut(value="execution(@com.redis.test.annotations.RCached * saveUser(*)) "
			+ "&& args(object) "
			+ "&& @annotation(cached) "
			+ "&& @args(cacheable)")
	public void savePC2(Object object, RCached cached, RCacheable cacheable){}
	
	@Pointcut(value="execution(@com.redis.test.annotations.RCached * remove*(*)) "
			+ "&& args(object) "
			+ "&& @annotation(cached) "
			+ "&& @args(cacheable)")
	public void deletePC(Object object, RCached cached, RCacheable cacheable){}
	
	@Pointcut(value="execution(@com.redis.test.annotations.RCached * find*(*)) && @annotation(cached)")
	public void findPC(RCached cached){}
	
	
	//first way - using raw reflection
	/*@Around("savePC()")
	public Object saveAdvice(ProceedingJoinPoint jp) throws Throwable {
		try{
			Object returnObj = jp.proceed();
			RCached cached = jp.getTarget().getClass().getDeclaredMethod(jp.getSignature().getName(), User.class).getAnnotation(RCached.class);
			String hashesName = cached.key();
			Class<?> className = jp.getArgs()[0].getClass();
			RCacheable cacheable = className.getAnnotation(RCacheable.class);
			String fieldName = cacheable.keyField();
			Method method = className.getDeclaredMethod("get"+StringUtils.capitalize(fieldName));
			if(method==null)
				return returnObj;
			String hashKey = (String) method.invoke(jp.getArgs()[0]);
			User user = (User) jp.getArgs()[0];
			hashOps.put(hashesName, hashKey, user);
			redisTemplate.expire(hashesName, cacheable.timeOut(), TimeUnit.SECONDS);
			return returnObj;
		} catch(Throwable e){
			logger.error(e.getMessage(), e);
			throw e;
		} finally {
			//ignore
		}
	}*/
	
	//second way - using @annotation and @args
	@Around(value="savePC2(object, cached, cacheable)")
	public Object saveAdvice2(ProceedingJoinPoint jp, Object object, RCached cached,
			RCacheable cacheable) throws Throwable {
		try{
			Object returnObj = jp.proceed();
			String hashesName = cached.key();
			String fieldName = cacheable.keyField();
			Method method = object.getClass().getDeclaredMethod("get"+StringUtils.capitalize(fieldName));
			if(method==null)
				return returnObj;
			Object hashKey = method.invoke(object);
			hashOps.put(hashesName, hashKey, object);
			redisTemplate.expire(hashesName, cacheable.timeOut(), TimeUnit.SECONDS);
			return returnObj;
		} catch(Throwable e){
			logger.error(e.getMessage(), e);
			throw e;
		} finally {
			//ignore
		}
	}
	
	@Around("findPC(cached)")
	public Object findUserAdvice(ProceedingJoinPoint jp, RCached cached) throws Throwable {
		try{
			Class<?> returnType = ((MethodSignature)jp.getSignature()).getReturnType();
			RCacheable cacheable = returnType.getAnnotation(RCacheable.class);
			Object keyValue = jp.getArgs()[0];
			String hashesName = cached.key();
			Object returnVal = null;
			if(cacheable!=null){
				returnVal = hashOps.get(hashesName, keyValue);
				if(returnVal!=null)
					return returnVal;	
			}
			returnVal = jp.proceed();
			if(returnVal!=null)
				hashOps.put(hashesName, keyValue, returnVal);
			return returnVal;
		} catch (Throwable e){
			logger.error(e.getMessage(), e);
			throw e;
		} finally {
			//ignore
		}
	}
	
	@Around("deletePC(object, cached, cacheable)")
	public Object deleteUserAdvice(ProceedingJoinPoint jp, Object object,
			RCached cached, RCacheable cacheable) throws Throwable {
		try{
			Object returnObj = jp.proceed();
			String hashesName = cached.key();
			String fieldName = cacheable.keyField();
			Method method = object.getClass().getDeclaredMethod("get"+StringUtils.capitalize(fieldName));
			if(method==null)
				return returnObj;
			Object hashKey = method.invoke(object);
			hashOps.delete(hashesName, hashKey);
			return returnObj;
		} catch(Throwable e){
			logger.error(e.getMessage(), e);
			throw e;
		} finally {
			//ignore
		}
	}
	
}
