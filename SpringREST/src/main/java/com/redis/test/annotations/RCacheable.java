package com.redis.test.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark entity as Cacheable.
 * 
 * @author ketav
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RCacheable {
	public String keyField();
	public int timeOut() default 3600;
}
