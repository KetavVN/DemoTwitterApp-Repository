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
	
	@Pointcut(value="execution(@com.redis.test.annotations.RCached * saveUser(*)) "
			+ "&& args(object) "
			+ "&& @annotation(cached) "
			+ "&& @args(cacheable)")
	public void savePointCut(Object object, RCached cached, RCacheable cacheable){}
	
	@Pointcut(value="execution(@com.redis.test.annotations.RCached * remove*(*)) "
			+ "&& args(object) "
			+ "&& @annotation(cached) "
			+ "&& @args(cacheable)")
	public void deletePointCut(Object object, RCached cached, RCacheable cacheable){}
	
	@Pointcut(value="execution(@com.redis.test.annotations.RCached * find*(*)) && @annotation(cached)")
	public void findPointCut(RCached cached){}
	
	@Around(value="savePointCut(object, cached, cacheable)")
	public Object saveAdvice2(ProceedingJoinPoint jp, Object object, RCached cached,
			RCacheable cacheable) throws Throwable {
		try {
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
		} catch(Throwable e) {
			logger.error(e.getMessage(), e);
			throw e;
		} finally {
			//
		}
	}
	
	@Around("findPointCut(cached)")
	public Object findAdvice(ProceedingJoinPoint jp, RCached cached) throws Throwable {
		try {
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
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
			throw e;
		} finally {
			//
		}
	}
	
	@Around("deletePointCut(object, cached, cacheable)")
	public Object deleteAdvice(ProceedingJoinPoint jp, Object object,
			RCached cached, RCacheable cacheable) throws Throwable {
		try {
			Object returnObj = jp.proceed();
			String hashesName = cached.key();
			String fieldName = cacheable.keyField();
			Method method = object.getClass().getDeclaredMethod("get"+StringUtils.capitalize(fieldName));
			if(method==null)
				return returnObj;
			Object hashKey = method.invoke(object);
			hashOps.delete(hashesName, hashKey);
			return returnObj;
		} catch(Throwable e) {
			logger.error(e.getMessage(), e);
			throw e;
		} finally {
			//
		}
	}
	
}
