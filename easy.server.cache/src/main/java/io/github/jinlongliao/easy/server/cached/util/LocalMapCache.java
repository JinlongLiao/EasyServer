package io.github.jinlongliao.easy.server.cached.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 本地Map 缓存
 *
 * @author liaojinlong
 * @since 2022-02-17 14:43
 */
public class LocalMapCache<T> {
    private static final AtomicInteger index = new AtomicInteger();
    private static final Logger log = LoggerFactory.getLogger(LocalMapCache.class);
    public static final ThreadPoolTaskExecutor TF;
    /**
     * 缓存最大个数
     */
    private final int cacheMaxNumber;
    /**
     * 当前缓存个数
     */
    private int currentSize = 0;
    /**
     * 时间10S
     */
    public static final long TEN_SECOND = 10 * 1000L;
    /**
     * 缓存对象
     */
    private final Map<String, CacheObj<T>> cacheObjMap;
    private static final Set<LocalMapCache> ALL_CACHE = new HashSet<>(32);
    /**
     * 这个记录了缓存使用的最后一次的记录，最近使用的在最前面
     */
    private final List<String> cacheUseLogList = new LinkedList<>();
    /**
     * 清理过期缓存是否在运行
     */
    private static volatile Boolean CLEAN_THREAD_IS_RUN = false;

    static {
        TF = new ThreadPoolTaskExecutor();
        TF.setCorePoolSize(1);
        TF.setMaxPoolSize(2);
        TF.setThreadNamePrefix("LocalMapCache thread");
        TF.afterPropertiesSet();
    }

    public LocalMapCache(int cacheMaxNumber) {
        this.cacheMaxNumber = cacheMaxNumber;
        this.cacheObjMap = new ConcurrentHashMap<>(cacheMaxNumber);
        ALL_CACHE.add(this);
    }

    public int getCurrentSize() {
        return this.currentSize;
    }

    /**
     * 设置缓存
     */
    public void setCache(String cacheKey, T cacheValue, long cacheTime) {
        long ttlTime;
        if (cacheTime <= 0L) {
            if (cacheTime == -1L) {
                ttlTime = -1L;
            } else {
                return;
            }
        } else {
            ttlTime = System.currentTimeMillis() + cacheTime;
        }
        checkSize();
        saveCacheUseLog(cacheKey);
        currentSize = currentSize + 1;
        CacheObj cacheObj = new CacheObj(cacheValue, ttlTime);
        cacheObjMap.put(cacheKey, cacheObj);
        if (log.isTraceEnabled()) {
            log.trace("have set key :{}", cacheKey);
        }
    }

    /**
     * 设置缓存
     */
    public void setCache(String cacheKey, T cacheValue) {
        setCache(cacheKey, cacheValue, -1L);
    }

    /**
     * 获取缓存
     */
    public T getCache(String cacheKey) {
        startCleanThread();
        if (checkCache(cacheKey)) {
            saveCacheUseLog(cacheKey);
            return cacheObjMap.get(cacheKey).getCacheValue();
        }
        return null;
    }

    public boolean isExist(String cacheKey) {
        return checkCache(cacheKey);
    }

    /**
     * 删除所有缓存
     */
    public void clear() {
        log.info("have clean all key !");
        cacheObjMap.clear();
        currentSize = 0;
    }

    /**
     * 删除某个缓存
     */
    public void deleteCache(String cacheKey) {
        Object cacheValue = cacheObjMap.remove(cacheKey);
        if (cacheValue != null) {
            if (log.isTraceEnabled()) {
                log.trace("have delete key :{}", cacheKey);
            }
            cacheUseLogList.remove(cacheKey);
            currentSize = currentSize - 1;
        }
    }

    /**
     * 判断缓存在不在,过没过期
     */
    private boolean checkCache(String cacheKey) {
        CacheObj cacheObj = cacheObjMap.get(cacheKey);
        if (cacheObj == null) {
            return false;
        }
        if (cacheObj.getTtlTime() == -1L) {
            return true;
        }
        if (cacheObj.getTtlTime() < System.currentTimeMillis()) {
            deleteCache(cacheKey);
            return false;
        }
        return true;
    }

    /**
     * 删除最近最久未使用的缓存
     */
    private void deleteLRU() {
        if (log.isTraceEnabled()) {
            log.trace("delete Least recently used run!");
        }
        String cacheKey = null;
        if (cacheUseLogList.size() >= cacheMaxNumber - 10) {
            cacheKey = cacheUseLogList.remove(cacheUseLogList.size() - 1);
        }
        if (cacheKey != null) {
            deleteCache(cacheKey);
        }
    }

    /**
     * 删除过期的缓存
     */
    static void deleteTimeOut() {
        deleteTimeOut((true));
    }

    /**
     * 删除过期的缓存
     */
    static void deleteTimeOut(boolean tryAg) {
        if (log.isTraceEnabled()) {
            log.trace("delete time out run!");
        }
        List<String> deleteKeyList = new LinkedList<>();
        try {
            for (LocalMapCache cache : ALL_CACHE) {
                Map<String, CacheObj> cacheObjMap = cache.cacheObjMap;
                for (Map.Entry<String, CacheObj> entry : cacheObjMap.entrySet()) {
                    long ttlTime = entry.getValue().getTtlTime();
                    if (ttlTime < System.currentTimeMillis() && ttlTime != -1L) {
                        deleteKeyList.add(entry.getKey());
                    }
                }
                for (String deleteKey : deleteKeyList) {
                    cache.deleteCache(deleteKey);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            if (tryAg) {
                deleteTimeOut((false));
            }
        }

    }

    /**
     * 检查大小
     * 当当前大小如果已经达到最大大小
     * 首先删除过期缓存，如果过期缓存删除过后还是达到最大缓存数目
     * 删除最久未使用缓存
     */
    private void checkSize() {
        if (currentSize >= cacheMaxNumber) {
            deleteTimeOut();
        }
        if (currentSize >= cacheMaxNumber) {
            deleteLRU();
        }
    }

    /**
     * 保存缓存的使用记录
     */
    private synchronized void saveCacheUseLog(String cacheKey) {
        synchronized (cacheUseLogList) {
            cacheUseLogList.remove(cacheKey);
            cacheUseLogList.add(0, cacheKey);
        }
    }

    /**
     * 设置清理线程的运行状态为正在运行
     */
    static void setCleanThreadRun() {
        CLEAN_THREAD_IS_RUN = true;
    }

    /**
     * 开启清理过期缓存的线程
     */
    private static void startCleanThread() {
        if (!CLEAN_THREAD_IS_RUN) {
            synchronized (LocalMapCache.class) {
                if (CLEAN_THREAD_IS_RUN) {
                    return;
                }
                index.incrementAndGet();
                ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, TF);
                CleanTimeOutThread cleanTimeOutThread = new CleanTimeOutThread();
                executor.scheduleWithFixedDelay(cleanTimeOutThread, TEN_SECOND, TEN_SECOND, TimeUnit.SECONDS);
                setCleanThreadRun();
            }
        }
    }

}

class CacheObj<T> {
    /**
     * 缓存对象
     */
    private T CacheValue;
    /**
     * 缓存过期时间
     */
    private long ttlTime;

    CacheObj(T cacheValue, long ttlTime) {
        CacheValue = cacheValue;
        this.ttlTime = ttlTime;
    }

    T getCacheValue() {
        return CacheValue;
    }

    long getTtlTime() {
        return ttlTime;
    }

    @Override
    public String toString() {
        return "CacheObj {" +
                "CacheValue = " + CacheValue +
                ", ttlTime = " + ttlTime +
                '}';
    }
}

/**
 * 每一分钟清理一次过期缓存
 */
class CleanTimeOutThread implements Runnable {

    @Override
    public void run() {
        LocalMapCache.setCleanThreadRun();
        LocalMapCache.deleteTimeOut();
    }
}
