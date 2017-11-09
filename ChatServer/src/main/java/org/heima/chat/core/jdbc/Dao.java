package org.heima.chat.core.jdbc;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class Dao<POJO>	extends
						HibernateDaoSupport	implements
											IDao<POJO>
{
	private Class<POJO>	clazz;

	@SuppressWarnings("unchecked")
	public Dao() {
		ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
		clazz = (Class<POJO>) type.getActualTypeArguments()[0];
	}

	@Override
	public Serializable save(POJO entity) {
		//		Session session = getSessionFactory().getCurrentSession();
		//		session.save(entity);

		HibernateTemplate template = getHibernateTemplate();
		return template.save(entity);
	}

	@Override
	public void update(POJO entity) {
		//		Session session = getSessionFactory().getCurrentSession();
		//		session.update(entity);

		HibernateTemplate template = getHibernateTemplate();
		template.update(entity);
	}

	@Override
	public void delete(Serializable id) {
		//		Session session = getSessionFactory().getCurrentSession();
		//		session.delete(findById(id));

		HibernateTemplate template = getHibernateTemplate();
		template.delete(findById(id));
	}

	@SuppressWarnings("unchecked")
	@Override
	public POJO findById(Serializable id) {
		//		Session session = getSessionFactory().getCurrentSession();
		//		return (POJO) session.load(clazz, id);

		HibernateTemplate template = getHibernateTemplate();
		return template.get(clazz, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<POJO> findByHQL(String hql, Object... params) {
		//		Session session = getSessionFactory().getCurrentSession();
		//		Query query = session.createQuery(hql);
		//		for (int i = 0; i < params.length; i++) {
		//			query.setParameter(0, params[i]);
		//		}
		//		return query.list();

		HibernateTemplate template = getHibernateTemplate();
		return template.find(hql, params);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<POJO> findByField(Class<POJO> clazz, String fieldName, Object fieldValue) {
		//		Session session = getSessionFactory().getCurrentSession();
		//		Criteria criteria = session.createCriteria(clazz);
		//		criteria.add(Restrictions.eq(fieldName, fieldValue));
		//
		//		return (List<POJO>) criteria.list();

		HibernateTemplate template = getHibernateTemplate();
		DetachedCriteria criteria = DetachedCriteria.forClass(clazz);
		criteria.add(Restrictions.eq(fieldName, fieldValue));
		return template.findByCriteria(criteria);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<POJO> findByFields(Class<POJO> clazz, String[] fieldNames, Object[] fieldValues) {
		HibernateTemplate template = getHibernateTemplate();
		DetachedCriteria criteria = DetachedCriteria.forClass(clazz);
		for (int i = 0; i < fieldValues.length; i++) {
			criteria.add(Restrictions.eq(fieldNames[i], fieldValues[i]));
		}
		return template.findByCriteria(criteria);
	}

	@Override
	public POJO findOneByHQL(String hql, Object... params) {
		List<POJO> list = findByHQL(hql, params);
		if (list != null && list.size() > 0) { return list.get(0); }
		return null;
	}

	@Override
	public POJO findOneByField(Class<POJO> clazz, String fieldName, Object fieldValue) {
		List<POJO> list = findByField(clazz, fieldName, fieldValue);
		if (list != null && list.size() > 0) { return list.get(0); }
		return null;
	}

}
