package com.github.cryboy007.utils;


import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CollectionUtil {
	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	public static boolean isEmpty(Map<?, ?> map) {
		return map == null || map.isEmpty();
	}

	public static <T> boolean isEmpty(T[] t) {
		return t == null || t.length == 0;
	}

	public static boolean isNotEmpty(Collection<?> collection) {
		return !isEmpty(collection);
	}

	public static boolean isNotEmpty(Map<?, ?> map) {
		return !isEmpty(map);
	}

	public static <T> boolean isNotEmpty(T[] t) {
		return !isEmpty(t);
	}

	/**
	 * list转数组
	 * 
	 * @param list
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> T[] toArray(List<T> list, Class<T> clazz) {
		T[] arry0 = (T[]) Array.newInstance(clazz, 0);
		if (list == null || list.size() == 0) {
			return arry0;
		} else {
			return list.toArray(arry0);
		}
	}

	public static <T> String toInClause(T[] array) {
		if (null == array || array.length == 0) {
			return null;
		} else {
			StringBuilder sBuilder = new StringBuilder();
			for (T e : array) {
				if (null == e)
					continue;
				sBuilder.append("'").append(e).append("'").append(",");
			}

			return StringUtils.substringBeforeLast(sBuilder.toString(), ",");
		}

	}

	public static <T> String toInClause(Collection<T> collection) {
		if (null == collection || collection.size() == 0) {
			return null;
		} else {
			StringBuilder sBuilder = new StringBuilder();
			for (T e : collection) {
				if (null == e)
					continue;
				sBuilder.append("'").append(e).append("'").append(",");
			}

			return StringUtils.substringBeforeLast(sBuilder.toString(), ",");
		}

	}

	public static <T> String toInClause(Collection<T> collection, InnerHandler<T> handler) {
		if (null == collection || collection.size() == 0) {
			return null;
		} else {
			StringBuilder sBuilder = new StringBuilder();
			for (T e : collection) {
				if (null == e)
					continue;
				sBuilder.append("'").append(handler.handler(e)).append("'").append(",");
			}

			return StringUtils.substringBeforeLast(sBuilder.toString(), ",");
		}

	}

	public interface InnerHandler<T> {
		public String handler(T e);
	}

	public static interface InnerHandler2<T> {
		public Boolean handler(T e);
	}

	public static <T> T filter4Unique(Collection<T> collection, InnerHandler2<T> handler2) {
		if (null == collection || collection.size() == 0) {
			return null;
		} else {
			for (T e : collection) {
				if (handler2.handler(e)) {
					return e;
				}
			}

			return null;
		}
	}

	public static <T> List<T> filter4Collection(Collection<T> collection, InnerHandler2<T> handler2) {
		if (null == collection || collection.size() == 0) {
			return null;
		} else {
			List<T> list = new ArrayList<T>(collection.size());
			for (T e : collection) {
				if (handler2.handler(e)) {
					list.add(e);
				}
			}

			return list;
		}
	}

}
