package io.github.jinlongliao.easy.server.script

import io.github.jinlongliao.easy.server.script.groovy.config.ScriptConfig
import io.github.jinlongliao.easy.server.script.ITestGroovy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.support.ApplicationObjectSupport

class TestGroovy extends ApplicationObjectSupport implements ITestGroovy {
    @Autowired
    private ScriptConfig scriptConfig


    def sayAnyThing() {
        def app = getApplicationContext()
        ''' 
    嘻嘻哈哈😁   
'''
    }

    @Override
    String toString() {
        return "ToString ${sayAnyThing()}"
    }
}
