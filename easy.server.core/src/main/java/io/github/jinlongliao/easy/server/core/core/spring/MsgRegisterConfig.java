package io.github.jinlongliao.easy.server.core.core.spring;


import io.github.jinlongliao.easy.server.core.annotation.LogicController;
import io.github.jinlongliao.easy.server.core.core.MethodParse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

/**
 * 自动初始化
 *
 * @author liaojinlong
 * @since 2022-01-28 11:57
 */
@Deprecated
public class MsgRegisterConfig extends ApplicationObjectSupport implements InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    protected final MethodParse parse = new MethodParse();
    protected final Map<Object, LogicController> metaData = new HashMap<>(32);

    public MethodParse getParse() {
        return parse;
    }

    public Map<Object, LogicController> getMetaData() {
        return metaData;
    }

    private void iniLogic() {
        ListableBeanFactory beanFactory = getApplicationContext();
        Map<String, Object> logicMap = beanFactory.getBeansWithAnnotation(LogicController.class);
        for (Map.Entry<String, Object> entry : logicMap.entrySet()) {
            final Object obj = entry.getValue();
            this.metaData.put(obj, AnnotationUtils.findAnnotation(obj.getClass(), LogicController.class));
            parse.parserLogic(entry.getKey(), obj);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        long start = System.currentTimeMillis();
        this.iniLogic();
        long end = System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            log.debug("cost time {}", end - (start));
        }
    }
}
