package com.game.engine.basesource.db.annotiation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 基础数据表 标识 
 * @author lyh
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface BResource {

	/** 表名字,如没有则使用默认类名*/
	public String tableName() default "";
	
}
