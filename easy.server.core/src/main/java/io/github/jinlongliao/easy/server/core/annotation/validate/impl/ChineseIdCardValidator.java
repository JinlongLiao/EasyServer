package io.github.jinlongliao.easy.server.core.annotation.validate.impl;

import io.github.jinlongliao.easy.server.core.annotation.validate.ChineseIdCarNo;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 中华人民共和国身份证号码的构成规则
 * <p></p>
 * 1.18位身份证号码</br><p></p>
 * <li>第1~2位数字：所在省（直辖市、自治区）的代码；</li>
 * <li> 第3~4位：所在地级市（自治州）的代码；</li>
 * <li>第5~6位：所在区（县，自治县，县级市）的代码；</li>
 * <li>第7~14位：出生的年月日；</li>
 * <li>第15~16位：所在地派出所的代码；</li>
 * <li>第17位：表示性别的代码，男性为奇数，女性为偶数；</li>
 * <li> 第18位：校验位，用来校验身份证号码的正确性。一般是0~9其中的一个数字，或者X。X规范的是大写，但小写也可以。</li>
 * <p></p>
 * 2.15位身份证号码<br><p></p>
 * <li>第1~2位数字：所在省（直辖市、自治区）的代码；</li>
 * <li>第3~4位：所在地级市（自治州）的代码；</li>
 * <li>第5~6位：所在区（县，自治县，县级市）的代码；</li>
 * <li>第7~12位：出生的年月日；15位的年份是简写的。比如：18位的年份是1970年，而15位的就是70年。</li>
 * <li>第13~14位：所在地派出所的代码；</li>
 * <li>第15位：表示性别的代码，男性为奇数，女性为偶数；</li>
 * <p>
 * 15位身份证没有校验位。
 */

/**
 * 身份证 校验
 *
 * @author liaojinlong
 * @since 2021/3/18 16:51
 */
public class ChineseIdCardValidator implements ConstraintValidator<ChineseIdCarNo, String> {

    /**
     * 18位身份证号码正则
     */
    private static Pattern ID_CARD_18_REGEX = Pattern.compile("^[1-9](\\d{5})(19|20)(\\d{2})((0[1-9])|10|11|12)(([0-2][1-9])|10|20|30|31)(\\d{3})(\\d|X|x)$");
    /**
     * 15位身份证号码正则
     */
    private static Pattern ID_CARD_15_REGEX = Pattern.compile("^[1-9](\\d{5})(\\d{2})((0[1-9])|10|11|12)(([0-2][1-9])|10|20|30|31)(\\d{3})$");
    /**
     * 平年日期正则
     */
    private static Pattern ORDINARY_YEAR_REGEX = Pattern.compile("(((0[13578])|10|12)(([0-2][1-9])|10|20|30|31)|(((0[469])|11)(([0-2][1-9])|10|20|30))|(02(([0-2][1-8])|09|19|10|20)))");
    /**
     * 闰年日期正则
     */
    private static Pattern LEAP_YEAR_REGEX = Pattern.compile("(((0[13578])|10|12)(([0-2][1-9])|10|20|30|31)|(((0[469])|11)(([0-2][1-9])|10|20|30))|(02(([0-2][1-9])|10|20)))");
    /**
     * 1-17位相乘因子数组
     */
    private final static int[] FACTOR = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    /**
     * 18位随机码数组
     */
    private final static char[] RANDOM = "10X98765432".toCharArray();
    private static final int EIGHTEEN = 18;
    private static final int FIFTEEN = 18;

    @Override
    public void initialize(ChineseIdCarNo constraintAnnotation) {

    }

    @Override
    public boolean isValid(String idCardNo, ConstraintValidatorContext context) {
        if (idCardNo == null) {
            return false;
        }
        int length = idCardNo.length();
        if (!(length == EIGHTEEN || length == FIFTEEN)) {
            return false;
        }

        boolean flag1 = false;
        boolean flag2 = false;
        boolean flag3 = true;

        // 判断第18位校验值
        if (length == EIGHTEEN) {
            flag1 = regexValidate(ID_CARD_18_REGEX, idCardNo);
            // 判断是不是闰年，并匹配日期是否规范
            int year = Integer.parseInt(idCardNo.substring(6, 10));
            if (year % 4 == 0) {
                flag2 = regexValidate(LEAP_YEAR_REGEX, idCardNo.substring(10, 14));
            } else {
                flag2 = regexValidate(ORDINARY_YEAR_REGEX, idCardNo.substring(10, 14));
            }
            char[] charArray = idCardNo.toCharArray();
            char idCardLast = charArray[17];
            // 计算1-17位与相应因子乘积之和
            int total = 0;
            for (int i = 0; i < 17; i++) {
                total += Character.getNumericValue(charArray[i]) * FACTOR[i];
            }
            // 判断随机码是否相等
            char ch = RANDOM[total % 11];
            if (!String.valueOf(ch).equalsIgnoreCase(String.valueOf(idCardLast))) {
                flag3 = false;
            }
        } else if (length == FIFTEEN) {
            flag1 = regexValidate(ID_CARD_15_REGEX, idCardNo);
            flag2 = regexValidate(ORDINARY_YEAR_REGEX, idCardNo.substring(8, 12));
        }
        if (flag1 && flag2 && flag3) {
            return true;
        } else {
            return false;
        }
    }

    private boolean regexValidate(Pattern pattern, String value) {
        Matcher matcher = pattern.matcher(value);
        return matcher.find();
    }
}
