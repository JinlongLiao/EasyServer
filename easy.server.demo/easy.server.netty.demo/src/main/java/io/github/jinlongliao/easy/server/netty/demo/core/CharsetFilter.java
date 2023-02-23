package io.github.jinlongliao.easy.server.netty.demo.core;

import io.github.jinlongliao.easy.server.servlet.BaseHttpFilter;
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
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        chain.doFilter(request, response);
    }

    @Override
    public boolean isAsync() {
        return true;
    }
}
