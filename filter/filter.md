学院：省级示范性软件学院    
题目：《 作业二：Filter练习》    
姓名：张爽    
学号：2200770132    
班级：软工2202    
日期：2024-10-09

@WebFilter 注解：配置过滤器，使其应用于所有 URL 路径（"/*"）
```
@WebFilter("/*")
```

排除列表，不需要登录就能访问的路径
```
    private static final List<String> STATIC_EXTENSIONS = Arrays.asList(
            ".css", ".js", ".jpg", ".png", ".gif", ".ico", ".jsp"
    );
```

// 检查请求的资源是否是静态资源
```
        boolean isStativcResource = STATIC_EXTENSIONS.stream().anyMatch(ext -> {
            System.out.println("ext: " + ext);
            return request.getRequestURI().contains(ext);
        });
```

// 如果在排除列表，则直接放行；如果不是，则检查用户是否已登录
```
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
```

// 调用父类的init方法进行初始化
```
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }
```

// 调用父类的destroy方法用于销毁过滤器
```
 @Override
    public void destroy() {
        Filter.super.destroy();
    }
```