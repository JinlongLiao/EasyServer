package io.github.jinlongliao.easy.server.utils.cache;

/**
 * @author: liaojinlong
 * @date: 2024-03-15 16:20
 */
public class CacheObj<T> {
	private final String name;
	/**
	 * 缓存对象
	 */
	private final T cacheValue;
	/**
	 * 缓存过期时间
	 */
	private final long ttlTime;

	public CacheObj(String name, T cacheValue, long ttlTime) {
		this.name = name;
		this.cacheValue = cacheValue;
		this.ttlTime = ttlTime;
	}

	public CacheObj(T cacheValue, long ttlTime) {
		this("", cacheValue, ttlTime);
	}

	public T getCacheValue() {
		return cacheValue;
	}

	public long getTtlTime() {
		return ttlTime;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "CacheObj{" +
			"name='" + name + '\'' +
			", cacheValue=" + cacheValue +
			", ttlTime=" + ttlTime +
			'}';
	}
}
