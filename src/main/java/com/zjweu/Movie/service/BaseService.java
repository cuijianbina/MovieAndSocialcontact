package com.zjweu.Movie.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zjweu.Movie.common.Page;
import com.zjweu.Movie.util.CMyString;

@Service
@Transactional
public class BaseService {

	@Autowired
	private EntityManager entityManager;

	public void save(Object obj) {
		entityManager.persist(obj);
	}
	
	
	/**
	 * Description:保存或修改
	 *
	 * @param obj
	 * @author cui.jianbin
	 * @date 2019年4月1日 下午5:32:58
	 * @version 1.0
	 */
	public void update(Object obj) {
		entityManager.merge(obj);
	}

	/**
	 * Description:删除指定对象
	 *
	 * @param obj
	 * @author cui.jianbin
	 * @date 2019年4月1日 下午5:33:22
	 * @version 1.0
	 */
	public void delete(Object obj) {
		entityManager.remove(entityManager.merge(obj));
	}

	/**
	 * 用于查询单个对象 Description: 用于查询单个对象 <BR>
	 * 
	 * @param classes
	 *            查询对象Class eq:如AppUser.class
	 * @param id
	 *            对象主键ID
	 * @return Object
	 */
	public <T> T findById(Class<T> clazz, Integer id) {
		return entityManager.find(clazz, id);
	}

	/**
	 * Description:分页
	 *
	 * @param _sSql
	 * @param _params
	 * @param _nStartIndex
	 * @param _nPageSize
	 * @return
	 * @author cui.jianbin
	 * @date 2019年4月1日 下午5:33:50
	 * @version 1.0
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> find(String _sSql, List<Object> _params, int _nStartIndex, int _nPageSize) {
		Query query = entityManager.createQuery(_sSql);
		if (_params != null && !_params.isEmpty()) {
			for (int i = 0; i < _params.size(); i++) {
				query.setParameter(i + 1, _params.get(i));
			}
		}
		if (_nStartIndex >= 0 && _nPageSize > 0) {
			query.setFirstResult(_nStartIndex);
			query.setMaxResults(_nPageSize);
		}
		return query.getResultList();
	}

	/**
	 * 根据对象名，条件获取对象信息 Description: 根据对象名，条件获取对象信息,支持单个条件查询<BR>
	 * 
	 * @param sFrom
	 *            String 查询对象名称 eq:不包含from关键字，如AppUser
	 * @param sWhere
	 *            String 条件 eq:不包含where关键字，如userName = ?
	 * @param param
	 *            条件值
	 * @return Object 符合条件的对象
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public <T> T findObject(String sFrom, String sWhere, Object param) throws Exception {
		List<Object> dataList = find("", sFrom, sWhere, "", Collections.singletonList(param));
		if (dataList != null && dataList.size() > 0) {
			return (T) dataList.iterator().next();
		}
		return null;
	}

	public <T> List<T> find(String sSelectFields, String sFrom, String sWhere, String sOrder, List<Object> parameters)
			throws Exception {
		return find(sSelectFields, sFrom, sWhere, sOrder, -1, -1, parameters);
	}

	/**
	 * 通用分页查询 Description: 通用分页查询 <BR>
	 * 此方法用于封装支持where条件，查询部分字段，排序方式，分页操作。 注意： 1.当需要查询部分字段时，需要在参数封装时实例化一个对象。
	 * 如select new AppUser(param1,param2,...) from
	 * AppUser，将返回对象的集合，前提是对象有支持该属性列表的构造方法。 或者select new
	 * Map/List(param1,param2,...) from AppUser,将返回存有list/或map的集合对象。
	 * 2.当没有设置字段列表selectfields时，将查询所有字段值。 3.当设置where条件后，必须输入参数列表parameters。
	 * 4.当设置startpage和pagesize的值大于-1时，才会根据分页查询数据，否则返回所有对象集合。
	 * 5.当设置排序方式order时，才会按字段值排序。
	 * 
	 * @param sSelectFields
	 *            String 查询字段 eq:不包含select关键字，如userId,userName，或new
	 *            list(userName)或new map(userName)。当查询全部字段时可以输入空值或字符串。
	 * @param sFrom
	 *            String 查询对象名称 eq:不包含from关键字，如AppUser
	 * @param sWhere
	 *            String 条件 eq:不包含where关键字，如userName = ? and trueName = ?
	 *            模糊查询时，需要在参数值中加入"%",where语句中不可以加类似username like
	 *            '%?%',正确方式为：username like ?
	 * @param sOrder
	 *            String 排序 eq:不包含order by 关键字，如userId asc/desc
	 * @param nStartPage
	 *            int 数据开始数
	 * @param nPageSize
	 *            int 每页几条
	 * @param parameters
	 *            List 条件参数值
	 * @return List 符合查询条件的对象集合
	 * @version 1.0
	 * @throws Exception
	 */
	public <T> List<T> find(String sSelectFields, String sFrom, String sWhere, String sOrder, int nStartIndex,
			int nPageSize, List<Object> parameters) throws Exception {
		StringBuffer hql = new StringBuffer();
		if (!CMyString.isEmpty(sSelectFields) && !"*".equals(sSelectFields)) {
			hql.append("select ").append(sSelectFields);
		}
		if (!CMyString.isEmpty(sFrom)) {
			hql.append(" from ").append(sFrom);
		} else {
			throw new Exception("要查询的对象名称没有输入！");
		}
		if (!CMyString.isEmpty(sWhere)) {
			hql.append(" where ").append(sWhere);
		}
		if (!CMyString.isEmpty(sOrder)) {
			hql.append(" order by ").append(sOrder);
		}
		if ((!CMyString.isEmpty(sWhere) && (sWhere.indexOf("?") > 0 || sWhere.indexOf(":") > 0))
				&& parameters == null) {
			throw new Exception("要查询的对象的参数列表没有输入！");
		}
		if (CMyString.isEmpty(sWhere) && parameters != null && parameters.size() > 0) {
			throw new Exception("要查询的对象的字段列表没有输入！");
		}
		if (nStartIndex > -1 && nPageSize > -1) {
			return find(hql.toString(), parameters, nStartIndex, nPageSize);
		}
		return find(hql.toString(), parameters);
	}

	public <T> List<T> find(String _sSql, List<Object> _params) {
		return find(_sSql, _params, -1, -1);
	}
	
	
	/**
	 * 
	 * Description: 获取分页列表 <BR>
	 * 
	 * @author liu.zhuan
	 * @date 2014-3-12 下午02:40:40
	 * @param sSelectFields
	 *            查询字段 当查询全部字段时可以输入空值或字符串。
	 * @param sFrom
	 *            查询对象
	 * @param sWhere
	 *            条件
	 * @param sOrder
	 *            排序
	 * @param nStartPage
	 *            开始页数
	 * @param nPageSize
	 *            每页几条
	 * @param parameters
	 *            条件参数值
	 * @version 1.0
	 * @throws Exception
	 */
	public <T> Page<T> findPage(String sSelectFields, String sFrom, String sWhere, String sOrder, int nStartPage,
			int nPageSize, List<Object> paramters) throws Exception {
		int totalResults = count(sFrom, sWhere, paramters);
		Page<T> page = new Page<T>(nStartPage, nPageSize, totalResults);
		List<T> data = find(sSelectFields, sFrom, sWhere, sOrder, page.getStartIndex(), nPageSize, paramters);
		page.setLdata(data);
		return page;
	}

	/**
	 * Description: 分页查询（适用于关联查询时需要指定查询数据总数的字段） <BR>
	 * 
	 * @param sSelectFields
	 *            字段列表
	 * @param sCountSelectField
	 *            查询数据总数的字段
	 * @param sFrom
	 *            实体名称
	 * @param sWhere
	 *            查询条件
	 * @param sOrder
	 *            排序
	 * @param nStartPage
	 *            开始页码
	 * @param nPageSize
	 *            每页几条
	 * @param paramters
	 *            查询参数
	 * @return
	 * @throws Exception
	 * @version 1.0
	 */
	public <T> Page<T> findPageForJoinQuery(String sSelectFields, String sCountSelectField, String sFrom, String sWhere,
			String sOrder, int nStartPage, int nPageSize, List<Object> paramters) throws Exception {
		int totalResults = count("count(" + sCountSelectField + ")", sFrom, sWhere, paramters);
		Page<T> page = new Page<T>(nStartPage, nPageSize, totalResults);
		List<T> data = find(sSelectFields, sFrom, sWhere, sOrder, page.getStartIndex(), nPageSize, paramters);
		page.setLdata(data);
		return page;
	}

	/**
	 * 用于查询对象记录数,支持多个条件查询
	 * 
	 * @Description: 用于查询对象记录数,支持多个条件查询<BR>
	 * @param sFrom
	 *            String 查询对象名称 eq:不包含from关键字，如AppUser
	 * @param sWhere
	 *            String 条件 eq:不包含where关键字，如userName = ? and trueName = ?
	 * @param params
	 *            参数值
	 * @return Integer 记录数
	 */
	public Integer count(String sFrom, String sWhere, List<Object> params) throws Exception {
		return ((Long) find("count(*)", sFrom, sWhere, "", -1, -1, params).listIterator().next()).intValue();
	}

	/**
	 * Description: 查询对象记录数,支持多个条件查询 <BR>
	 * 
	 * @param sCountSelect
	 *            可指定count(字段)，用于连表查询时指定字段
	 * @param sFrom
	 *            表名
	 * @param sWhere
	 *            where条件
	 * @param params
	 *            sql参数
	 * @return Integer数量
	 */
	public Integer count(String sCountSelect, String sFrom, String sWhere, List<Object> params) throws Exception {
		return ((Long) find(sCountSelect, sFrom, sWhere, "", -1, -1, params).listIterator().next()).intValue();
	}

	/**
	 * 用于查询对象记录数,单个条件查询
	 * 
	 * @Description: 此方法只支持单个条件查询<BR>
	 * @param sFrom
	 *            String 查询对象名称 eq:不包含from关键字，如AppUser
	 * @param sWhere
	 *            String 条件 eq:不包含where关键字，如userName = ?
	 * @param param
	 *            参数值
	 * @return Integer 记录数
	 */
	public Integer count(String sFrom, String sWhere, Object param) throws Exception {
		return count(sFrom, sWhere, Collections.singletonList(param));
	}
	
	/**
	 * Description:原生sql计数
	 *
	 * @param sql
	 * @param params
	 * @return
	 * @author cui.jianbin  
	 * @date 2019年5月1日  下午5:23:10
	 * @version 1.0
	 */
	public int executeCountSql(String sql, List<Object> params) {
		Query query = entityManager.createNativeQuery(sql);
		if (params != null && params.size() > 0) {
			for (int i = 0; i < params.size(); i++) {
				query.setParameter(i + 1, params.get(i));
			}
		}
		query.setMaxResults(1);
		return Integer.valueOf(query.getResultList().iterator().next().toString());
	}

	/**
	 * Description: 执行原生sql（带参数） <BR>
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public int executeBaseSql(String sql, List<Object> params) {
		Query query = entityManager.createNativeQuery(sql);
		if (params != null && params.size() > 0) {
			for (int i = 0; i < params.size(); i++) {
				query.setParameter(i + 1, params.get(i));
			}
		}
		return query.executeUpdate();
	}

	/**
	 * Description:原生sql查询<BR>
	 * 
	 * @param sql
	 *            原生sql语句
	 * @param params
	 *            参数列表
	 * @param firstResult
	 *            设置从第几条记录开始读取
	 * @param maxResults
	 *            设置最大记录数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> executeQuerySql(String sql, List<Object> params, int firstResult, int maxResults) {
		Query query = entityManager.createNativeQuery(sql);
		if (params != null && params.size() > 0) {
			for (int i = 0; i < params.size(); i++) {
				query.setParameter(i + 1, params.get(i));
			}
		}
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		if (firstResult >= 0 && maxResults >= 0) {
			query.setFirstResult(firstResult);
			query.setMaxResults(maxResults);
		}
		return query.getResultList();
	}

	/**
	 * 判断数据在表中是否有重复 Description: 判断数据在表中是否有重复,支持多条件查询<BR>
	 * 
	 * @param sFrom
	 *            表对象 eq：AppUser
	 * @param sWhere
	 *            查询条件 eq：username = ? and userId = ?
	 * @param paramters
	 *            查询条件的值
	 * @return boolean true表示数据存在，false表示不存在
	 */
	public boolean existData(String sFrom, String sWhere, List<Object> paramters) throws Exception {
		int userCount = count(sFrom, sWhere, paramters);
		if (userCount > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * Description: 判断数据在表中是否有重复，单条件查询<BR>
	 * 
	 * @param sFrom
	 *            表对象 eq：AppUser
	 * @param sWhere
	 *            查询条件 eq：username = ?
	 * @param paramter
	 *            查询条件的值
	 * @return boolean true表示数据存在，false表示不存在
	 */
	public boolean existData(String sFrom, String sWhere, Object paramter) throws Exception {
		int userCount = count(sFrom, sWhere, paramter);
		if (userCount > 0) {
			return true;
		}
		return false;
	}
}
