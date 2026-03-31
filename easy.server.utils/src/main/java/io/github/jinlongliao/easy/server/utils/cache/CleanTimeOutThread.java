package io.github.jinlongliao.easy.server.utils.cache;

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
