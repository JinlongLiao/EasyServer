package io.github.jinlongliao.easy.server.extend.test.response;


import io.github.jinlongliao.easy.server.mapper.core.mapstruct.converter.InnerConverter;
import io.github.jinlongliao.easy.server.core.model.LogicModel;
import io.github.jinlongliao.easy.server.core.parser.IDefaultValueConverter;
import io.github.jinlongliao.easy.server.core.parser.IMessageParserCallBack;
import io.github.jinlongliao.easy.server.core.parser.IRequestStreamFactory;
import io.github.jinlongliao.easy.server.core.parser.inner.AbstractRequestParseRule;


import jakarta.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 消息解析规则
 *
 * @author liaojinlong
 * @since 2021/1/22 20:12
 */
public class TestRequestParseRule extends AbstractRequestParseRule {
    public TestRequestParseRule(LogicModel logicModel) {
        super(logicModel);
    }

    public TestRequestParseRule(IDefaultValueConverter defaultValueConverter, LogicModel logicModel) {
        super(defaultValueConverter, logicModel);
    }


    /**
     * 获取二进制中消息
     *
     * @return 二进制中消息
     */
    @Override
    public Object[] readHexMsg(IRequestStreamFactory request, IMessageParserCallBack msgHexParserCallBack, Object... args) {
        Object[] data = new Object[11];
        String readDynamicString = request.readString(request.readInt());
        if (this.rules.get(0).hasDef()) {
            if (readDynamicString == null || readDynamicString.isEmpty()) {
                readDynamicString = this.rules.get(1).getDefaultValue().toString();
            }
        }
        data[0] = readDynamicString;


        String readString = request.readString(100);
        if (this.rules.get(1).hasDef()) {
            if (readString == null || readString.isEmpty()) {
                readString = this.rules.get(1).getDefaultValue().toString();
            }
        }
        data[1] = readString;

        int readInt = request.readInt();

        if (this.rules.get(2).hasDef()) {
            if (readInt == 0) {
                readInt = (int) this.rules.get(2).getDefaultValue();
            }
        }
        data[2] = readInt;

        long readLong = request.readLong();

        if (this.rules.get(3).hasDef()) {
            if (readLong == 0) {
                readLong = (long) this.rules.get(3).getDefaultValue();
            }
        }
        data[3] = readLong;

        int size = request.readInt();
        List<Integer> ints = new ArrayList<>(size);
        while (size-- > 0) {
            ints.add(request.readInt());
        }
        data[4] = ints;
        int size2 = request.readInt();
        int[] arr = new int[size2];
        int index = 0;
        while (size2 > index) {
            arr[index++] = request.readInt();
        }
        data[5] = arr;
        int size3 = request.readInt();
        Integer[] arr2 = new Integer[size3];
        int index2 = 0;
        while (size3 > index2) {
            arr2[index2++] = request.readInt();
        }
        data[6] = arr2;

        data[7] = msgHexParserCallBack.parserCommonParam(request, this.rules.get(7), args);
        data[8] = msgHexParserCallBack.parserParamBody(request, this.rules.get(8), args);
        data[9] = msgHexParserCallBack.parserInnerFiled(request, this.rules.get(9), args);
        data[10] = request.readBool();
        return data;
    }

    /**
     * 获取Servlet中消息
     *
     * @return Servlet中的消息
     */
    @Override
    public Object[] readServletMsg(HttpServletRequest request, IMessageParserCallBack msgHexParserCallBack, Object... args) {
        Object[] data = new Object[10];
        //        包转参数
        Object xxx = request.getParameter("xxx");
        if (xxx == null || ((String) xxx).isEmpty()) {
            if (this.rules.get(0).hasDef()) {
                xxx = this.rules.get(0).getDefaultValue();
            }
        }
        data[0] = InnerConverter.getByte2(xxx);

        data[1] = msgHexParserCallBack.parserParamBody(request, this.rules.get(1), args);

        //                 内部属性解析
        data[2] = msgHexParserCallBack.parserInnerFiled(request, this.rules.get(2), args);

        //                 公共参数
        data[3] = msgHexParserCallBack.parserCommonParam(request, this.rules.get(3), args);

        String[] xxxes = request.getParameterValues("xxx");
        List<Integer> ints = new ArrayList<>(xxxes.length);
        int index = 0;
        while (xxxes.length > index) {
            ints.add(InnerConverter.getInt2(xxxes[index++]));
        }
        data[4] = ints;

        String[] xxxe = request.getParameterValues("xxx");
        int[] array = new int[xxxe.length];
        int index2 = 0;
        while (xxxe.length > index2) {
            array[index2] = (InnerConverter.getInt2(xxxe[index2++]));
        }
        data[5] = array;

        data[6] = InnerConverter.getT(Date.class, this.rules.get(6), request.getParameter("xxx"));
        Object a = 989;
        data[7] = InnerConverter.getT(Date.class, "__extra__", a);
        return data;
    }


}
