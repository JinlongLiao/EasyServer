package io.github.jinlongliao.easy.server.demo.core;

import io.github.jinlongliao.easy.server.servlet.BaseHttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @author: liaojinlong
 * @date: 2022-06-28 16:33
 */
@Component
public class CharsetFilter extends BaseHttpFilter {
    @Override
    public String[] supportPath() {
        return new String[]{"/*"};
    }

    @Override
    public boolean doLogicFilter(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        return true;
    }

    @Override
    public boolean isAsync() {
        return true;
    }
}
