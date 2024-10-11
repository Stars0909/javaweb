学院：省级示范性软件学院   
题目：《 作业三：Listener练习》   
姓名：张爽   
学号：2200770132   
班级：软工2202   
日期：2024-10-10

@WebListener注解：自动注册了RequestLoggingListener，使其成为Web应用程序的一个监听器，无需在web.xml中显式配置。
```
@WebListener
public class RequestLoggingListener implements ServletRequestListener 
```

requestInitialized方法在请求开始时触发,获取当前时间戳作为请求的开始时间，并将其存储在请求的属性中，这个时间戳稍后将用于计算请求的处理时间。
```
    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
    }
```

requestInitialized方法在请求结束时触发，从请求属性中检索开始时间，并计算处理时间。
```
    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
        Long startTime = (Long) request.getAttribute("startTime");
        if (startTime == null) {
            return;
        }
        long endTime = System.currentTimeMillis();
        long processingTime = endTime - startTime;
```

构建一个日志条目，包含请求的所有相关信息，如时间、客户端IP、请求方法、URI、查询字符串和User-Agent。
```
        String logEntry = String.format("Request Time: %s, Client IP: %s, Method: %s, URI: %s, Query String: %s, User-Agent: %s, Processing Time: %d ms",
                new java.util.Date(),
                request.getRemoteAddr(),
                request.getMethod(),
                request.getRequestURI(),
                request.getQueryString(),
                request.getHeader("User-Agent"),
                processingTime);
```

使用System.out.println来输出日志
```
System.out.println(logEntry);
```

