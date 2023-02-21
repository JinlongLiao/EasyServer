package io.github.jinlongliao.easy.server.core.pattern;

import io.github.jinlongliao.easy.server.core.annotation.validate.impl.ChineseIdCardValidator;
import org.junit.Assert;
import org.junit.Test;

public class ChineseIdCardPatternTest {
    @Test
    public void test1() {
        ChineseIdCardValidator validator = new ChineseIdCardValidator();
        boolean valid = validator.isValid("411423199412225076", null);
        boolean valid2 = validator.isValid("411423199412225077", null);
        Assert.assertTrue("error", valid);
        Assert.assertFalse("error", valid2);
    }
}
