package com.seu.kse.util;

import com.seu.kse.bean.User;
import com.seu.kse.dao.UserMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by yaosheng on 2017/6/1.
 */
public class Utils {
    private static ApplicationContext acUtils = new ClassPathXmlApplicationContext("classpath:spring-mybatis.xml");
    private static UserMapper userDaoUtils = (UserMapper) acUtils.getBean("userMapper");
    private static int limits = 3;
    private static int cur_num =0;
    public static User testLogin(HttpSession session, Model model){
        User login_user = (User)session.getAttribute(Constant.CURRENT_USER);
        model.addAttribute(Constant.CURRENT_USER,login_user);
        return login_user;
    }

    public static boolean testConnect(){
        if(cur_num>limits) {
            cur_num = 0;
            return false;
        }
        boolean flag = true;
        try{
            User user = userDaoUtils.testConnect();
            if(user == null){
                flag = false;
            }
        }catch (Exception e){
            flag = false;
        }
        if(!flag) {
            cur_num ++;
            testConnect();
        }
        cur_num = 0;
        return flag;
    }
    public static String getUrl(HttpServletRequest req) {
        String reqUrl = req.getRequestURL().toString();
        String queryString = req.getQueryString();
        if (queryString != null) {
            reqUrl += "?" + queryString;
        }
        if ("POST".equalsIgnoreCase(req.getMethod())) {
            reqUrl = req.getHeader("Referer");
        }
        return reqUrl;
    }

    public static String getDate(int i){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,i);
        Date date = calendar.getTime();
        return new SimpleDateFormat("yyyyMMdd").format(date);

    }

}
