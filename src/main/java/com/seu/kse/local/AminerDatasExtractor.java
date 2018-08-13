package com.seu.kse.local;


import com.seu.kse.bean.Author;
import com.seu.kse.dao.AuthorMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.*;


public class AminerDatasExtractor {
    static ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring-mybatis.xml");
    static AuthorMapper authorDao = (AuthorMapper) ac.getBean("authorMapper");



    public static void addAuthorInformation(int type) throws IOException {
        String path = "";
        if(type==0){
            path = "F:/论文实验数据/DuplicatedAuthors.txt";
        }else{
            path = "F:/论文实验数据/NonDuplicatedAuthors.txt";
        }
        File file = new File(path);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = "";
        while(line!=null){
            String aid_str = br.readLine();
            if(aid_str==null || aid_str.length()==0){
                break;
            }
            aid_str = aid_str.split(":")[1].trim();
            int aid = Integer.parseInt(aid_str);
            String authorName = br.readLine().split(":")[1].trim();
            Byte atype = Byte.valueOf(br.readLine().split(":")[1].trim());
            String organization = br.readLine().split(":")[1].trim();
            String url = br.readLine().split(":")[1].trim();
            int publishNum = Integer.parseInt(br.readLine().split(":")[1].trim());
            int citationNum = Integer.parseInt(br.readLine().split(":")[1].trim());
            int Hindex = Integer.parseInt(br.readLine().split(":")[1].trim());
            double Pindex = Double.parseDouble(br.readLine().split(":")[1].trim());
            String researchInterests = br.readLine().split(":")[1].trim();
            int aminerIndex = Integer.parseInt(br.readLine().split(":")[1].trim());
            Author author = new Author();
            author.setAid(aid);
            author.setAuthorname(authorName);
            author.setAtype(atype);
            author.setOrganization(organization);
            author.setUrl(url);
            author.setPublishNum(publishNum);
            author.setCitationNum(citationNum);
            author.setHindex(Hindex);
            author.setPindex(Pindex);
            author.setResearchInterests(researchInterests);
            author.setAminerIndex(aminerIndex);
            if(type == 0){
                authorDao.updateByPrimaryKey(author);
            }else{
                authorDao.insert(author);
            }
            line = br.readLine();
        }
    }

    public static void main(String args[]) throws IOException {
        String path = "F:/论文实验数据/DuplicatedAuthors.txt";
        File file = new File(path);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = "";



        while(line!=null){

            int aid = Integer.parseInt(br.readLine().split(":")[1].trim());
            String authorName = br.readLine().split(":")[1].trim();
            Byte atype = Byte.valueOf(br.readLine().split(":")[1].trim());
            String organization = br.readLine().split(":")[1].trim();
            String url = br.readLine().split(":")[1].trim();
            int publishNum = Integer.parseInt(br.readLine().split(":")[1].trim());
            int citationNum = Integer.parseInt(br.readLine().split(":")[1].trim());
            int Hindex = Integer.parseInt(br.readLine().split(":")[1].trim());
            double Pindex = Double.parseDouble(br.readLine().split(":")[1].trim());
            String researchInterests = br.readLine().split(":")[1].trim();
            int aminerIndex = Integer.parseInt(br.readLine().split(":")[1].trim());
            Author author = new Author();
            author.setAid(aid);
            author.setAuthorname(authorName);
            author.setAtype(atype);
            author.setOrganization(organization);
            author.setUrl(url);
            author.setPublishNum(publishNum);
            author.setCitationNum(citationNum);
            author.setHindex(Hindex);
            author.setPindex(Pindex);
            author.setResearchInterests(researchInterests);
            author.setAminerIndex(aminerIndex);
            authorDao.updateByPrimaryKey(author);
            line = br.readLine();

        }
    }

}
