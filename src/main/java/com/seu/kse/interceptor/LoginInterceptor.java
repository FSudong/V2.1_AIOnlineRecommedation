package com.seu.kse.interceptor;

import com.seu.kse.bean.User;
import com.seu.kse.controller.UserInfoController;
import com.seu.kse.service.impl.UserServiceImpl;
import com.seu.kse.util.Constant;
import com.seu.kse.util.LogUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

public class LoginInterceptor extends HandlerInterceptorAdapter{
    @Resource
    UserServiceImpl userService;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User loginUser = (User) request.getSession().getAttribute(Constant.CURRENT_USER);

        if(loginUser == null){
            String loginCookieUseID = "";
            String loginCookiePassword = "";

            Cookie[] cookies = request.getCookies();
            if(null!=cookies){
                for(Cookie cookie : cookies){
                    //if("/".equals(cookie.getPath())){ //getPath为null
                    if(Constant.COOKIE_USER_ID.equals(cookie.getName())){
                        loginCookieUseID = cookie.getValue();
                    }else if(Constant.COOKIE_USER_PSW.equals(cookie.getName())){
                        loginCookiePassword = cookie.getValue();
                    }
                    //}
                }
                if(!"".equals(loginCookieUseID) && !"".equals(loginCookiePassword)){
                    User user = userService.getUserByID(loginCookieUseID);
                    if(loginCookiePassword.equals(user.getUpassword())){
                        request.getSession().setAttribute(Constant.CURRENT_USER, user);
                    }
                }
            }
        }
        return true;
    }
}
