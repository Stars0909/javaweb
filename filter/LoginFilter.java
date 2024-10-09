package com.gzu.filterlistenerdemo;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter("/*")
public class LoginFilter implements Filter {

    private static final List<String> STATIC_EXTENSIONS = Arrays.asList(
            ".css", ".js", ".jpg", ".png", ".gif", ".ico", ".jsp"
    );

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        boolean isStativcResource = STATIC_EXTENSIONS.stream().anyMatch(ext -> {
            System.out.println("ext: " + ext);
            return request.getRequestURI().contains(ext);
        });
        if (isStativcResource) {
            filterChain.doFilter(servletRequest, servletResponse);
        }else{
            String username = (String) request.getSession().getAttribute("user");
            if("".equals(username) || username==null) {
                response.sendRedirect(request.getContextPath() + "/login");
            }else{
                filterChain.doFilter(servletRequest, servletResponse);
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}