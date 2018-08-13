package com.seu.kse.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seu.kse.dao.PaperTagMapper;
import com.seu.kse.dao.UserPaperBehaviorMapper;
import com.seu.kse.dao.UserPaperQuestionMapper;
import com.seu.kse.service.impl.UserPaperService;
import com.seu.kse.util.LogUtils;
import com.seu.kse.util.Utils;
import com.seu.kse.bean.*;
import com.seu.kse.dao.UserPaperNoteMapper;
import com.seu.kse.service.impl.AuthorService;
import com.seu.kse.service.impl.PaperService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by yaosheng on 2017/6/1.
 */

@Controller
@RequestMapping("/")
public class PaperInfoController {

    @Resource
    private PaperService paperService;

    @Resource
    private AuthorService authorService;
    @Resource
    private UserPaperNoteMapper userPaperNoteDao;
    @Resource
    private UserPaperQuestionMapper userPaperQuestionDao;
    @Resource
    private UserPaperService userPaperService;
    @Resource
    private PaperTagMapper paperTagDao;
    /**
     * 对一篇论文写笔记
     * @param request 请求
     * @return 请求是否完成
     */
    @RequestMapping(method= RequestMethod.POST,value="/takenotes",produces="text/plain;charset=UTF-8")
    public @ResponseBody String takeNotesForPaper(HttpServletRequest request, HttpSession session, Model model){
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            LogUtils.error(e.getMessage(),PaperInfoController.class);
        }
        String pid = request.getParameter("pid");
        User login_user = Utils.testLogin(session,model);
        //String title = request.getParameter("notetitle");
        //String content = request.getParameter("notecontent");
        if (pid == null || login_user==null) return "error";

        UserPaperNoteWithBLOBs old = userPaperNoteDao.selectByPrimaryKey(new UserPaperNoteKey(login_user.getId(),pid));
        String author = request.getParameter("author");
        String author_place = request.getParameter("author_place");
        String paper_source =  request.getParameter("paper_source");
        String paper_keywords =  request.getParameter("paper_keywords");
        String problems =  request.getParameter("problems");
        String paper_tech =  request.getParameter("paper_tech");
        String paper_model = request.getParameter("paper_model");
        String related_word = request.getParameter("related_word");
        String paper_brief_comment =request.getParameter("paper_brief_comment");
        String paper_area = request.getParameter("paper_area");
        UserPaperNoteWithBLOBs upnote = new UserPaperNoteWithBLOBs(login_user.getId(),pid,
                paper_source,author,paper_source,paper_keywords,paper_tech,author_place,
                paper_brief_comment,paper_area,paper_model,problems,related_word,paper_brief_comment);
        if(old != null){
            //更新
            userPaperNoteDao.updateByPrimaryKey(upnote);
        }else{
            //插入笔记
            userPaperNoteDao.insert(upnote);
        }
        return "success";
    }

    /**
     * 展示论文
     * @param request 请求
     * @param session session
     * @param model 返回参数
     * @return 地址
     */
    @RequestMapping("/paperinfo")
    public String searchPaper(HttpServletRequest request, HttpSession session, Model model){
       /* if(!Utils.testConnect()){
            return "/index";
        }*/

        String id = request.getParameter("id");
        Paper paper = paperService.searchPaper(id);
        //System.out.println("URL: "+paper.getUrl());
        paper.setId(id);
        model.addAttribute("paper",paper);
        List<Author> authorsOfpaper = authorService.getAuthorsByPaper(id);
        if(authorsOfpaper==null){
            authorsOfpaper = new ArrayList<Author>();
        }
        if(authorsOfpaper.size()<=0){
            Author author = new Author();
            author.setAuthorname("未知");
            authorsOfpaper.add(author);
        }
        model.addAttribute("authors",authorsOfpaper);
//        User login_user = Utils.testLogin(session,model);
//        if(login_user!=null) {
//            Byte yes = 1;
//            Byte no = 0;
//           //UserPaperNoteKey keys = new UserPaperNoteKey(login_user.getId(),paper.getId());
//            UserPaperNoteKey key = new UserPaperNoteKey(login_user.getId(),paper.getId());
//            UserPaperNoteWithBLOBs paperNote = userPaperNoteDao.selectByPrimaryKey(key);
//            //System.out.println(paperNotes.size());
//            model.addAttribute("note",paperNote);
//            //修改用户-论文行为记录
//            UserPaperBehavior ub = new UserPaperBehavior();
//            ub.setUid(login_user.getId());
//            ub.setAuthor(no);
//            ub.setReaded(yes);
//            ub.setInterest(3);
//            ub.setPid(id);
//            userPaperService.insertObject(ub);
//        }
        //标签
        List<String> tags=new ArrayList<String>();
        //获取标签
        List<PaperTagKey> tagkey =paperTagDao.selectByPID(paper.getId());

        for(PaperTagKey t : tagkey){
            tags.add(t.getTagname());
        }
        model.addAttribute("tags", tags);
        Map<String, List<Author>> authorMap ;
        List<Paper> papers;
        if (paper.getPublisher().contains("arxiv")){
            papers=paperService.getSimPaper(paper.getId(),10);
        }else{
            papers= paperService.getRefPaper(paper.getId());
        }
        authorMap = authorService.getAuthorForPapers(papers);
        model.addAttribute("authorMap",authorMap);
        model.addAttribute("refPapers",papers);
        model.addAttribute("tag",0);



        return "/paperinfo";
    }
    /**
     * 查询笔记
     * @param request 请求
     * @return json
     */
    @RequestMapping(method= RequestMethod.GET,value="/searchnotes",produces="text/plain;charset=UTF-8")
    public @ResponseBody String searchNotesForPaper(HttpServletRequest request,HttpSession session, Model model){

        String id = request.getParameter("pid");
        User login_user = Utils.testLogin(session,model);
        if (id == null || login_user==null) return "error";
        UserPaperNoteKey keys = new UserPaperNoteKey();
        keys.setPid(id);
        keys.setUid(login_user.getId());
        List<UserPaperNoteWithBLOBs> paperNotes = userPaperNoteDao.selectByPrimaryPaperIDKey(id);
        System.out.println(paperNotes.size());
        JSONArray results = new JSONArray();
        for(UserPaperNoteWithBLOBs note:paperNotes){
            JSONObject json = new JSONObject();
            json.put("title",note.getContexturi());
            json.put("uid",note.getUid());
            json.put("pid",note.getPid());
            json.put("content",note.getContext());
            results.add(json);
        }
        return results.toJSONString();
    }
    /**
     * 对一篇论文进行提问
     * @param request 请求
     * @return 结果
     */
    @RequestMapping(method= RequestMethod.POST,value="/takequestions",produces="text/plain;charset=UTF-8")
    public @ResponseBody String takeQuestionsForPaper(HttpServletRequest request,HttpSession session, Model model){
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            LogUtils.error(e.getMessage(),PaperInfoController.class);
        }
        String id = request.getParameter("paperid");
        String title = request.getParameter("questiontitle");
        String content = request.getParameter("questioncontent");
        User login_user = Utils.testLogin(session,model);
        if (id == null || login_user==null) return "error";
        UserPaperQuestion upquestion = new UserPaperQuestion();
        upquestion.setPid(id);
        upquestion.setUid(login_user.getId());
        upquestion.setQuestion(content);
        userPaperQuestionDao.insert(upquestion);
        return "success";
    }
    /**
     * 点赞一篇论文
     * @param request 请求
     * @return 结果
     */
    @RequestMapping(method= RequestMethod.GET,value="/supportpaper",produces="text/plain;charset=UTF-8")
    public @ResponseBody String supportForOnePaper(HttpServletRequest request,HttpSession session, Model model){

        User login_user = Utils.testLogin(session,model);
        if(login_user == null) return "error";
        String id = request.getParameter("pid");

        int score = Integer.parseInt(request.getParameter("score"));
        String uid=login_user.getId();
        UserPaperBehaviorKey keys = new UserPaperBehaviorKey();
        keys.setPid(id);
        keys.setUid(uid);
        UserPaperBehavior user_papers = userPaperService.selectByKey(keys);
        if(user_papers == null){
            //insert this record into user_paper
            user_papers=new UserPaperBehavior();
            user_papers.setPid(id);
            user_papers.setUid(uid);
            Byte yes =1;
            Byte no = 0;
            user_papers.setAuthor(no);
            user_papers.setInterest(score);
            user_papers.setReaded(yes);
            userPaperService.insertObject(user_papers);
        }else{
            // update this record into user_paper
            user_papers.setInterest(score);
            userPaperService.updateByPrimaryKey(user_papers);
        }
        if(id!=null) return "success";
        return "error";
    }
}
