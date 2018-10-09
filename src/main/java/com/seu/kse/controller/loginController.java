package com.seu.kse.controller;

import com.seu.kse.bean.UserTagKey;
import com.seu.kse.service.impl.UserTagService;
import com.seu.kse.util.Constant;
import com.seu.kse.bean.User;
import com.seu.kse.service.IUserService;
import com.seu.kse.service.impl.UserFieldService;
import com.seu.kse.bean.userFieldsKey;
import com.seu.kse.util.LogUtils;
import org.bytedeco.javacpp.presets.opencv_core;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by yaosheng on 2017/5/23.
 */

@Controller
@RequestMapping("/login")
public class loginController {
    @Resource
    private IUserService userService;
    @Resource
    private UserFieldService utService;
    @Resource
    private UserTagService utagService;

    @RequestMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model){
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            LogUtils.error(e.getMessage(),UserInfoController.class);
        }
        String userEmail=request.getParameter("email");
        String psw=request.getParameter("password");
//        String utypestr=request.getParameter("utype");
        if(userEmail!=null && psw !=null){
            User user=userService.verification(userEmail,psw);
            if(user==null){
                model.addAttribute("result","用户输入信息错误");
                return "/login/login";
            }
            Cookie userIDCookie = new Cookie(Constant.COOKIE_USER_ID,user.getId());
            Cookie userPSWCookie = new Cookie(Constant.COOKIE_USER_PSW,user.getUpassword());
            userIDCookie.setMaxAge(60*60*24*30);
            userIDCookie.setPath("/");
            userPSWCookie.setMaxAge(60*60*24*30);
            userPSWCookie.setPath("/");
            response.addCookie(userIDCookie);
            response.addCookie(userPSWCookie);
            session.setAttribute(Constant.CURRENT_USER,user);
        }
        // 获取原网址
        String next = request.getParameter("next");
        if(next == null || next.equals("null")) next = "/";
        return "redirect:".concat(next);
    }

    @RequestMapping("/logout")
    public String logout( HttpServletRequest request, HttpServletResponse response ,HttpSession session){
        User user = (User) request.getSession().getAttribute(Constant.CURRENT_USER);
        Cookie userIDCookie = new Cookie(Constant.COOKIE_USER_ID,user.getId());
        Cookie userPSWCookie = new Cookie(Constant.COOKIE_USER_PSW,user.getUpassword());
        userPSWCookie.setMaxAge(0);
        userPSWCookie.setPath("/");
        userPSWCookie.setMaxAge(0);
        userPSWCookie.setPath("/");
        response.addCookie(userIDCookie);
        response.addCookie(userPSWCookie);
        session.removeAttribute(Constant.CURRENT_USER);
        return "redirect:/login/login.jsp";
    }

    @Transactional
    @RequestMapping("/register")
    public String register(HttpServletRequest request, HttpSession session, Model model){
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            LogUtils.error(e.getMessage(),UserInfoController.class);
        }
        String uname=request.getParameter("name");
        String upsw=request.getParameter("password");
        String email=request.getParameter("email");
        int utype=Integer.parseInt(request.getParameter("utype"));
        int pushNum = Integer.parseInt(request.getParameter("pushnum"));
        int mailfrequency = Integer.parseInt(request.getParameter("mailfrequency"));
        //判断email是否已经注册
        if(userService.isRegisterEmail(email)){
            model.addAttribute("result","邮箱已经注册，请更换邮箱重试");
            return "/login/register";
        }else{
            User user=new User(uname,upsw,email,utype,pushNum,mailfrequency);
            int lines = userService.insertUser(user);
            if(lines<=0){
                model.addAttribute("result","数据库注册失败，请联系管理员");
                return "/login/register";
            }
            model.addAttribute("id",user.getId());
            String[] area_ids = {"00010","00020","00030","00040","00050","00060","00070","00080","00090","00100","00110","00120"};
            String[] EnglishName = {"Artificial Intelligence","Computer graphics","Computer vision","Data mining","Machine learning",
                                    "Natural language processing","Pattern Recognition","World Wide Web","Speech recognition"
                                    ,"Semantic web","Knowledge Graph","information retrieval"};
            for(int i=0; i<area_ids.length;i++){
                String userfield = request.getParameter(area_ids[i]);
                if(userfield!=null && userfield.equals("true")){
                    userFieldsKey ufkey = new userFieldsKey();
                    ufkey.setUid(user.getId());
                    ufkey.setFid(area_ids[i]);
                    utService.insertRecord(ufkey);
                    UserTagKey utag = new UserTagKey();
                    utag.setTagname(EnglishName[i]);
                    utag.setUid(user.getId());
                    utagService.insertUserAndTag(utag);
                }
            }
            session.setAttribute(Constant.CURRENT_USER,user);
        }
        return "redirect:/";
    }

    /**
     * 记录用户的相关领域
     * @param request 请求
     * @return 地址
     */
    @RequestMapping("/insertUserField")
    public String insertUserField(HttpServletRequest request){
        Map<String, String[]> tags = request.getParameterMap();
        String[] tagList=tags.get("tags");
        String uid=request.getParameter("uid");

        userFieldsKey user_fields;
        for(String tag : tagList){
            user_fields = new userFieldsKey();
            user_fields.setFid(tag);
            user_fields.setUid(uid);
            utService.insertRecord(user_fields);

        }

        return  "/login/insertUserField";
    }


}
