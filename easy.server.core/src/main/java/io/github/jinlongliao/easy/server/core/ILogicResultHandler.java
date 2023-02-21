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
    default String logicResultHandler(Integer msgType, Object[] args, Object obj) throws Exception {
        return this.logicResultHandler(msgType, obj);
    }

    /**
     * 正常操作解析
     *
     * @param msgType
     * @param obj
     * @return 解析后的结果
     */
    String logicResultHandler(Integer msgType, Object obj) throws Exception;

    /**
     * 异常信息解析
     *
     * @param msgType
     * @param exception
     * @return 解析后的结果
     */
   default String logicExceptionHandler(Integer msgType, Object[] args,Object obj,  Exception exception) throws Exception {
        return this.logicExceptionHandler(msgType, exception);
    }
    /**
     * 异常信息解析
     *
     * @param msgType
     * @param exception
     * @return 解析后的结果
     */
    String logicExceptionHandler(Integer msgType, Exception exception) throws Exception;

}
