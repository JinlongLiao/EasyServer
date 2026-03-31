package io.github.jinlongliao.easy.server.utils.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 本地Map 缓存
 *
 * @author liaojinlong
 * @since 2022-02-17 14:43
 */
public class LocalMapCache {
    private static final AtomicInteger index = new AtomicInteger();
    private static final Logger log = LoggerFactory.getLogger(LocalMapCache.class);
    private static final ThreadPoolExecutor TF;
    private static final ScheduledThreadPoolExecutor SCHEDULED_THREAD_POOL_EXECUTOR;

    /**
     * 缓存最大个数
     */
    private final int cacheMaxNumber;
    /**
     * 当前缓存个数
     */
    private int currentSize = 0;
    /**
     * 时间一分钟
     */
    public static final long ONE_MINUTE = 60 * 1000L;
    /**
     * 缓存对象
     */
    private final Map<String, CacheObj<?>> cacheObjMap;
    private static final Map<String, ScheduledFuture<?>> SCHEDULED_FUTURE_CONCURRENT_CACHE = new ConcurrentHashMap<>(8);
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
        TF = new ThreadPoolExecutor(1, 8, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1024), runnable -> {
            Thread thread = new Thread(runnable, "EASY_SERVER_CACHE_" + (System.currentTimeMillis() / 1000));
            thread.setDaemon(true);
            return thread;
        }, (r, e) -> {
            log.warn("tread pool queue is full :{},tread num:{}", e.getQueue().size(), e.getActiveCount());
            if (!e.isShutdown()) {
                e.getQueue().poll();
                e.execute(r);
            }
        });


        SCHEDULED_THREAD_POOL_EXECUTOR = new ScheduledThreadPoolExecutor(4, runnable -> {
            Thread thread = new Thread(runnable, "EASY_SERVER_SCHEDULED_" + (System.currentTimeMillis() / 1000));
            thread.setDaemon(true);
            return thread;
        }, (r, e) -> {
            log.warn("scheduled tread pool queue is full :{},tread num:{}", e.getQueue().size(), e.getActiveCount());
            if (!e.isShutdown()) {
                e.getQueue().poll();
                e.execute(r);
            }
        });

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
     * 设置缓存 毫秒
     *
     * @param cacheKey   /
     * @param cacheValue /
     * @param cacheTime  毫秒
     */
    public void setCache(String cacheKey, Object cacheValue, long cacheTime) {
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
        CacheObj<?> cacheObj = new CacheObj<>(cacheValue, ttlTime);
        cacheObjMap.put(cacheKey, cacheObj);
        if (log.isTraceEnabled()) {
            log.trace("have set key :{}", cacheKey);
        }
    }

    /**
     * 设置缓存
     */
    public void setCache(String cacheKey, Object cacheValue) {
        setCache(cacheKey, cacheValue, -1L);
    }

    /**
     * 获取缓存
     */
    public <T> T getCache(String cacheKey) {
        startCleanThread();
        if (checkCache(cacheKey)) {
            saveCacheUseLog(cacheKey);
            CacheObj<?> cacheObj = cacheObjMap.get(cacheKey);
            return (T) cacheObj.getCacheValue();
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
            currentSize = currentSize - 1;
        }
    }

    /**
     * 判断缓存在不在,过没过期
     */
    private boolean checkCache(String cacheKey) {
        CacheObj<?> cacheObj = cacheObjMap.get(cacheKey);
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
        if (log.isTraceEnabled()) {
            log.trace("delete time out run!");
        }
        List<String> deleteKeyList = new LinkedList<>();
        for (LocalMapCache cache : ALL_CACHE) {
            Map<String, CacheObj<?>> cacheObjMap = cache.cacheObjMap;
            for (Map.Entry<String, CacheObj<?>> entry : cacheObjMap.entrySet()) {
                long ttlTime = entry.getValue().getTtlTime();
                if (ttlTime < System.currentTimeMillis() && ttlTime != -1L) {
                    deleteKeyList.add(entry.getKey());
                }
            }
            for (String deleteKey : deleteKeyList) {
                cache.deleteCache(deleteKey);
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
                CleanTimeOutThread cleanTimeOutThread = new CleanTimeOutThread();
                SCHEDULED_THREAD_POOL_EXECUTOR.scheduleWithFixedDelay(cleanTimeOutThread, ONE_MINUTE, ONE_MINUTE, TimeUnit.SECONDS);
                setCleanThreadRun();
            }
        }
    }
    //region 异步任务执行或定时任务

    public static void addScheduledTask(String taskName, Runnable task, long delay, TimeUnit unit) {
        ScheduledFuture<?> future = SCHEDULED_THREAD_POOL_EXECUTOR.scheduleAtFixedRate(task, 0, delay, unit);
        ScheduledFuture<?> scheduledFuture = SCHEDULED_FUTURE_CONCURRENT_CACHE.put(taskName, future);
        if (Objects.nonNull(scheduledFuture)) {
            scheduledFuture.cancel(true);
        }
    }

    public static Future<?> addAsync(Runnable runnable) {
        return TF.submit(runnable);
    }

    public static <T> Future<T> addAsync(Callable<T> runnable) {
        return TF.submit(runnable);
    }
    //endregion
}


