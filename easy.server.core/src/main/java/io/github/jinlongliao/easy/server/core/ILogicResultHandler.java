package io.github.jinlongliao.easy.server.core;

/**
 * 业务操作结果解析类
 *
 * @author liaojinlong
 * @since 2021/1/24 19:18
 */
public interface ILogicResultHandler {
    /**
     * 正常操作解析

     */
    default String logicResultHandler(String logicId, Object[] args, Object obj) throws Exception {
        return this.logicResultHandler(logicId, obj);
    }

    /**
     * 正常操作解析
     *
     * @param logicId
     * @param obj
     * @return 解析后的结果
     */
    String logicResultHandler(String  logicId, Object obj) throws Exception;

    /**
     * 异常信息解析
     *
     * @param logicId
     * @param exception
     * @return 解析后的结果
     */
   default String logicExceptionHandler(String logicId, Object[] args,Object obj,  Exception exception) throws Exception {
        return this.logicExceptionHandler(logicId, exception);
    }
    /**
     * 异常信息解析
     *
     * @param logicId
     * @param exception
     * @return 解析后的结果
     */
    String logicExceptionHandler(String logicId, Exception exception) throws Exception;

}
