package org.heima.chat.core;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ContextUtil	implements
									ApplicationContextAware
{

	private static ApplicationContext	context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ContextUtil.context = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return context;
	}
}
