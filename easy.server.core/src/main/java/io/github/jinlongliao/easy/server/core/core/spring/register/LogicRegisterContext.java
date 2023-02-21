package io.github.jinlongliao.easy.server.core.core.spring.register;

import io.github.jinlongliao.easy.server.core.annotation.LogicController;
import io.github.jinlongliao.easy.server.core.core.MethodParse;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.util.*;

/**
 * @author liaojinlong
 */
public class LogicRegisterContext implements AutoCloseable, InitializingBean {
    private final DefaultListableBeanFactory listableBeanFactory;
    protected final MethodParse parse;
    protected final Map<Object, LogicController> metaData = new HashMap<>(32);

    public LogicRegisterContext(DefaultListableBeanFactory listableBeanFactory) {
        this.listableBeanFactory = listableBeanFactory;
        Map<String, MethodPostProcess> beansOfType = listableBeanFactory.getBeansOfType(MethodPostProcess.class);
        this.parse = new MethodParse(new ArrayList<>(beansOfType.values()));
    }

    public MethodParse getParse() {
        return parse;
    }

    public Map<Object, LogicController> getMetaData() {
        return metaData;
    }

    public DefaultListableBeanFactory getListableBeanFactory() {
        return listableBeanFactory;
    }

    @Override
    public void close() throws Exception {
        this.metaData.clear();
        this.parse.close();
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
