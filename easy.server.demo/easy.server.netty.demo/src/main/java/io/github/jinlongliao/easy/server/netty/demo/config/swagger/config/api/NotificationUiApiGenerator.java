package io.github.jinlongliao.easy.server.netty.demo.config.swagger.config.api;


import io.github.jinlongliao.easy.server.core.core.spring.LogicRegisterContext;
import io.github.jinlongliao.easy.server.core.model.LogicModel;
import io.github.jinlongliao.easy.server.mapper.utils.CLassUtils;
import io.github.jinlongliao.easy.server.netty.demo.logic.request.IRequest;
import io.github.jinlongliao.easy.server.servlet.BaseHttpServlet;
import io.github.jinlongliao.easy.server.swagger.config.ApiConfig;
import io.github.jinlongliao.easy.server.swagger.knife4j.parse.AbstractDefaultApiGenerator;
import io.github.jinlongliao.easy.server.swagger.knife4j.parse.ExtraApiDocGenerator;
import io.github.jinlongliao.easy.server.swagger.model.ApiDoc;
import io.github.jinlongliao.easy.server.swagger.model.ApiResource;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author: liaojinlong
 * @date: 2022-06-21 11:04
 */
public class NotificationUiApiGenerator extends AbstractDefaultApiGenerator {
    private final Field[] parentFields = IRequest.class.getDeclaredFields();

    public NotificationUiApiGenerator(LogicRegisterContext logicRegisterContext, ApiConfig apiConfig, List<ExtraApiDocGenerator> extraApiDocGenerators) {
        super(logicRegisterContext, apiConfig, extraApiDocGenerators);
    }


    protected void parseConfig() {
        super.parseConfig();
        Collection<ApiResource> apiResources = this.generatorApiDocApiResource();
        Optional<ApiResource> logic = apiResources.stream().filter(n -> n.getName().equals("logic")).findFirst();
        if (logic.isPresent()) {
            ApiResource apiResource = logic.get();
            ApiDoc apiDoc = this.generatorApiDoc(apiResource);
            System.out.printf("");
        }

    }

    @Override
    protected List<Map<String, Object>> getServletParameters(String key, BaseHttpServlet baseHttpServlet) {
        return super.getServletParameters(key, baseHttpServlet);
    }

    @Override
    protected Field[] getCommonField() {
        return parentFields;
    }

    @Override
    protected void buildParameter(String key, int fieldLength, List<Map<String, Object>> parameters, int modifiers, String fieldName, Class<?> fieldType, boolean required) {
        Map<String, Object> param = new HashMap<>(8);
        if ("logicId".equalsIgnoreCase(fieldName)) {
            return;
        }
        addDefaultVal(param, fieldName);

        parameters.add(param);
        param.put("in", "formData");
        param.put("required", required);
        param.put("name", fieldName);
        param.put("description", fieldName);
        if (CLassUtils.isInteger(fieldType)) {
            param.put("format", "int32");
            param.put("type", "integer");
            param.put("description", fieldName + " int 4 byte");
        } else if (fieldType == String.class) {
            param.put("format", "string");
            param.put("type", "string");
            param.put("description", fieldName + " string " + fieldLength + " byte");
        } else if (CLassUtils.isFloat(fieldType)) {
            param.put("format", "float");
            param.put("type", "number");
            param.put("description", fieldName + " int 6 byte");
        } else if (CLassUtils.isDouble(fieldType)) {
            param.put("format", "double");
            param.put("type", "number");
            param.put("description", fieldName + " int 8 byte");
        } else if (CLassUtils.isLong(fieldType)) {
            param.put("format", "int64");
            param.put("type", "integer");
            param.put("description", fieldName + " int 8 byte");
        } else if (CLassUtils.isShort(fieldType)) {
            param.put("format", "int32");
            param.put("type", "integer");
            param.put("description", fieldName + " int 2 byte");
        } else if (CLassUtils.isBool(fieldType)) {
            param.put("format", null);
            param.put("type", "boolean");
            param.put("description", fieldName + " int 1 byte");
        } else if (CLassUtils.isCharacter(fieldType)) {
            param.put("format", "string");
            param.put("type", "string");
            param.put("description", fieldName + " int 1 byte");
        } else if (CLassUtils.isByte(fieldType)) {
            param.put("format", "byte");
            param.put("type", "string");
            param.put("description", fieldName + " int 1 byte");
        } else if (fieldType == Date.class) {
            param.put("format", "date-time");
            param.put("type", "string");
        } else if (fieldType == List.class) {
            //param.put("format", "array");
            //param.put("type", "array");
            param.put("format", "int32");
            param.put("type", "integer");
            param.put("description", fieldName + " int 4 byte");
        } else if (fieldType.isArray()) {
            //param.put("format", "array");
            //param.put("type", "array");
            param.put("format", "int32");
            param.put("type", "integer");
            param.put("description", fieldName + " int 4 byte");
        }
    }


    private void addDefaultVal(Map<String, Object> param, String name) {
        Object value = null;
        if ("version".equals(name)) {
            value = "3";
        }
        if ("userToken".equals(name)) {
            value = "1111";
        }
        if ("extraFlag".equals(name)) {
            value = "1";
        }

        if ("userId".equals(name)) {
            value = 13794;
        }

        param.put("value", value);
        param.put("default", value);
    }

    @Override
    protected String getLogicTagUrl(String key, LogicModel logicModel) {
        return apiConfig.getProxyAccessPath() + "?logicId=" + key;
    }

    @Override
    protected String getServletTagUrl(String key, BaseHttpServlet<?> value) {
        return apiConfig.getProxyAccessPath() + "/" + value.supportPath()[0] + "?servlet=" + key;
    }
}
