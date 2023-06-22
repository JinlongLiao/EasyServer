package groovy

import io.github.jinlongliao.easy.server.netty.demo.logic.param.UserModel
import io.github.jinlongliao.easy.server.netty.demo.logic.service.IGroovyService
import io.github.jinlongliao.easy.server.utils.json.JsonHelper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author liaojinlong
 * @since 2022/3/26 19:35
 */
class GroovyService implements IGroovyService {
    private final Logger log = LoggerFactory.getLogger(GroovyService.getClass())
    @Autowired
    private JsonHelper jsonHelper


    @Override
    Object getTest(UserModel userModel) {
        def userId = userModel.userId
        def ts = System.currentTimeMillis()
        def format = """{"userId":"${userId}","timestamp":${ts}}"""
        log.info("Model: {} return:{}", userModel, format.toString());
        return jsonHelper.fromJson(format.toString(), Map.class)
    }
}
