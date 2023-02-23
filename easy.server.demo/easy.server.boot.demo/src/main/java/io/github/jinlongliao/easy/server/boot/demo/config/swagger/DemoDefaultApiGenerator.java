package io.github.jinlongliao.easy.server.boot.demo.config.swagger;

import io.github.jinlongliao.easy.server.core.core.spring.LogicRegisterContext;
import io.github.jinlongliao.easy.server.core.model.LogicModel;
import io.github.jinlongliao.easy.server.mapper.utils.CLassUtils;
import io.github.jinlongliao.easy.server.servlet.BaseHttpServlet;
import io.github.jinlongliao.easy.server.swagger.config.ApiConfig;
import io.github.jinlongliao.easy.server.swagger.knife4j.parse.AbstractDefaultApiGenerator;
import io.github.jinlongliao.easy.server.swagger.knife4j.parse.ExtraApiDocGenerator;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: liaojinlong
 * @date: 2022-06-20 11:20
 */
public class DemoDefaultApiGenerator extends AbstractDefaultApiGenerator {
    public DemoDefaultApiGenerator(LogicRegisterContext logicRegisterContext,
                                   ApiConfig apiConfig,
                                   List<ExtraApiDocGenerator> extraApiDocGenerators) {
        super(logicRegisterContext, apiConfig, extraApiDocGenerators);
    }


    @Override
    protected void buildParameter(String key,
                                  int fieldLength,
                                  List<Map<String, Object>> parameters,
                                  int modifiers, String fieldName,
                                  Class<?> fieldType,
                                  boolean required) {
        Map<String, Object> param = new HashMap<>(8);
        if ("logicId".equalsIgnoreCase(fieldName)) {
            return;
        }
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

    @Override
    protected String getLogicTagUrl(String key, LogicModel logicModel) {
        return apiConfig.getProxyAccessPath() + "?logicId=" + key;
    }

    @Override
    protected String getServletTagUrl(String key, BaseHttpServlet<?> value) {
        return apiConfig.getProxyAccessPath() + "/" + value.supportPath()[0] + "?servlet=" + key;
    }
}
