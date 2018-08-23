package com.seu.kse.controller;

import com.seu.kse.bean.*;
import com.seu.kse.dao.UserAuthorFocusMapper;
import com.seu.kse.dao.UserPaperBehaviorMapper;
import com.seu.kse.service.impl.AuthorService;
import com.seu.kse.service.impl.PaperService;
import com.seu.kse.service.impl.TaggingService;
import com.seu.kse.service.impl.UserPaperService;
import com.seu.kse.service.retrieval.Retrieval;
import com.seu.kse.util.Constant;
import com.seu.kse.util.LogUtils;
import com.seu.kse.util.Utils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

/**
 * Created by yaosheng on 2017/5/22.
 */

@Controller
@RequestMapping("/")
public class IndexController {


    @Resource
    private PaperService paperService;

    @Resource
    private AuthorService authorService;

    @Resource
    private UserPaperBehaviorMapper userPaperBehaviorDao;

    @Resource
    private UserAuthorFocusMapper userAuthorFocusDao;

    @Resource
    private UserPaperService userPaperService;

    @Resource
    private TaggingService taggingService;

//    @Resource
//    private UserPaperNoteMapper userPaperNoteDao;
    private int limit = 20;


    /**
     * 显示最近的论文，根据time字段排序
     * @param request 请求
     * @param model 返回参数
     * @return 地址
     */
    @RequestMapping("/")
    public String toIndex(HttpServletRequest request,HttpSession session, Model model){

        /*if(!Utils.testConnect()){
            return "/index";
        }*/

        User login = Utils.testLogin(session,model);
        int pageNum=0;
        if(request.getParameter("pageNum")!=null) {
            pageNum = Integer.parseInt(request.getParameter("pageNum"));
        }
//        List<Paper> papers=paperService.selectPaperByTime(pageNum*limit, (pageNum+1)*limit);
//        按照时间和类型排序
        List<Paper> papers = paperService.selectPaperByTimeSource(pageNum*limit, limit);
        model.addAttribute("papers",papers);
        model.addAttribute("previousPage",pageNum>0?(pageNum-1):pageNum);
        model.addAttribute("nextPage",pageNum+1);
        //获得作者
        Map<String, List<Author>> authorMap = authorService.getAuthorForPapers(papers);
        //获取收藏信息 默认得分为5
        Map<String,Boolean> startedMap = userPaperService.getUserPaperStarted(papers,login);

        model.addAttribute("startedMap",startedMap);
        model.addAttribute("authorMap",authorMap);
        model.addAttribute("limit",limit);
        model.addAttribute("paper_num",papers.size());
        model.addAttribute("tag",0);
        return "/index";
    }
    @RequestMapping("/index")
    public String processIndex(HttpServletRequest request,HttpSession session, Model model){
        return toIndex(request,session,model);
    }


    @RequestMapping("/tagpaper")
    public String tagPaper(HttpServletRequest request,HttpSession session, Model model){
        taggingService.init2();
        return "";
    }



    @Transactional
    @RequestMapping(value="/search",produces="text/plain;charset=UTF-8")
    public  String search(HttpServletRequest request, HttpSession session, Model model){
        String terms = request.getParameter("terms");
        if(terms == null || terms.trim().length() == 0){
            return "/index";
        }

        int pageNum=0;
        if(request.getParameter("pageNum")!=null) {
            pageNum = Integer.parseInt(request.getParameter("pageNum"));
        }
        TransportClient client = null;
        try {
            client = new PreBuiltTransportClient(Settings.EMPTY).
                    addTransportAddress(new TransportAddress(InetAddress.getByName("120.78.165.80"), 9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        MultiMatchQueryBuilder multiMatchQueryBuilder = new MultiMatchQueryBuilder(terms,Constant.ES_SEARCH_FIELDS);

        SearchResponse search_response = client.prepareSearch(Constant.ES_INDEX)
                .setTypes(Constant.ES_TYPE)
                .setSearchType(SearchType.QUERY_THEN_FETCH)
                .setQuery(multiMatchQueryBuilder)
                .setExplain(true).setFrom(pageNum*limit).setSize(limit).get();

        SearchHits hits = search_response.getHits();
        List<Paper> papers = Retrieval.getPapers(hits);

        //获取收藏信息 默认得分为5
        User login = Utils.testLogin(session,model);
        Map<String,Boolean> startedMap = userPaperService.getUserPaperStarted(papers,login);
        model.addAttribute("startedMap",startedMap);
        model.addAttribute("papers",papers);
        model.addAttribute("previousPage",pageNum>0?(pageNum-1):pageNum);
        model.addAttribute("nextPage",pageNum+1);
        //获得作者
        Map<String, List<Author>> authorMap = authorService.getAuthorForPapers(papers);
        model.addAttribute("authorMap",authorMap);
        model.addAttribute("limit",limit);
        model.addAttribute("paper_num",papers.size());
        model.addAttribute("tag",0);
        model.addAttribute("terms",terms);
        if(login!=null){
            LogUtils.info(Constant.SEARCH_LOG_KEYWORD+Constant.SEARCH_LOG_SPILT+login.getId()+Constant.SEARCH_TAG_SPILT+terms,IndexController.class);

        }else{
            LogUtils.info(Constant.SEARCH_LOG_KEYWORD+Constant.SEARCH_LOG_SPILT+Constant.VISIT_ID+Constant.SEARCH_TAG_SPILT+terms,IndexController.class);

        }

        return "/index";
    }


    public boolean isTag(String tag){
        return tag.length()>1;
    }


    @RequestMapping(method= RequestMethod.GET,value="/todayArxiv",produces="text/plain;charset=UTF-8")
    public String searchTodayArxiv(HttpServletRequest request, Model model){
        int pageNum=0;
        if(request.getParameter("pageNum")!=null) {
            pageNum = Integer.parseInt(request.getParameter("pageNum"));
        }
        List<Paper> papers = paperService.getTodayArxivPapers(pageNum*limit,limit);
        model.addAttribute("papers",papers);
        model.addAttribute("previousPage",pageNum>0?(pageNum-1):pageNum);
        model.addAttribute("nextPage",pageNum+1);
        //获得作者
        Map<String, List<Author>> authorMap = authorService.getAuthorForPapers(papers);
        model.addAttribute("authorMap",authorMap);
        model.addAttribute("limit",limit);
        model.addAttribute("paper_num",papers.size());
        model.addAttribute("tag",1);
        //System.out.println(papers.size());
        return "/index";
    }

    @RequestMapping(method= RequestMethod.GET,value="/recommender",produces="text/plain;charset=UTF-8")
    public  String recommender(HttpServletRequest request,HttpSession session, Model model){

        model.addAttribute("tag",2);
        return "/index";
    }


    /**
     *
     * @param request 请求
     * @return 返回布尔值
     */
    @RequestMapping(method= RequestMethod.GET,value="/testinterest",produces="text/plain;charset=UTF-8")
    public @ResponseBody String testInterest(HttpServletRequest request){

        String uid = request.getParameter("uid");
        int aid = Integer.parseInt(request.getParameter("aid"));
        UserAuthorFocusKey ub = userAuthorFocusDao.selectByPrimaryKey(new UserAuthorFocusKey(uid,aid));
        if(ub!=null ){
            return "true";
        }
        return "false";
    }
    /**
     *
     * @param request 请求
     * @return 返回兴趣
     */
    @RequestMapping(method= RequestMethod.GET,value="/testscoreonpaper",produces="text/plain;charset=UTF-8")
    public @ResponseBody String testScoreOnPaper(HttpServletRequest request){
//       url格式为http://localhost:8080/testscoreonpaper?uid=&pid=
        String uid = request.getParameter("uid");
        String pid = request.getParameter("pid");
        UserPaperBehaviorKey keys = new UserPaperBehaviorKey();
        keys.setPid(pid);
        keys.setUid(uid);
        UserPaperBehavior user_papers = userPaperBehaviorDao.selectByPrimaryKey(keys);
        if(user_papers==null)return "nothing";
        else return ""+user_papers.getInterest();
    }


    @RequestMapping("/author")
    public String showAuthor(HttpServletRequest request,HttpSession session, Model model){
//        if(!Utils.testConnect()){
//            return "/index";
//        }
        Utils.testLogin(session,model);
        int aid = Integer.valueOf(request.getParameter("id"));
        Author author = authorService.getAuthorByID(aid);
        model.addAttribute("author",author);

        List<Paper> papers=paperService.selectPaperByTime(0, 20);
        Map<String, List<Author>> authorMap = authorService.getAuthorForPapers(papers);

        model.addAttribute("authorMap",authorMap);
        List<Paper> refpapers = papers.subList(0,7);
        model.addAttribute("refPapers",refpapers);
        List<Paper> readedpapers = papers.subList(7,14);
        model.addAttribute("readedpapers",readedpapers);
        List<Paper> relatedpapers = papers.subList(14,20);
        model.addAttribute("relatedpapers",relatedpapers);
        return "/authorinfo";
    }


    /**
     * 关注某一用户
     * @param request 请求
     * @return 返回布尔值
     */
    @RequestMapping(method= RequestMethod.GET,value="/fouseonauthor",produces="text/plain;charset=UTF-8")
    public @ResponseBody String fouseOnAuthor(HttpServletRequest request,HttpSession session, Model model){
//        if(!Utils.testConnect()){
//            return "/index";
//        }
        String id = request.getParameter("aid");
        User login_user =Utils.testLogin(session,model);
        if(login_user == null || id == null) return "error";
        UserAuthorFocusKey key = new UserAuthorFocusKey(login_user.getId(), Integer.parseInt(id));
        UserAuthorFocusKey testkey = userAuthorFocusDao.selectByPrimaryKey(key);
        if(testkey==null){
            userAuthorFocusDao.insert(key);
            return "true";
        }else{
            userAuthorFocusDao.deleteByPrimaryKey(key);
            return "false";
        }
    }

    @RequestMapping("/arxiv")
    public String showArxivPapers(HttpServletRequest request, Model model){
//        if(!Utils.testConnect()){
//            return "/index";
//        }
        int pageNum =Integer.parseInt(request.getParameter("pageNum"));
        List<Paper> papers = paperService.getArxivPapers(pageNum,20);
        model.addAttribute("papers",papers);
        return "/index";
    }


}
