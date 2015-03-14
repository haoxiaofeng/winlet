package com.aggrepoint.winlet.spring.def;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Hashtable;

import org.springframework.core.annotation.AnnotationUtils;

import com.aggrepoint.winlet.spring.annotation.Code;
import com.aggrepoint.winlet.spring.annotation.Return;

/**
 * 
 * @author Jiangming Yang (yangjm@gmail.com)
 */
public class ReturnDefList {
	private Hashtable<String, ArrayList<ReturnDef>> htRetDefs = new Hashtable<String, ArrayList<ReturnDef>>();

	private ArrayList<ReturnDef> getRetDefList(String key) {
		synchronized (htRetDefs) {
			ArrayList<ReturnDef> list = htRetDefs.get(key);
			if (list == null) {
				list = new ArrayList<ReturnDef>();
				htRetDefs.put(key, list);
			}

			return list;
		}
	}

	public ReturnDefList(Method method) {
		Return[] rets = method.getAnnotationsByType(Return.class);
		if (rets != null)
			for (Return ret : rets) {
				ReturnDef rd = new ReturnDef(ret);
				if (rd.hasValue())
					getRetDefList(rd.getValue()).add(rd);
			}
	}

	public ArrayList<ReturnDef> getReturnDef(String code) {
		ArrayList<ReturnDef> list = htRetDefs.get(code);
		if (list == null || list.size() == 0)
			return htRetDefs.get(Return.NOT_SPECIFIED);

		return list;
	}
}
