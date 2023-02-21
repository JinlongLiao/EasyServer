package io.github.jinlongliao.easy.server.core.parser.inner;


import io.github.jinlongliao.easy.server.mapper.utils.CLassUtils;
import io.github.jinlongliao.easy.server.core.model.LogicModel;

import io.github.jinlongliao.easy.server.core.parser.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.invoke.MethodHandles;
import java.util.*;

/**
 * 消息解析规则
 *
 * @author liaojinlong
 * @since 2021/1/22 20:12
 */
public class RequestParseRule extends AbstractRequestParseRule {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public RequestParseRule(LogicModel logicModel) {
        super(logicModel);
    }


    public RequestParseRule(IDefaultValueConverter defaultValueConverter, LogicModel logicModel) {
        super(defaultValueConverter, logicModel);
    }


    /**
     * 获取二进制中消息
     *
     * @return 二进制中消息
     */
    @Override
    public Object[] readHexMsg(IRequestStreamFactory request, IMessageParserCallBack msgHexParserCallBack) {
        Object[] data = new Object[rules.size()];
        Object arg = request.getArg();
        for (int i = 0; i < rules.size(); i++) {
            MeType meType = rules.get(i);
            // 包装参数 skip
            if (meType.isBody()) {
                data[i] = msgHexParserCallBack.parserParamBody(request, meType, arg);
                continue;
            }
            // 内部属性解析
            if (meType.isInnerField()) {
                data[i] = msgHexParserCallBack.parserInnerFiled(request, meType, arg);
                continue;
            }
            if (meType.isCommon()) {
                data[i] = msgHexParserCallBack.parserCommonParam(request, meType, arg);
                continue;
            }
            Class<?> type = meType.getType();
            if (CLassUtils.isStringClass(type)) {
                // 获取变量值
                String sValue = request.readString(meType.getLen());
                if (meType.hasDef()) {
                    if (sValue == null || sValue.isEmpty()) {
                        sValue = String.valueOf(meType.getDefaultValue());
                    }
                }
                data[i] = sValue;
            } else if (type == DynamicString.class) {
                // 获取长度
                int strLen = request.readInt();
                // 获取变量值
                String sValue = request.readString(strLen);
                if (meType.hasDef()) {
                    if (sValue == null || sValue.isEmpty()) {
                        sValue = String.valueOf(meType.getDefaultValue());
                    }
                }
                data[i] = sValue;
            } else if (CLassUtils.isBool(type)) {
                data[i] = request.readBool();
            } else if (CLassUtils.isByte(type)) {
                byte iValue = request.readByte();
                if (meType.hasDef()) {
                    if (iValue == 0) {
                        iValue = (byte) meType.getDefaultValue();
                    }
                }
                data[i] = iValue;
            } else if (CLassUtils.isShort(type)) {
                short iValue = request.readShort();
                if (meType.hasDef()) {
                    if (iValue == 0) {
                        iValue = (short) meType.getDefaultValue();
                    }
                }
                data[i] = iValue;
            } else if (CLassUtils.isInteger(type)) {
                int iValue = request.readInt();
                if (meType.hasDef()) {
                    if (iValue == 0) {
                        iValue = (int) meType.getDefaultValue();
                    }
                }
                data[i] = iValue;
            } else if (CLassUtils.isLong(type)) {
                long lValue = request.readLong();
                if (meType.hasDef()) {
                    if (lValue == 0) {
                        lValue = (long) meType.getDefaultValue();
                    }
                }
                data[i] = lValue;
            } else if (type == List.class) {
                // 目前list里只能支持int类型
                List<Integer> list = new ArrayList<>();
                int size = request.readInt();
                if (size > 0) {
                    for (int j = 0; j < size; j++) {
                        list.add(request.readInt());
                    }
                }
                data[i] = list;
            } else if (type.isArray()) {
                // 当变量为数组类型时，获取该数组长度
                int len = meType.getLen();
                int[] tmp = new int[len];
                for (int j = 0; j < len; j++) {
                    tmp[j] = request.readInt();
                }
                data[i] = tmp;
            }
        }
        return data;
    }

    /**
     * 获取Servlet中消息
     *
     * @return Servlet中的消息
     */
    @Override
    public Object[] readServletMsg(HttpServletRequest request, IMessageParserCallBack msgHexParserCallBack) {
        Object[] data = new Object[rules.size()];
        for (int i = 0; i < rules.size(); i++) {
            MeType meType = rules.get(i);
            // 包转参数
            if (meType.isBody()) {
                data[i] = msgHexParserCallBack.parserParamBody(request, meType, request.getAttribute(REQUEST_ARG));
                continue;
            }
            // 内部属性解析
            if (meType.isInnerField()) {
                data[i] = msgHexParserCallBack.parserInnerFiled(request, meType, request.getAttribute(REQUEST_ARG));
                continue;
            }
            // 公共参数
            if (meType.isCommon()) {
                data[i] = msgHexParserCallBack.parserCommonParam(request, meType, request.getAttribute(REQUEST_ARG));
                continue;
            }

            data[i] = this.converterData(request.getParameter(meType.getParamName()), meType);
        }
        return data;
    }

    /**
     * 数据转换
     *
     * @param parameter
     * @param meType
     * @return /
     */
    protected Object converterData(String parameter, final MeType meType) {
        Class type = meType.getType();
        if (parameter == null) {
            if (meType.hasDef()) {
                return meType.getDefaultValue();
            }
            parameter = "";
        }
        Object value;
        if (CLassUtils.isStringClass(type)) {
            value = parameter;
            if (meType.hasDef()) {
                if (!StringUtils.hasLength(parameter)) {
                    value = meType.getDefaultValue();
                }
            }
        } else if (CLassUtils.isByte(type)) {
            if (parameter.length() == 0) {
                value = 0;
            } else {
                value = Byte.parseByte(parameter);
            }
            if (meType.hasDef()) {
                if ((byte) value == 0) {
                    value = meType.getDefaultValue();
                }
            }
        } else if (CLassUtils.isShort(type)) {
            if (parameter.length() == 0) {
                value = 0;
            } else {
                value = Short.parseShort(parameter);
            }
            if (meType.hasDef()) {
                if ((short) value == 0) {
                    value = meType.getDefaultValue();
                }
            }
        } else if (CLassUtils.isInteger(type)) {
            if (parameter.length() == 0) {
                value = 0;
            } else {
                value = Integer.parseInt(parameter);
            }
            if (meType.hasDef()) {
                if ((int) value == 0) {
                    value = meType.getDefaultValue();
                }
            }
        } else if (CLassUtils.isFloat(type)) {
            if (parameter.length() == 0) {
                value = 0F;
            } else {
                value = Float.parseFloat(parameter);
            }
            if (meType.hasDef()) {
                if ((float) value == 0) {
                    value = meType.getDefaultValue();
                }
            }
        } else if (CLassUtils.isDouble(type)) {
            if (parameter.length() == 0) {
                value = 0D;
            } else {
                value = Double.parseDouble(parameter);
            }
            if (meType.hasDef()) {
                if ((double) value == 0) {
                    value = meType.getDefaultValue();
                }
            }
        } else if (CLassUtils.isLong(type)) {
            if (parameter.length() == 0) {
                value = 0L;
            } else {
                value = Long.parseLong(parameter);
            }
            if (meType.hasDef()) {
                if ((long) value == 0) {
                    value = meType.getDefaultValue();
                }
            }
        } else if (CLassUtils.isCharacter(type)) {
            if (parameter.length() == 0) {
                value = 0;
            } else {
                value = parameter.toCharArray()[0];
            }
            if (meType.hasDef()) {
                if (Character.isWhitespace((char) value)) {
                    value = meType.getDefaultValue();
                }
            }
        } else if (DynamicString.class.equals(type)) {
            if (log.isDebugEnabled()) {
                log.debug("servlet ignore {},read as String", DynamicString.class.getName());
            }
            value = parameter;
            if (meType.hasDef()) {
                if (!StringUtils.hasLength(parameter)) {
                    value = meType.getDefaultValue();
                }
            }
        } else {
            throw new RuntimeException("Can't Converter [" + type.getName() + "] " + parameter);
        }
        return value;
    }
}
