package io.github.jinlongliao.easy.server.swagger.servlet.help;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * @author: liaojinlong
 * @date: 2022-06-22 17:11
 */
public class ApiCleanHelper implements BeanFactoryAware {
    private ApiResourceHelper apiResourceHelper;
    private BeanFactory beanFactory;

    public void reset() {
        this.getApiResourceHelper().cleanCache();
    }

    private ApiResourceHelper getApiResourceHelper() {
        if (apiResourceHelper == null) {
            synchronized (ApiCleanHelper.class) {
                if (apiResourceHelper == null) {
                    this.apiResourceHelper = this.beanFactory.getBean(ApiResourceHelper.class);
                }
            }
        }
        return apiResourceHelper;
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

}
