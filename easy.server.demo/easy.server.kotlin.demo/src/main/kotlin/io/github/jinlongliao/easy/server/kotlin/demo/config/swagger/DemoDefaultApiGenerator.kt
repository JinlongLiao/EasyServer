package io.github.jinlongliao.easy.server.kotlin.demo.config.swagger

import io.github.jinlongliao.easy.server.core.core.spring.LogicRegisterContext
import io.github.jinlongliao.easy.server.core.model.LogicModel
import io.github.jinlongliao.easy.server.mapper.utils.CLassUtils
import io.github.jinlongliao.easy.server.servlet.BaseHttpServlet
import io.github.jinlongliao.easy.server.swagger.config.ApiConfig
import io.github.jinlongliao.easy.server.swagger.knife4j.parse.AbstractDefaultApiGenerator
import io.github.jinlongliao.easy.server.swagger.knife4j.parse.ExtraApiDocGenerator
import java.util.*

/**
 * @author: liaojinlong
 * @date: 2022-06-20 11:20
 */
class DemoDefaultApiGenerator(
    logicRegisterContext: LogicRegisterContext?,
    apiConfig: ApiConfig?,
    extraApiDocGenerators: List<ExtraApiDocGenerator?>?
) : AbstractDefaultApiGenerator(logicRegisterContext, apiConfig, extraApiDocGenerators) {
    override fun buildParameter(
        key: String,
        fieldLength: Int,
        parameters: MutableList<MutableMap<String, Any>>,
        modifiers: Int, fieldName: String,
        fieldType: Class<*>,
        required: Boolean
    ) {
        val param: MutableMap<String, Any> = HashMap(8)
        if ("logicId".equals(fieldName, ignoreCase = true)) {
            return
        }
        parameters.add(param)
        param["in"] = "formData"
        param["required"] = required
        param["name"] = fieldName
        param["description"] = fieldName
        if (CLassUtils.isInteger(fieldType)) {
            param["format"] = "int32"
            param["type"] = "integer"
            param["description"] = "$fieldName int 4 byte"
        } else if (fieldType == String::class.java) {
            param["format"] = "string"
            param["type"] = "string"
            param["description"] = "$fieldName string $fieldLength byte"
        } else if (CLassUtils.isFloat(fieldType)) {
            param["format"] = "float"
            param["type"] = "number"
            param["description"] = "$fieldName int 6 byte"
        } else if (CLassUtils.isDouble(fieldType)) {
            param["format"] = "double"
            param["type"] = "number"
            param["description"] = "$fieldName int 8 byte"
        } else if (CLassUtils.isLong(fieldType)) {
            param["format"] = "int64"
            param["type"] = "integer"
            param["description"] = "$fieldName int 8 byte"
        } else if (CLassUtils.isShort(fieldType)) {
            param["format"] = "int32"
            param["type"] = "integer"
            param["description"] = "$fieldName int 2 byte"
        } else if (CLassUtils.isBool(fieldType)) {
            param["format"] = ""
            param["type"] = "boolean"
            param["description"] = "$fieldName int 1 byte"
        } else if (CLassUtils.isCharacter(fieldType)) {
            param["format"] = "string"
            param["type"] = "string"
            param["description"] = "$fieldName int 1 byte"
        } else if (CLassUtils.isByte(fieldType)) {
            param["format"] = "byte"
            param["type"] = "string"
            param["description"] = "$fieldName int 1 byte"
        } else if (fieldType == Date::class.java) {
            param["format"] = "date-time"
            param["type"] = "string"
        } else if (fieldType == MutableList::class.java) {
            //param.put("format", "array");
            //param.put("type", "array");
            param["format"] = "int32"
            param["type"] = "integer"
            param["description"] = "$fieldName int 4 byte"
        } else if (fieldType.isArray) {
            //param.put("format", "array");
            //param.put("type", "array");
            param["format"] = "int32"
            param["type"] = "integer"
            param["description"] = "$fieldName int 4 byte"
        }
    }

    override fun getLogicTagUrl(key: String, logicModel: LogicModel): String {
        return apiConfig.proxyAccessPath + "?logicId=" + key
    }

    override fun getServletTagUrl(key: String, value: BaseHttpServlet<*>): String {
        return apiConfig.proxyAccessPath + "/" + value.supportPath()[0] + "?servlet=" + key
    }
}
