/*
 *	History				Who				What
 *  2016年5月10日			Administrator			Created.
 */
package com.zjweu.Movie.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Title: 传播评价系统 <BR>
 * 
 */
public class CtlStatus implements Map<String, Object> {
	private Map<String, Object> properties;

	private CtlStatus() {
		// TODO Auto-generated constructor stub
		properties = new HashMap<String, Object>();
	}

	public static CtlStatus success() {
		return new CtlStatus().put("success", true);
	}

	public static CtlStatus failed() {
		return new CtlStatus().put("success", false);
	}

	public static CtlStatus success(String _sMessage) {
		return success().put("message", _sMessage);
	}

	public static CtlStatus success(Object _data) {
		return success().put("data", _data);
	}

	public static CtlStatus failed(String _sMessage) {
		return failed().put("message", _sMessage);
	}

	public boolean isSuccess() {
		return (boolean) this.properties.get("success");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#size()
	 */
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return this.properties.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return this.properties.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	@Override
	public boolean containsKey(Object key) {
		// TODO Auto-generated method stub
		return this.properties.containsKey(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	@Override
	public boolean containsValue(Object value) {
		// TODO Auto-generated method stub
		return this.properties.containsValue(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#get(java.lang.Object)
	 */
	@Override
	public Object get(Object key) {
		// TODO Auto-generated method stub
		return this.properties.get(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public CtlStatus put(String key, Object value) {
		// TODO Auto-generated method stub
		this.properties.put(key, value);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	@Override
	public Object remove(Object key) {
		// TODO Auto-generated method stub
		return this.properties.remove(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		// TODO Auto-generated method stub
		this.properties.putAll(m);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#clear()
	 */
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		this.properties.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#keySet()
	 */
	@Override
	public Set<String> keySet() {
		// TODO Auto-generated method stub
		return this.properties.keySet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#values()
	 */
	@Override
	public Collection<Object> values() {
		// TODO Auto-generated method stub
		return this.properties.values();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#entrySet()
	 */
	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		// TODO Auto-generated method stub
		return this.properties.entrySet();
	}

	public Object getData() {
		return this.properties.get("data");
	}
}
