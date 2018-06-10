package org.heima.chat.core.jdbc;

import java.io.Serializable;
import java.util.List;

public interface IDao<POJO> {

	public Serializable save(POJO entity);

	public void update(POJO entity);

	public void delete(Serializable id);

	public POJO findById(Serializable id);

	public List<POJO> findByHQL(String hql, Object... params);

	public POJO findOneByHQL(String hql, Object... params);

	public List<POJO> findByField(Class<POJO> clazz, String fieldName, Object fieldValue);

	public List<POJO> findByFields(Class<POJO> clazz, String[] fieldNames, Object[] fieldValues);

	public POJO findOneByField(Class<POJO> clazz, String fieldName, Object fieldValue);
}
