package com.seu.kse.service;

import com.seu.kse.bean.Author;
import com.seu.kse.bean.AuthorPaperKey;
import com.seu.kse.bean.Paper;
import com.seu.kse.dao.AuthorMapper;
import com.seu.kse.dao.AuthorPaperMapper;
import com.seu.kse.dao.PaperMapper;
import com.seu.kse.util.CommonFileUtil;
import com.seu.kse.util.Configuration;
import com.seu.kse.util.LogUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by Zcpwillam on 2017/5/24.
 * add by Yaosheng on 2017/12/4
 */

@Service
public class DataInjectService {



    private final AuthorMapper authorDao;


    private final AuthorPaperMapper authorPaperDao;


    private final PaperMapper paperDao;


    private static String path= Configuration.arxiv_path;


    public DataInjectService(AuthorMapper authorDao, AuthorPaperMapper authorPaperDao, PaperMapper paperDao){
        this.authorDao = authorDao;
        this.authorPaperDao = authorPaperDao;
        this.paperDao = paperDao;

    }
    public  void dataInject(){
        try{

            Date now = new Date();
            SimpleDateFormat sf = new SimpleDateFormat("yyyy_MM_dd");
            String date = sf.format(now);


            //date = "2017_10_16";

            File file=new File(path+"/"+date);

            if(!file.exists()) {
                return;
            }
            File[] list=file.listFiles();
            if(list==null) {
                LogUtils.info("无数据",DataInjectService.class);
                return;
            }
            for(File f:list)
                DataInjectByFile(f);

            LogUtils.info("开始导入数据！",DataInjectService.class);
            dataInject_init_pwzhdblp(Configuration.paperweekly_path, Configuration.zhihu_path, Configuration.nips_path);

        }catch (Exception e){
            System.out.println(e);
            LogUtils.error(e.getMessage(),DataInjectService.class);

        }

    }

    public void dataInject_init_pwzhdblp(String... sources){
        for(String source : sources) {
//            Calendar c = Calendar.getInstance();
//            Date now = c.getTime();
//            c.set(2018, Calendar.AUGUST, 16);
//
//            Date cur = c.getTime();
            Calendar c = Calendar.getInstance();
            Date now = c.getTime();
//       取前第三天的日期
            Date cur=new Date();//取时间
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(cur);
            calendar.add(calendar.DATE,0);//把日期往后增加一天.整数往后推,负数往前移动
            cur=calendar.getTime(); //这个时间就是日期往后推一天的结果
            int source_splits_length = source.split("/").length;
            String sourceName = source.split("/")[source_splits_length-2];
            while (cur.before(now)||cur.equals(now)) {
                try {
                    LogUtils.info("process cur :" + cur+sourceName, DataInjectService.class);

                    SimpleDateFormat sf = new SimpleDateFormat("yyyy_MM_dd");
                    String date = sf.format(cur);

                    File file = new File(source + "/" + date);
                    if (!file.exists()) {

                        LogUtils.info(date + "无数据", DataInjectService.class);
                        break;
                    }
                    File[] list = file.listFiles();
                    if (list == null) {
                        LogUtils.info("无数据", DataInjectService.class);
                        break;
                    }
//                    Integer i = 0;
                    for (File f : list){
//                        i = i+1;
//                        if(i <= 937) continue;
                        try{
                            DataInjectByFile__pwzhdblp(source,f);
                        }catch (Exception e){
                            LogUtils.info("插入"+f.toString()+"失败", DataInjectService.class);
                        }

                    }


                    LogUtils.info("导入 " + date+sourceName + " 数据", DataInjectService.class);


                } catch (Exception e) {
                    System.out.println(e);
                    LogUtils.error(e.getMessage(), DataInjectService.class);
                }
                c.add(Calendar.DATE, 1);
                cur = c.getTime();

            }
        }
    }

    public void dataInject_init(){


//       取前第三天的日期
        Date cur=new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(cur);
        calendar.add(calendar.DATE,0);//把日期往后增加一天.整数往后推,负数往前移动
        cur=calendar.getTime(); //这个时间就是日期往后推一天的结果
        Calendar c = Calendar.getInstance();
        Date now = c.getTime();
        while(cur.before(now)||cur.equals(now)){
            try{
                LogUtils.info("process cur :"+cur,DataInjectService.class);

                SimpleDateFormat sf = new SimpleDateFormat("yyyy_MM_dd");
                String date = sf.format(cur);

                File file=new File(path+"/"+date);
                if(!file.exists()) {

                    LogUtils.info(date+"无数据",DataInjectService.class);
                    break;
                }
                File[] list=file.listFiles();
                if(list==null){
                    LogUtils.info(date +"无数据",DataInjectService.class);
                    break;
                }
                for(File f:list)
                    DataInjectByFile(f);

                LogUtils.info("导入 "+date+" 数据",DataInjectService.class);


            }catch (Exception e){
                System.out.println(e);
                LogUtils.error(e.getMessage(),DataInjectService.class);
            }
            c.add(Calendar.DATE,1);
            cur =  c.getTime();

        }
//        dataInject_init_pwzhdblp(Configuration.zhihu_path,Configuration.paperweekly_path);
//        将pw_zh数据导入
        dataInject_init_pwzhdblp(Configuration.paperweekly_path, Configuration.zhihu_path, Configuration.nips_path);

    }


    private void DataInjectByFile__pwzhdblp(String source,File file){
        try{
            ArrayList<String> inputList= CommonFileUtil.read(file);
            if(inputList==null||inputList.isEmpty()){
                LogUtils.error("Empty File:"+file.getName(),DataInjectService.class);

            }else{
                GenerateDataRecord_pwzhdblp(source,inputList);
            }
        }catch (Exception e){
            System.out.println(e);
            LogUtils.error(e.getMessage(),DataInjectService.class);
            ArrayList<String> inputList= CommonFileUtil.read(file);
            if(inputList==null||inputList.isEmpty()){
                LogUtils.error("Empty File:"+file.getName(),DataInjectService.class);
            }else{
                GenerateDataRecord_pwzhdblp(source,inputList);
            }
        }
    }
    private void DataInjectByFile(File file){
        try{
            ArrayList<String> inputList= CommonFileUtil.read(file);
            if(inputList==null||inputList.isEmpty()){
                LogUtils.error("Empty File:"+file.getName(),DataInjectService.class);

            }else{
                GenerateDataRecord(inputList);
            }
        }catch (Exception e){
            System.out.println(e);
            LogUtils.error(e.getMessage(),DataInjectService.class);
            ArrayList<String> inputList= CommonFileUtil.read(file);
            if(inputList==null||inputList.isEmpty()){
                LogUtils.error("Empty File:"+file.getName(),DataInjectService.class);
            }else{
                GenerateDataRecord(inputList);
            }
        }

    }

    private  void GenerateDataRecord_pwzhdblp(String source,ArrayList<String> recordList){
        ArrayList<String> authorList=new ArrayList<String>();
        HashMap<String,String> paperInfo=new HashMap<String,String>();
        String paperName=recordList.get(0).split("\t")[0];
        String[] tempID = paperName.split("/");
        paperName = tempID[tempID.length-1];
        //如果为authors 就要存入list,否则就存储为字典
        for(String record:recordList){
            String[] temp=record.split("\t");
            if(temp[1].equals("authors"))
            {
                authorList.add(temp[2]);
            }
            else{
                paperInfo.put(temp[1],temp[2]);
            }
        }
        Paper paper=generatePaper_pwzhdblp(source,paperName,paperInfo);
        List<Author> authors=generateAuthorList_pwzhdblp(source,authorList);
        int i = insertPaperRecord_pwzhdblp(source,paper);

        List<Integer> aIdList=insertAuthorList(authors);
        if(aIdList!=null&&aIdList.size()>0){
            insertAuthorPaper(paperName,aIdList);
        }
    }


    private  void GenerateDataRecord(ArrayList<String> recordList){
        ArrayList<String> authorList=new ArrayList<String>();
        HashMap<String,String> paperInfo=new HashMap<String,String>();
        String paperName=recordList.get(0).split("\t")[0];
        String[] tempID = paperName.split("/");
        paperName = tempID[tempID.length-1];

        for(String record:recordList){
            String[] temp=record.split("\t");
            if(temp[1].equals("authors"))
            {
                authorList.add(temp[2]);
            }
            else{
                paperInfo.put(temp[1],temp[2]);
            }
        }
        Paper paper=generatePaper(paperName,paperInfo);
        List<Author> authors=generateAuthorList(authorList);
        int i = insertPaperRecord(paper);

        List<Integer> aIdList=insertAuthorList(authors);
        if(aIdList!=null&&aIdList.size()>0){
            insertAuthorPaper(paperName,aIdList);
        }
    }

    private static Paper generatePaper_pwzhdblp(String source, String paperName, Map<String,String> paperInfo){
        Paper paper=new Paper();
        paper.setId(paperName);
        if(paperInfo.containsKey("title"))
            paper.setTitle(paperInfo.get("title"));
        if(paperInfo.containsKey("abstract"))
            paper.setPaperAbstract(paperInfo.get("abstract"));
        if(paperInfo.containsKey("publisher"))
            paper.setPublisher(paperInfo.get("publisher"));
        if(paperInfo.containsKey("downlink")){
            paper.setUrl(paperInfo.get("downlink"));
        }
        if(paperInfo.containsKey("subjects")){
            paper.setKeywords(paperInfo.get("subjects"));
        }
        if(paperInfo.containsKey("time")){
            String time = paperInfo.get("time");
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date time_date = format.parse(time);
                paper.setTime(time_date);
            } catch (ParseException e) {
                System.out.println(e);
                Date time_date= new java.sql.Date(new Date().getTime());
                paper.setTime(time_date);
            }
        }
        if(source.equalsIgnoreCase(Configuration.zhihu_path)){

            paper.setType(2);
        }
        else if(source.equalsIgnoreCase(Configuration.paperweekly_path)){

            paper.setType(2);
        }else if(source.equalsIgnoreCase(Configuration.nips_path)){

            paper.setType(4);
        }


        return paper;
    }

    private static Paper generatePaper(String paperName, Map<String,String> paperInfo){
        Paper paper=new Paper();
        paper.setId(paperName);
        if(paperInfo.containsKey("title"))
            paper.setTitle(paperInfo.get("title"));
        if(paperInfo.containsKey("abstract"))
            paper.setPaperAbstract(paperInfo.get("abstract"));
        if(paperInfo.containsKey("publisher"))
            paper.setPublisher(paperInfo.get("publisher"));
        if(paperInfo.containsKey("downlink")){
            paper.setUrl(paperInfo.get("downlink"));
        }
        if(paperInfo.containsKey("subjects")){
            paper.setKeywords(paperInfo.get("subjects"));
        }
        if(paperInfo.containsKey("time")){
            String time = paperInfo.get("time");
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date time_date = format.parse(time);
                paper.setTime(time_date);
            } catch (ParseException e) {
                System.out.println(e);
                Date time_date= new java.sql.Date(new Date().getTime());
                paper.setTime(time_date);
            }
        }
        paper.setPublisher("arxiv");
        paper.setType(0);

        return paper;
    }

    private static List<Author> generateAuthorList_pwzhdblp(String source, ArrayList<String> authorList){
        List<Author> authors=new ArrayList<Author>();
        Author author;
        for(String a:authorList){
            String aurl = "https";
            author=new Author();
            author.setAuthorname(a);
            if(source.equalsIgnoreCase(Configuration.paperweekly_path)||source.equalsIgnoreCase(Configuration.zhihu_path)
                    ||source.equalsIgnoreCase(Configuration.nips_path)){
                author.setUrl(aurl);
            }
            authors.add(author);
        }
        return  authors;
    }

    private static List<Author> generateAuthorList(ArrayList<String> authorList){
        String author_add="https://arxiv.org";
        List<Author> authors=new ArrayList<Author>();
        Author author;
        for(String a:authorList){
            String[] sp = a.split(",");
            String aname = sp[0].trim();
            String aurl = sp[1].trim();
            author=new Author();
            author.setAuthorname(aname);
            author.setUrl(author_add+aurl);
            authors.add(author);
        }
        return  authors;
    }

    private  int  insertPaperRecord_pwzhdblp(String source, Paper p){
        //生成关键字

        //查询是否存在
        System.out.println(p.getId()+" "+p.getTitle());
        Paper temp = paperDao.selectByPrimaryKey(p.getId());
//        Paper temp2 = paperDao.selectByTitle(p.getTitle());
        Paper temp2;
        List<Paper> temp2s = paperDao.selectPaperListByTitle(p.getTitle());
        if(temp2s.size()>0){
            temp2 = temp2s.get(0);
        }else{
            temp2 = null;
        }
        if(temp == null&& temp2==null){
            int line =  paperDao.insert(p);
            LogUtils.info("insert paper1 normal "+p.getId()+"信息",DataInjectService.class);
            return line;
        }
        else if(temp!=null && temp2==null){
//            已改进 ：id重复了但 title不重复   方法：更改现在文章的id直接插入，插入失败就算了
            String newId = p.getId()+"dup";
            newId = newId.length()>60?newId.substring(newId.length()-59,newId.length()):newId;
            p.setId(newId);
            int line = paperDao.insert(p);
            LogUtils.info("insert paper2 dupId "+p.getId()+"信息",DataInjectService.class);
            return line;
        }else if(temp==null&&temp2!=null){
//             已改进：title 重复 id不重复 则id等保持不变，只变动source和type
            if(p.getType()>temp2.getType()){
                temp2.setType(p.getType());
                temp2.setPublisher(p.getPublisher());
            }
            temp2.setTime(p.getTime());
            int line = paperDao.updateByPrimaryKeySelective(temp2);
            LogUtils.info("update paper3 duptitle "+temp2.getId()+"信息",DataInjectService.class);
            return line;
        }else if(temp!=null&&temp2!=null){
            if(temp.getId().equals(temp2.getId())){
                //已改进：只更新type 和 source
                if(p.getType()>temp2.getType()){
                    temp2.setType(p.getType());
                    temp2.setPublisher(p.getPublisher());
                }
                temp2.setTime(p.getTime());
                int line = paperDao.updateByPrimaryKeySelective(temp2);
                LogUtils.info("update paper4 duptitleIdsame "+temp2.getId()+"信息",DataInjectService.class);
                return line;

            }else{
//                已改进:id重复的不动 题目重复的更新type和source
                if(p.getType()>temp2.getType()){
                    temp2.setType(p.getType());
                    temp2.setPublisher(p.getPublisher());
                }
                temp2.setTime(p.getTime());
                int line = paperDao.updateByPrimaryKeySelective(temp2);
                LogUtils.info("update paper5 duptitleIddiff"+temp2.getId()+"信息",DataInjectService.class);
                return line;
            }
        }
        return 0;

    }
    /**
     * 插入论文
     * @param p 论文p
     */
    private  int  insertPaperRecord(Paper p){
        //生成关键字

        //查询是否存在
        Paper temp = paperDao.selectByPrimaryKey(p.getId());
        if(temp == null){
            int line =  paperDao.insert(p);
            return line;
        }else{
            paperDao.updateByPrimaryKey(p);
            LogUtils.info("更新paper"+p.getId()+"信息",DataInjectService.class);
        }
        return 0;

    }


    private  List<Integer> insertAuthorList(List<Author> authorList){

        List<Integer> authorIdList=new ArrayList<Integer>();
        for(Author author:authorList){//根据作者姓名查询
            Integer authorId=authorDao.selectAidByAuthorName(author.getAuthorname());
            if(authorId==null){
                authorDao.insertSelective(author);
                authorId=authorDao.selectAidByAuthorName(author.getAuthorname());
            }
            authorIdList.add(authorId);
        }
        return authorIdList;
    }
    private  void insertAuthorPaper(String paperId,List<Integer> authorIdList){
        AuthorPaperKey apK=new AuthorPaperKey();
        apK.setPid(paperId);
        Integer count=authorPaperDao.countPid(apK.getPid());
        if(count>0){
            return;
        }
        for(Integer aId:authorIdList){
            apK.setAid(aId);
            int line = authorPaperDao.insertSelective(apK);
            if(line<=0) LogUtils.error("insert author meet error !",DataInjectService.class);
        }
    }
}
