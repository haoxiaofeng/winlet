package com.aggrepoint.winlet;

/**
 * 个性化规则引擎，负责执行个性化规则表达式
 * 
 * @author Jim
 */
public interface PsnRuleEngine {
	boolean eval(String rule) throws Exception;
}
