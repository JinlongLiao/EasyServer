package io.github.jinlongliao.easy.server.swagger.knife4j.parse;

import io.github.jinlongliao.easy.server.mapper.core.mapstruct2.annotation.Ignore2;
import io.github.jinlongliao.easy.server.swagger.config.ApiConfig;
import io.github.jinlongliao.easy.server.swagger.model.*;
import io.github.jinlongliao.easy.server.servlet.BaseHttpServlet;
import io.github.jinlongliao.easy.server.core.annotation.LogicRequestParam;
import io.github.jinlongliao.easy.server.core.core.MethodParse;
import io.github.jinlongliao.easy.server.core.core.spring.LogicRegisterContext;
import io.github.jinlongliao.easy.server.core.model.LogicModel;
import io.github.jinlongliao.easy.server.core.model.MsgModel;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 默认实现
 *
 * @author: liaojinlong
 * @date: 2022-06-17 17:23
 */
public abstract class AbstractDefaultApiGenerator implements ApiGenerator {
    protected final LogicRegisterContext logicRegisterContext;
    protected final ApiConfig apiConfig;
    protected final List<ExtraApiDocGenerator> extraApiDocGenerators;
    protected Map<Class<?>, Tag> tags;
    protected Set<ApiResource> apiResources;
    protected Map<ApiResource, ApiDoc> apiDocMap;

    public AbstractDefaultApiGenerator(LogicRegisterContext logicRegisterContext,
                                       ApiConfig apiConfig,
                                       List<ExtraApiDocGenerator> extraApiDocGenerators) {
        this.apiConfig = apiConfig;
        this.extraApiDocGenerators = extraApiDocGenerators;
        this.logicRegisterContext = logicRegisterContext;
    }


    protected void parseConfig() {
        MethodParse parse = this.logicRegisterContext.getParse();
        Map<String, LogicModel> logicDefineCache = parse.getLogicDefineCache();
        apiResources = new HashSet<>(2, 1.5f);
        apiDocMap = new HashMap<>(2, 1.5f);
        this.extraApiDoc(1, null, null, this.apiResources, this.apiDocMap);
        this.buildLogicApiDoc(logicDefineCache);
        this.buildServletApiDoc();
        this.extraApiDoc(4, null, null, this.apiResources, this.apiDocMap);
    }

    protected void buildServletApiDoc() {
        ApiResource apiResource = new ApiResource();
        apiResource.setName("servlet");
        apiResource.setSwaggerVersion("2.0");
        apiResource.setLocation(getApiResourceLocation());
        apiResource.setUrl(getApiResourceUrl() + "?group=servlet");
        apiResources.add(apiResource);

        DefaultListableBeanFactory beanFactory = logicRegisterContext.getListableBeanFactory();
        String[] servletBeanNames = beanFactory.getBeanNamesForType(BaseHttpServlet.class);
        Map<String, BaseHttpServlet<?>> servletMap = new HashMap<>(servletBeanNames.length, 1.5f);
        for (String servletBeanName : servletBeanNames) {
            BaseHttpServlet<?> servlet = beanFactory.getBean(servletBeanName, BaseHttpServlet.class);
            servletMap.put(servletBeanName, servlet);
        }
        ApiDoc apiDoc = this.buildServletAipDoc(servletMap);
        apiDocMap.put(apiResource, apiDoc);
        this.extraApiDoc(3, apiResource, apiDoc, this.apiResources, this.apiDocMap);
    }


    /**
     * 基于Logic 形式创建的Api
     *
     * @param logicDefineCache
     */
    protected void buildLogicApiDoc(Map<String, LogicModel> logicDefineCache) {
        ApiResource apiResource = new ApiResource();
        apiResource.setName("logic");
        apiResource.setSwaggerVersion("2.0");
        apiResource.setLocation(getApiResourceLocation());
        apiResource.setUrl(getApiResourceUrl() + "?group=logic");
        apiResources.add(apiResource);
        ApiDoc apiDoc = this.buildLogicAipDoc(logicDefineCache);
        apiDocMap.put(apiResource, apiDoc);
        this.extraApiDoc(2, apiResource, apiDoc, this.apiResources, this.apiDocMap);
    }


    protected ApiDoc buildLogicAipDoc(Map<String, LogicModel> logicDefineCache) {
        ApiDoc apiDoc = new ApiDoc();
        apiDoc.setBasePath(this.getBasePath());
        apiDoc.setDefinitions(this.getDefinitions());
        apiDoc.setHost(this.getHost());
        apiDoc.setInfo(this.getAipInfo());
        apiDoc.setSchemes(this.getSchemes());
        apiDoc.setTags(this.getApiTags(logicDefineCache));
        apiDoc.setPaths(this.getLogicPath(logicDefineCache));
        return apiDoc;
    }

    protected ApiDoc buildServletAipDoc(Map<String, BaseHttpServlet<?>> servletMap) {

        ApiDoc apiDoc = new ApiDoc();
        apiDoc.setBasePath(this.getBasePath());
        apiDoc.setDefinitions(this.getDefinitions());
        apiDoc.setHost(this.getHost());
        apiDoc.setInfo(this.getAipInfo());
        apiDoc.setSchemes(this.getSchemes());
        apiDoc.setTags(Collections.singletonList(new Tag("servlet", "javax.servlet.Servlet")));
        apiDoc.setPaths(this.getServletPath(servletMap));
        return apiDoc;
    }

    protected Map<String, Map<String, Operation>> getServletPath(Map<String, BaseHttpServlet<?>> servletMap) {
        Map<String, Map<String, Operation>> servletPaths = new HashMap<>(servletMap.size(), 1.5f);
        for (Map.Entry<String, BaseHttpServlet<?>> servletEntry : servletMap.entrySet()) {
            final Map<String, Operation> operationHashMap = new HashMap<>(2);
            String key = servletEntry.getKey();
            BaseHttpServlet<?> baseHttpServlet = servletEntry.getValue();
            servletPaths.put(getServletTagUrl(key, baseHttpServlet), operationHashMap);
            Operation operation = getServletOperation(key, baseHttpServlet);
            operation.setTags(Collections.singletonList("servlet"));
            operationHashMap.put("post", operation);
        }
        return servletPaths;
    }


    protected abstract String getServletTagUrl(String key, BaseHttpServlet<?> value);

    protected Map<String, Map<String, Operation>> getLogicPath(Map<String, LogicModel> logicDefineCache) {
        Map<String, Map<String, Operation>> paths = new HashMap<>(logicDefineCache.size(), 1.5f);
        for (Map.Entry<String, LogicModel> modelEntry : logicDefineCache.entrySet()) {
            String key = modelEntry.getKey();
            LogicModel logicModel = modelEntry.getValue();
            final Map<String, Operation> operationHashMap = new HashMap<>(2);
            paths.put(getLogicTagUrl(key, logicModel), operationHashMap);
            Operation operation = getLogicOperation(key, logicModel);
            operation.setTags(Collections.singletonList(this.tags.get(logicModel.getSourceClass()).getName()));
            operationHashMap.put("post", operation);
        }
        return paths;
    }

    protected Operation getLogicOperation(String key, LogicModel logicModel) {
        Operation operation = new Operation();
        final String operationId = key + "-" + logicModel.getDirectMethod().getName();
        operation.setOperationId(operationId);
        operation.setSummary(operationId);
        operation.setDescription(logicModel.getDescription());
        List<Map<String, Object>> parameters = getLogicParameters(key, logicModel.getMsgModel());
        operation.setParameters(parameters);
        operation.setConsumes(Collections.singletonList("application/json"));
        operation.setProduces(Collections.singletonList("application/json"));
        Map<String, Object> responses = new HashMap<>(2);
        Map<String, Object> response = new HashMap<>(2);
        response.put("description", "successful operation");
        responses.put("default", response);
        operation.setResponses(responses);
        return operation;
    }

    protected Operation getServletOperation(String key, BaseHttpServlet<?> baseHttpServlet) {
        Operation operation = new Operation();
        operation.setOperationId(key);
        operation.setSummary(key);
        operation.setDescription(baseHttpServlet.getDescription() + "\n 访问地址："
                + Arrays.toString(baseHttpServlet.supportPath()) + "\n 支持请求方法："
                + (baseHttpServlet.supportMethod()));
        List<Map<String, Object>> parameters = getServletParameters(key, baseHttpServlet);
        operation.setParameters(parameters);
        operation.setConsumes(Collections.singletonList("application/json"));
        operation.setProduces(Collections.singletonList("application/json"));
        Map<String, Object> responses = new HashMap<>(2);
        Map<String, Object> response = new HashMap<>(2);
        response.put("description", "successful operation");
        responses.put("default", response);
        operation.setResponses(responses);
        return operation;
    }

    protected List<Map<String, Object>> getServletParameters(String key, BaseHttpServlet<?> baseHttpServlet) {
        Class<?> genericClass = baseHttpServlet.getGenericClass();
        if (genericClass == null) {
            return Collections.emptyList();
        }
        Field[] declaredFields = genericClass.getDeclaredFields();
        List<Map<String, Object>> parameters = new ArrayList<>(16);
        for (Field field : declaredFields) {
            Ignore2 ignore = field.getAnnotation(Ignore2.class);
            if (ignore != null && ignore.value()) {
                continue;
            }
            if (Modifier.isFinal(field.getModifiers())) {
                continue;
            }
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            buildParameter(key, parameters, field, false);
        }
        return parameters;
    }

    protected List<Map<String, Object>> getLogicParameters(String key, List<MsgModel> msgModels) {
        List<Map<String, Object>> parameters = new ArrayList<>(16);
        final Field[] parentFields = getCommonField();
        Set<String> filedName = new HashSet<>(parentFields.length);
        for (Field field : parentFields) {
            buildParameter(String.valueOf(key), parameters, field, false);
            filedName.add(field.getName());
        }
        for (MsgModel msgModel : msgModels) {
            boolean requestBody = msgModel.isRequestBody();
            if (requestBody) {
                Field[] fields = msgModel.getType().getDeclaredFields();
                for (Field field : fields) {
                    if (!filedName.contains(field.getName())) {
                        Class<?> type = field.getType();
                        final LogicRequestParam annotation = field.getAnnotation(LogicRequestParam.class);
                        int fieldLength = 0;
                        if (annotation != null) {
                            fieldLength = annotation.length();
                            if (annotation.innerParse()) {
                                continue;
                            }
                        }
                        filedName.add(field.getName());
                        buildParameter(String.valueOf(key), fieldLength, parameters, Modifier.PUBLIC, field.getName(), type, false);
                    }
                }
            } else {
                if ((!msgModel.isInnerField()) && (!filedName.contains(msgModel.getParamName()))) {
                    Class<?> type = msgModel.getType();
                    int fieldLength = msgModel.getLength();
                    filedName.add(msgModel.getParamName());
                    buildParameter(String.valueOf(key), fieldLength, parameters, Modifier.PUBLIC, msgModel.getParamName(), type, false);
                }
            }
        }
        return parameters;
    }

    protected Field[] getCommonField() {
        return new Field[0];
    }

    protected void buildParameter(String key, List<Map<String, Object>> parameters, Field field, boolean required) {
        final LogicRequestParam annotation = field.getAnnotation(LogicRequestParam.class);
        int fieldLength = 0;
        if (annotation != null) {
            fieldLength = annotation.length();
        }
        this.buildParameter(key, fieldLength, parameters, field.getModifiers(), field.getName(), field.getType(), required);
    }

    protected abstract void buildParameter(String key, int fieldLength, List<Map<String, Object>> parameters, int modifiers, String fieldName, Class<?> fieldType, boolean required);

    protected abstract String getLogicTagUrl(String key, LogicModel logicModel);


    protected Collection<Tag> getApiTags(Map<String, LogicModel> logicDefineCache) {
        this.tags = new HashMap<>(logicDefineCache.size(), 1.5f);
        for (Map.Entry<String, LogicModel> modelEntry : logicDefineCache.entrySet()) {
            LogicModel logicModel = modelEntry.getValue();
            Class<?> sourceClass = logicModel.getSourceClass();
            if (tags.containsKey(sourceClass)) {
                continue;
            }
            Tag tag = new Tag();
            tag.setName(logicModel.getBeanName());
            tag.setDescription(sourceClass.getName() + "\n\r" + logicModel.getDescription());
            tags.put(sourceClass, tag);
        }
        return tags.values();
    }

    protected String[] getSchemes() {
        return apiConfig.getSchemes();
    }


    protected ApiDocInfo getAipInfo() {
        return apiConfig.getApiDocInfo();
    }

    /**
     * @return /
     */
    protected String getHost() {
        return apiConfig.getHost();
    }

    protected Map<String, Object> getDefinitions() {
        return Collections.emptyMap();
    }

    protected String getBasePath() {
        return apiConfig.getBasePath();
    }

    protected String getApiResourceUrl() {
        return apiConfig.getApiResourceUrl();
    }

    protected String getApiResourceLocation() {
        return apiConfig.getApiResourceLocation();
    }


    @Override
    public ApiDoc generatorApiDoc(ApiResource apiResource) {
        if (apiDocMap == null) {
            parseConfig();
        }
        return apiDocMap.get(apiResource);
    }

    @Override
    public Collection<ApiResource> generatorApiDocApiResource() {
        if (apiResources == null) {
            parseConfig();
        }
        return apiResources;
    }

    @Override
    public void extraApiDoc(int tag, ApiResource apiResource, ApiDoc apiDoc, Set<ApiResource> apiResources, Map<ApiResource, ApiDoc> apiDocMap) {
        for (ExtraApiDocGenerator extraApiDocGenerator : extraApiDocGenerators) {
            extraApiDocGenerator.generatorApiDoc(tag, apiResource, apiDoc, apiResources, apiDocMap);
        }
    }

    @Override
    public void refresh() {
        this.parseConfig();
    }

    public LogicRegisterContext getLogicRegisterContext() {
        return logicRegisterContext;
    }

    public ApiConfig getApiConfig() {
        return apiConfig;
    }

    public List<ExtraApiDocGenerator> getExtraApiDocGenerators() {
        return extraApiDocGenerators;
    }
}
