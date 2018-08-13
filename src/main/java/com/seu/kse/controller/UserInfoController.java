package com.seu.kse.controller;

import com.seu.kse.service.impl.*;
import com.seu.kse.util.LogUtils;
import com.seu.kse.util.Utils;
import com.seu.kse.bean.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Created by yaosheng on 2017/5/22.
 */
@Controller
@RequestMapping("/")
public class UserInfoController {

    @Resource
    private PaperService paperService;

    @Resource
    private UserPaperService userPaperService;

    @Resource
    private UserTagService userTagService;

    @Resource
    private AuthorService authorService;
    @Resource
    private UserServiceImpl userService;


    @RequestMapping(method= RequestMethod.POST,value="/submitwriterPaper",produces="text/plain;charset=UTF-8")
    public @ResponseBody String submitwriterPaper(HttpServletRequest  request){
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            LogUtils.error(e.getMessage(),UserInfoController.class);
        }
        String uid = request.getParameter("uid");

        String paperTitle = request.getParameter("paper_title").trim();
        String paper_url =  request.getParameter("paper_url");
        int type = Integer.parseInt(request.getParameter("type"));
        //根据标题判断是否存在该论文
        Paper paper = paperService.getPaperByTitle(paperTitle);
        if(paper == null){
            // 通过标题和url 尝试爬取该论文
            // 插入论文
            Date now = new Date();
            paper = new Paper(paperTitle,paperTitle,"", 10, "",now , paperTitle, paper_url);
            if(paperService.insertPaper(paper)<=0){
                return "error";
            }
        }else{
            //更新论文url
            paper.setUrl(paper_url);
            if(paperService.updatePaper(paper)<=0){
                return "error";
            }
        }

        //更新 user_paper 表，全部设为5星
        UserPaperBehavior ub = new UserPaperBehavior();
        ub.setUid(uid);
        ub.setPid(paper.getId());
        ub.setInterest(5);
        Byte yes = 1;
        Byte no = 0;
        ub.setReaded(yes);
        if(type == 0){
            ub.setAuthor(no);
        }else{
            ub.setAuthor(yes);
        }
        if(userPaperService.insertObject(ub)<=0){
            return "error";
        }
        return "success";
    }
    @RequestMapping(method= RequestMethod.POST,value="/addTag",produces="text/plain;charset=UTF-8")
    public @ResponseBody String addTag(HttpServletRequest  request){
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            LogUtils.error(e.getMessage(),UserInfoController.class);
        }
        String uid = request.getParameter("uid");
        String[] tagNameList = request.getParameter("tags").split(",|，");
        int line = 0;
        for(String tagName : tagNameList){

            UserTagKey userAndTag = new UserTagKey(uid, tagName.trim());
            line = userTagService.insertUserAndTag(userAndTag);
        }
        if(line == 0) return "error";
        return "success";
    }
    @RequestMapping("/userinfo")
    public  String searchHisPaper( HttpSession session, Model model){
        //传递论文
        User login_user = Utils.testLogin(session,model);
        if(login_user == null) return "/login/login";
        String uid =login_user.getId();
        List<UserPaperBehavior> userAndPapers = userPaperService.getUserPaperByUID(uid);
        List<Paper> hisPapers = new ArrayList<Paper>();
        List<Paper> writerPapers = new ArrayList<Paper>();
        int limit = 20;
        int cnt = 0;
        for(UserPaperBehavior up : userAndPapers){
            if(cnt++ > limit){
                break;
            }
            String pid = up.getPid();
            Paper paper =paperService.searchPaper(pid);
            if(up.getReaded() == 1 && paper!=null){
                hisPapers.add(paper);
            }
            if(up.getAuthor() == 1&& paper!=null){
                writerPapers.add(paper);
            }
        }
        Map<String, List<Author>> hisAuthorMap = authorService.getAuthorForPapers(hisPapers);
        Map<String, List<Author>> wriAuthorMap = authorService.getAuthorForPapers(writerPapers);
        model.addAttribute("tag",0);
        model.addAttribute("hisPapers",hisPapers);
        model.addAttribute("writerPapers",writerPapers);
        model.addAttribute("hisAuthorMap",hisAuthorMap);
        model.addAttribute("wriAuthorMap",wriAuthorMap);
        return "/userinfo";
    }

    @RequestMapping(value="/updateUser",produces="text/plain;charset=UTF-8")
    public String updateUser(HttpServletRequest  request,  HttpSession session){
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            LogUtils.error(e.getMessage(),UserInfoController.class);
        }
        User user = new User();
        String uid =request.getParameter("uid");
        String uname = request.getParameter("username");
        int utype = Integer.parseInt(request.getParameter("usertype"));
        String url = request.getParameter("userurl");
        int pushNum = Integer.parseInt(request.getParameter("userpushnum"));
        User oldUser = userService.getUserByID(uid);
        if(oldUser!=null){
            user.setUname(uname);
            user.setUrl(url);
            user.setUtype(utype);
            user.setId(uid);
            user.setMailbox(oldUser.getMailbox());
            user.setUpassword(oldUser.getUpassword());
            user.setPushnum(pushNum);
            int line = userService.updateUser(user);
            if(line>0){
               //更新Session
                session.setAttribute("LOGIN_USER",user);
            }
        }

        return "redirect:/userinfo";
    }
}
