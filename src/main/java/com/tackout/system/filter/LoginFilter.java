package com.tackout.system.filter;

import com.alibaba.fastjson.JSON;
import com.tackout.system.common.BaseContext;
import com.tackout.system.common.R;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "LoginFilter",urlPatterns = "/*")
public class LoginFilter implements Filter {
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();//路径匹配器

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        String Uri = request.getRequestURI();
        String[] petitions = new String[]{
          "/employee/login",
           "/employee/logout",
                "/user/registry",
                "/user/loginout",
                "/user/login",
                "/backend/**",
                "/front/**",
                "/common/**"
        };

        if (requestURIcheck(petitions,Uri)){
            filterChain.doFilter(request,response);
            return ;
        }

        if(request.getSession().getAttribute("employee")!=null){
            BaseContext.setCurrentId((Long)request.getSession().getAttribute("employee"));
            filterChain.doFilter(request,response);

            return ;
        }

        if(request.getSession().getAttribute("user")!=null){
            BaseContext.setCurrentId((Long)request.getSession().getAttribute("user"));
            filterChain.doFilter(request,response);
            return;
        }
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));//通过输出错误信息让前端拦截器拦截

        return;
    }

    public boolean requestURIcheck(String[] petitions,String URI){
        for (String petition : petitions) {
            boolean IsMatch = PATH_MATCHER.match(petition,URI);
            if(IsMatch) return true;
        }
        return false;
    }

}
