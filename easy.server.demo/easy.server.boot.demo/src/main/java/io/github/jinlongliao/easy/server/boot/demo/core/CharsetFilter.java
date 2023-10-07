package io.github.jinlongliao.easy.server.boot.demo.core;

import io.github.jinlongliao.easy.server.servlet.BaseHttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

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
