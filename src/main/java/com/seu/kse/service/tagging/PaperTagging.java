package com.seu.kse.service.tagging;

/**
 * Created by yaosheng on 2017/6/3.
 */


public class PaperTagging {
    /*
    ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring-mybatis.xml");
    PaperMapper paperDao = (PaperMapper) ac.getBean("paperMapper");
    TagMapper tagDao =(TagMapper) ac.getBean("tagMapper");

    PaperTagMapper paperTagMapper= (PaperTagMapper) ac.getBean("paperTagMapper");
    StopWords sw= new StopWords(Configuration.stopWords);
    CWSTagger seg ;
    private  void insertTagAndPaper(String pid,Map<String,Integer> tags){
        //插入标签表
        for(Map.Entry<String,Integer> e : tags.entrySet()){
            String tagName= e.getKey();
            Tag tag = new Tag();
            tag.setTagname(tagName);
            insertTag(tag);
            //插入论文-标签表
            PaperTagKey paperTagKey = new PaperTagKey();
            paperTagKey.setPid(pid);
            paperTagKey.setTagname(tagName);
            if(paperTagMapper.selectByPrimaryKey(paperTagKey)==null){
                paperTagMapper.insert(paperTagKey);
            }
        }
    }
    @Test
    public void extractKeyWords() throws LoadModelException {
        seg = new CWSTagger(Configuration.projectRoot+"/resources/models/seg.m");
        AbstractExtractor key = new WordExtract(seg,sw);
        List<Paper> papers = paperDao.selectAllPaper();
        for(Paper paper : papers){
            String title = paper.getTitle();
            String asbtract_paper = paper.getPaperAbstract();
            Map<String,Integer> tags_title = key.extract(title,2);
            //存储 title tag
            insertTagAndPaper(paper.getId(),tags_title);
            Map<String,Integer> tags_abs = key.extract(asbtract_paper,5);
            insertTagAndPaper(paper.getId(),tags_abs);
        }
    }




    public void insertTag(Tag tag){
        Tag tag1 = tagDao.selectByTagName(tag.getTagname());
        if(tag1 == null ){
            tagDao.insert(tag);
        }
    }
    */
}
