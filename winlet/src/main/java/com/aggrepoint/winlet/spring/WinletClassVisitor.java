package com.aggrepoint.winlet.spring;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aggrepoint.winlet.spring.annotation.Action;
import com.aggrepoint.winlet.spring.annotation.Window;
import com.aggrepoint.winlet.spring.annotation.Winlet;

/**
 * 对使用了@Winlet注解的类进行以下处理：
 * 
 * <pre>
 * 1) 若类上定义了RequestMapping注解，则将其去除
 * 2) 为类添加RequestMapping注解
 * !! 3) 若方法上定义了@RequestMapping注解，则将注解去除 !! - 20150917 不去除 － Winlet中也可以直接定义@RequestMapping方法
 * 4) 若@Window注解没有指明value，则将其方法名替换为value
 * 5) 对于所有@Window注解，创建对应的RequestMapping注解
 * 6) 若Action注解没有指明value，则将其方法名替换为value
 * </pre>
 * 
 * @author Jiangming Yang (yangjm@gmail.com)
 */
public class WinletClassVisitor extends ClassVisitor implements Opcodes {
	static String getClassDesc(Class<?> c) {
		String str = c.getName().replaceAll("\\.", "/");
		return "L" + str + ";";
	}

	final String DESC_WINLET = getClassDesc(Winlet.class);
	final String DESC_ACTION = getClassDesc(Action.class);
	final String DESC_WINDOW = getClassDesc(Window.class);
	final String DESC_REQUEST_MAPPING = getClassDesc(RequestMapping.class);

	String winletPath = "";
	boolean hasWindow = false;
	boolean hasAction = false;

	public WinletClassVisitor(ClassVisitor inner) {
		super(ASM5, inner);
	}

	public boolean isWinlet() {
		return !"".equals(winletPath) || hasWindow || hasAction;
	}

	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		if (DESC_REQUEST_MAPPING.equals(desc)) {
			// 若类上使用了@RequestMapping注解，将其去除
			return null;
		} else if (DESC_WINLET.equals(desc)) {
			// 处理@Winlet注解
			return new AnnotationVisitor(ASM5, cv.visitAnnotation(desc, visible)) {
				public void visit(String name, Object value) {
					super.visit(name, value);

					// 提取@Winlet注解value
					if ("value".equals(name))
						winletPath = value.toString();
				}

				public void visitEnd() {
					super.visitEnd();

					if (!isWinlet())
						return;

					// 加入@RequestMapping注解
					av = cv.visitAnnotation(DESC_REQUEST_MAPPING, true);
					AnnotationVisitor av1 = av.visitArray("value");
					String path = winletPath.startsWith("/") ? winletPath : "/" + winletPath;
					// 用/win/开始，以区别/site/和其他内容
					av1.visit(null, "/win" + path);
					av1.visitEnd();
					av.visitEnd();
				}
			};
		}

		return cv.visitAnnotation(desc, visible);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		final String methodName = name;

		return new MethodVisitor(ASM5, super.visitMethod(access, name, desc, signature, exceptions)) {
			@Override
			public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
				if (DESC_WINDOW.equals(desc)) {
					hasWindow = true;

					// 1. 在@Window之前添加@RequestMapping
					// 2. 当@Window没有指定value()时，取方法的名称作为value()的值
					final AnnotationVisitor theAv = mv.visitAnnotation(DESC_REQUEST_MAPPING, visible);

					return new AnnotationVisitor(ASM5) {
						String value = methodName;

						/**
						 * Get value() of @Window annotation
						 */
						@Override
						public void visit(String name, Object value) {
							if ("value".equals(name) && !"".equals(value.toString()))
								this.value = value.toString();
						}

						/**
						 * Replace @Window value() string with RequestMapping value() string array
						 */
						@Override
						public void visitEnd() {
							AnnotationVisitor av1 = theAv.visitArray("value");
							av1.visit(null, "/" + value);
							av1.visitEnd();
							theAv.visitEnd();

							av1 = mv.visitAnnotation(DESC_WINDOW, true);
							av1.visit("value", value);
							av1.visitEnd();
						}
					};
				} else if (DESC_ACTION.equals(desc)) {
					hasAction = true;

					// 当@Action没有指定value()时，取方法的名称作为value()的值
					return new AnnotationVisitor(ASM5, mv.visitAnnotation(desc, visible)) {
						boolean visited = false;

						@Override
						public void visit(String name, Object value) {
							if ("value".equals(name)) {
								if ("".equals(value.toString()))
									super.visit(name, methodName);
								else
									super.visit(name, value);

								visited = true;
							}
						}

						@Override
						public void visitEnd() {
							if (!visited)
								super.visit("value", methodName);
						}
					};
				}

				return mv.visitAnnotation(desc, visible);
			}
		};
	}
}
