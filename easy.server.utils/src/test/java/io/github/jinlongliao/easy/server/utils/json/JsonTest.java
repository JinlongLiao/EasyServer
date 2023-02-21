package io.github.jinlongliao.easy.server.utils.json;

import io.github.jinlongliao.easy.server.utils.json.extra.FastJson2JsonHelper;
import io.github.jinlongliao.easy.server.utils.json.extra.FastJsonJsonHelper;
import io.github.jinlongliao.easy.server.utils.json.extra.JackJsonJsonHelper;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class JsonTest {
    private final FastJsonJsonHelper fastJsonJsonHelper = new FastJsonJsonHelper();
    private final FastJson2JsonHelper fastJsonJsonHelper2 = new FastJson2JsonHelper();
    private final JackJsonJsonHelper jackJsonJsonHelper = new JackJsonJsonHelper();
    private final List<Date> data = Collections.singletonList(new Date());
    private final JsonHelper jsonHelper = new JsonHelper(JsonType.FAST_JSON2, fastJsonJsonHelper, fastJsonJsonHelper2, jackJsonJsonHelper, jackJsonJsonHelper);

    @Test
    public void all() {
        defaultTest();
        fastJsonTest();
        jackJsonTest();
    }

    @Test
    public void defaultTest() {
        String json = jsonHelper.objectToJson(data);
        Assert.assertNotNull(json);
        List<Date> dates = jsonHelper.fromJsonArray(json, Date.class);
        Assert.assertEquals(dates.size(), 1);
    }

    @Test
    public void fastJsonTest() {
        String json = jsonHelper.objectToJson(data, JsonType.FAST_JSON);
        Assert.assertNotNull(json);
        List<Date> dates = jsonHelper.fromJsonArray(json, Date.class, JsonType.FAST_JSON);
        Assert.assertEquals(dates.size(), 1);
    }

    @Test
    public void fastJson2Test() {
        String json = jsonHelper.objectToJson(data, JsonType.FAST_JSON2);
        Assert.assertNotNull(json);
        List<Date> dates = jsonHelper.fromJsonArray(json, Date.class, JsonType.FAST_JSON2);
        Assert.assertEquals(dates.size(), 1);
    }

    @Test
    public void jackJsonTest() {
        String json = jsonHelper.objectToJson(data, JsonType.JACK_JSON);
        Assert.assertNotNull(json);
        List<Date> dates = jsonHelper.fromJsonArray(json, Date.class, JsonType.JACK_JSON);
        Assert.assertEquals(dates.size(), 1);
    }

}
