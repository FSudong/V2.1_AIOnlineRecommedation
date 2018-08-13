package com.seu.kse.service.impl;

import com.seu.kse.bean.Author;
import com.seu.kse.bean.AuthorPaperKey;
import com.seu.kse.bean.Paper;
import com.seu.kse.dao.AuthorMapper;
import com.seu.kse.dao.AuthorPaperMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yaosheng on 2017/5/25.
 */

@Service("authorService")
public class AuthorService {
    @Resource
    AuthorMapper authordao;
    @Resource
    AuthorPaperMapper authorPaperdao;

    public Author getAuthorByID(int id){
        Author author=authordao.selectByPrimaryKey(id);
        return  author;
    }

    public List<Author> getAuthorsByPaper(String paperID){
        List<AuthorPaperKey> aids = authorPaperdao.selectAuthorsByPaper(paperID);
        List<Author> authors = new ArrayList<Author>();
        if(aids!=null && aids.size()>0){
            for(AuthorPaperKey aid : aids){
                Author author = authordao.selectByPrimaryKey(aid.getAid());
                authors.add(author);
            }
        }
        if(authors.size()>3){
            authors = authors.subList(0,3);
        }
        return authors;
    }

    public Map<String,List<Author>> getAuthorForPapers(List<Paper> papers){
        Map<String, List<Author>> authorMap = new HashMap<String, List<Author>>();
        for(Paper paper : papers){
            //只返回第一作者
            String paperId = paper.getId();
            String paperAbstract = paper.getPaperAbstract();
            if(paperAbstract.length()>350){
                paper.setPaperAbstract(paperAbstract.substring(0,350)+"...");
            }

            List<Author>  authorsOfpaper = getAuthorsByPaper(paperId);

            paper.setId(paperId);

            if(authorsOfpaper!=null && authorsOfpaper.size()!=0){
                if(authorsOfpaper.size()>3){
                    authorMap.put(paper.getId(),authorsOfpaper.subList(0,3));
                }else{
                    authorMap.put(paper.getId(),authorsOfpaper);
                }
            }else{
                //为了保证一一对齐，没有作者的填充为未知
                Author author = new Author();
                author.setAuthorname("未知");
                authorsOfpaper = new ArrayList<Author>();
                authorsOfpaper.add(author);
                authorMap.put(paper.getId(),authorsOfpaper);
            }
        }
        return authorMap;
    }

}
