package com.aggrepoint.winlet.form;

import java.util.Collection;
import java.util.function.Function;

/**
 * 
 * @author Jiangming Yang (yangjm@gmail.com)
 */
public interface Form {
	/** 判断当前是是否在进行表单单字段校验 */
	public boolean isValidateField();

	/** 若当前在进行表单字段校验，返回被校验的字段的名称 */
	public String getValidateFieldName();

	/** 若当前在进行表单字段校验，返回被校验的字段的ID */
	public String getValidateFieldId();

	/**
	 * 判断当前是否需要对字段field进行校验
	 * 
	 * @param field
	 * @return
	 */
	public boolean validate(String field);

	/**
	 * 对字段field进行校验
	 * 
	 * @param field
	 * @param when
	 *            true表示校验未通过
	 * @param error
	 *            如果校验失败，使用的错误信息
	 */
	public Form addError(String field, Function<String, Boolean> when,
			String error);

	public Form addError(String field, boolean when, String error);

	public Form addError(String field, Function<String, Boolean> when,
			String error, boolean validateEvenErrorExist);

	public Form addError(String field, boolean when, String error,
			boolean validateEvenErrorExist);

	/**
	 * 获取字段field的值。
	 * 
	 * @param field
	 * @return
	 */
	public String getValue(String field);

	/**
	 * 获取字段field的值，如果不存在则返回缺省值。
	 * 
	 * @param field
	 * @param def
	 * @return
	 */
	public String getValue(String field, String def);

	/**
	 * 获取字段field的值。
	 * 
	 * @param field
	 * @return
	 */
	public String[] getValues(String field);

	public void setValue(String field, String value);

	public void setValue(String fields, String[] value);

	/**
	 * 判断表单中是否存在校验错误。
	 * 
	 * @return
	 */
	boolean hasError();

	boolean hasError(boolean fieldErrorsOnly);

	boolean hasError(String field);

	boolean hasErrorOrValidateField();

	/**
	 * 获取字段上当前存在的所有校验错误
	 * 
	 * @param field
	 * @return
	 */
	public String[] getErrors(String field);

	/**
	 * 给字段添加校验错误信息。
	 * 
	 * @param field
	 * @param error
	 */
	public void addError(String field, String error);

	/**
	 * 清除指定字段上所有校验错误信息。
	 * 
	 * @param field
	 */
	public void clearError(String field);

	public void clearErrors();

	/**
	 * 将指定字段的状态改为禁用。
	 * 
	 * @param field
	 */
	public void setDisabled(String field);

	/**
	 * 将指定字段的状态改为启用。
	 * 
	 * @param field
	 */
	public void setEnabled(String field);

	/**
	 * 显示表单中的元素。
	 * 
	 * @param field
	 */
	public void show(String selector);

	/**
	 * 不显示表单中的元素。
	 * 
	 * @param field
	 */
	public void hide(String selector);

	/**
	 * 获取当前所有处于禁用状态的字段。
	 * 
	 * @return
	 */
	public String[] getDisabledFields();

	/**
	 * 是否存在field
	 * 
	 * @param field
	 * @return
	 */
	public boolean hasField(String field);

	void setSelectOptions(String field, Collection<? extends SelectOption> list);

	public String mapStatus(String vf, String vfError, String error);

	public String mapStatus(String vf, String vfError, String error,
			String passed);

	public Form noError(Process process) throws Exception;
}
