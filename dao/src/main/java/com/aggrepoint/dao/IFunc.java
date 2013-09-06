package com.aggrepoint.dao;

import java.lang.annotation.Annotation;

/**
 * @author Jiangming Yang (yangjm@gmail.com)
 */
public interface IFunc {
	public String getName();

	/**
	 * 执行方法
	 * 
	 * @param params
	 *            方法参数
	 * @param args
	 *            Dao方法的参数
	 * @param anns
	 *            Dao方法参数的注解
	 * @return
	 */
	public String exec(String[] params, Object[] args, Annotation[][] anns);
}
