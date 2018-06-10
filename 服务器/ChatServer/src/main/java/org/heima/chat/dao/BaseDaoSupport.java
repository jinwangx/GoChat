package org.heima.chat.dao;

import javax.annotation.Resource;

import org.heima.chat.core.jdbc.Dao;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

public class BaseDaoSupport<POJO>	extends
									Dao<POJO>
{

	//	@Resource(name = "hibernateTemplete")
	//	public void setTemplate(HibernateTemplate template) {
	//		super.setHibernateTemplate(template);
	//	}

	@Resource(name = "sessionFactory")
	public void setFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

}
