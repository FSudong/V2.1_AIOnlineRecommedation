package com.seu.kse.service.recommender.model.CB;

import com.seu.kse.bean.*;
import com.seu.kse.dao.PaperMapper;
import com.seu.kse.dao.PaperTagMapper;
import com.seu.kse.dao.TagMapper;
import com.seu.kse.service.recommender.ReccommendUtils;
import com.seu.kse.service.recommender.RecommenderCache;
import com.seu.kse.service.recommender.feature.TFIDFProcessor;
import com.seu.kse.service.recommender.feature.Word2vecProcessor;
import com.seu.kse.service.recommender.model.PaperSim;
import com.seu.kse.service.retrieval.Retrieval;
import com.seu.kse.util.LogUtils;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by yaosheng on 2017/5/26.
 */
@Service
public class CBKNNModel {
    @Resource
    private PaperMapper paperDao;
    @Resource
    private TagMapper tagDao;
    @Resource
    private PaperTagMapper paperTagDao;

    public String getHeadFatherTag(String tagName){
        Tag tag = tagDao.selectByTagName(tagName);
        if(tag ==null || tag.getFathername() == null || tag.getFathername().equalsIgnoreCase("")|| tag.getTagname().equalsIgnoreCase(tag.getFathername())){
            System.out.println("getheadfathertag:" + tag.getTagname()+"::"+tag.getFathername());
        }
        if(tag.getFathername().equals("head")){
            return tag.getTagname();
        }
        return getHeadFatherTag(tag.getFathername());
    }


    public void init(boolean open, List<Paper> trainPapers, List<Paper> allPapers, int type){
        RecommenderCache.similarPaperList = new HashMap<String, List<PaperSim>>();

        if(open){
            if(type == 0){
                Word2vecProcessor.process(trainPapers);
            }else if(type == 1){
                TFIDFProcessor.process(trainPapers, allPapers);
            }else if(type == 2){
                TFIDFProcessor.processByMatrix(trainPapers);
            }

        }else{
            Word2vecProcessor.loadPaperSimModel();
        }
        LogUtils.info("CBKNNModel 初始化完成",CBKNNModel.class);

    }




    private List<PaperSim> userColdStart(List<Paper> newPapers){
        //针对冷启动用户
        //选择当日的论文

        List<PaperSim> res = new ArrayList<PaperSim>();
        for(Paper paper : newPapers){
            PaperSim sim = new PaperSim(paper.getId(),1);
            res.add(sim);
        }
        return res;
    }

    public void thingColdStart(){
        //1. 获取每日新产生的论文
        //2. 计算新产生论文和用户
    }


    public void modelByUser(User user, Set<Paper> papers , List<UserPaperBehavior> user_paper_behaves, List<Paper> newPapers){
        HashMap<String, double[]>  history = new HashMap<String, double[]>();
        HashMap<String, Integer> weight = new HashMap<String, Integer>();
        List<PaperSim> ranks = new ArrayList<PaperSim>();
        Queue<PaperSim> maxKPaper = new PriorityQueue<PaperSim>(user.getPushnum());
//        获取用户所有读过的文章id
        List<String> userPaperIds = new ArrayList<String>();
        for(UserPaperBehavior upb:user_paper_behaves){
            userPaperIds.add(upb.getPid());
        }
        Random random = new Random();
        if(user_paper_behaves != null && user_paper_behaves.size() !=0){
            //calculate the k-nearest paper for user

            for(UserPaperBehavior up : user_paper_behaves){
                int score = up.getInterest();
                String pid = up.getPid();
                double[] vec = RecommenderCache.paperVecs.get(pid);
//                注意为了降低计算复杂度 只将用户有评分和本身就存在向量的 文章转化为向量history中只存了这些文章
                if(vec!=null){
                    history.put(pid, vec);
                    weight.put(pid, score);
                }else if(score>0){
                    Paper paper = paperDao.selectByPrimaryKey(pid);
                    if(paper == null) continue;
                    String abs = paper.getPaperAbstract();
                    String title = paper.getTitle();
                    String content = abs +" "+title;
                    INDArray cur_vec = RecommenderCache.TFIDF.transform(content);
                    double [] vectors = new double[cur_vec.length()];
                    for(int k = 0 ; k<cur_vec.length(); k++){
                        vectors[k] = cur_vec.getDouble(k);
                    }
                    history.put(pid, vectors);
                    weight.put(pid, score);
                }
            }
            //计算其他论文和history论文中的相似度
            for(Paper paper : papers){
                String pid = paper.getId();
//                能不能得到向量还是一个问题
                double[] vec = RecommenderCache.paperVecs.get(pid);
//                if(vec == null) continue;
//                无向量就构造向量
                if(vec == null) {
                    Paper paperExistInMysql = paperDao.selectByPrimaryKey(pid);
                    if (paperExistInMysql == null) continue;
                    String abs = paperExistInMysql.getPaperAbstract();
                    String title = paperExistInMysql.getTitle();
                    String content = abs + " " + title;
                    INDArray cur_vec = RecommenderCache.TFIDF.transform(content);
                    vec = new double[cur_vec.length()];
                    for(int k = 0 ; k<cur_vec.length(); k++){
                        vec[k] = cur_vec.getDouble(k);
                    }
                }
                // cal every paper and history's paper sim
                double everySim = 0.0;
                boolean readed = false;
                int count = 0;
                for(Map.Entry<String, double[]> his : history.entrySet()){
                    if(history.containsKey(pid) || userPaperIds.contains(pid)) {
                        readed = true;
                        break;
                    }
                    double sim = ReccommendUtils.cosinSimilarity(his.getValue(),vec);

                    if(!Double.isNaN(sim)){
                        everySim = everySim + weight.get(his.getKey())* sim;
                    }

                    count++;
                }
                if(!readed){
                    if(count!=0){
                        everySim=everySim / count;
                    }
                    System.out.println("论文相似度："+everySim+" "+count+" "+user.getUname()+" "+paper.getTitle());
                    PaperSim paperSim = new PaperSim(pid, everySim);
                    if(maxKPaper.size() < user.getPushnum()){
                        maxKPaper.add(paperSim);
                    }else{
                        PaperSim lowest = maxKPaper.peek();
                        if(paperSim.compareTo(lowest)>0){
                            maxKPaper.poll();
                            maxKPaper.add(paperSim);
                        }
                    }
                }

            }
        }
        else{
            for(Paper paper : papers){
                int s = random.nextInt(5)%(5-1+1) + 1;
                PaperSim paperSim = new PaperSim(paper.getId(), s);

                maxKPaper.add(paperSim);
            }
        }
        ranks.addAll(maxKPaper);
        RecommenderCache.userRecommend.put(user.getMailbox(),ranks);
    }


    public Set<Paper> coarseRank(List<UserTagKey> userTags){
        Set<Paper> coarseRankRes = new HashSet<Paper>();
        LogUtils.info("粗排中...",this.getClass());
        for(UserTagKey tag : userTags){
            LogUtils.info(tag.getTagname()+"检索中",this.getClass());
            if(tag!=null&&tag.getTagname()!=null&&tag.getTagname().length()>=2){
                coarseRankRes.addAll(Retrieval.retrieval(tag.getTagname()));
            }
        }
        return coarseRankRes;
    }

    public Set<Paper> coarseRankLess(List<UserTagKey> userTags){
        Set<Paper> coarseRankRes = new HashSet<Paper>();
        LogUtils.info("粗排中...",this.getClass());
        for(UserTagKey tag : userTags){
            LogUtils.info(tag.getTagname()+"检索中",this.getClass());
            if(tag!=null&&tag.getTagname()!=null&&tag.getTagname().length()>=2){
                coarseRankRes.addAll(Retrieval.retrievalless(tag.getTagname()));
            }
        }
        return coarseRankRes;
    }

    public void model(List<Paper> papers, Map<String,List<UserPaperBehavior>> users_paper_behaves, List<User> users,
                      List<Paper> newPapers, Map<String, List<UserTagKey>> userTags, Map<String, List<UserTagKey>> usersHeadTag){
        //get the user's papers
        //1 . get all Users
        //2. get every user's papers
        //3. get the paper vec
        //4. cal knn paper for user
        LogUtils.info("计算用户推荐列表...",this.getClass());
        RecommenderCache.userRecommend = new HashMap<String, List<PaperSim>>();
        //获取所有paper 对应的顶级标签 2018-08-25 fsd
        Map<String,List<String>> papersHeadTags = new HashMap<String, List<String>>();
        for(Paper paper:papers){
            List<PaperTagKey> paperTagKeys = paperTagDao.selectByPID(paper.getId());
            List<String> paperHeadTags = new ArrayList<String>();
            for(PaperTagKey ptk:paperTagKeys){
                String paperHeadTag = getHeadFatherTag(ptk.getTagname());
                if(!paperHeadTags.contains(paperHeadTag)){
                    paperHeadTags.add(paperHeadTag);
                }
            }
            papersHeadTags.put(paper.getId(),paperHeadTags);
        }
        for(User user : users){
            //检索
            Set<Paper> candidatePapers = new HashSet<Paper>();
            List<UserTagKey> thisUserHeadTagList = new ArrayList<UserTagKey>();
            List<UserTagKey> thisUserTagList = new ArrayList<UserTagKey>();
            if(userTags!=null && userTags.size()!=0){
                thisUserTagList = userTags.get(user.getId());
            }
            if(usersHeadTag!=null && usersHeadTag.size()!=0){
                thisUserHeadTagList = usersHeadTag.get(user.getId());
            }

            //利用用户的标签粗选出一些文章
            if(userTags!=null&& userTags.size()!=0 && thisUserTagList !=null && thisUserTagList.size()!=0 ){
                //候选论文中 添加每个用户标签 ES搜索的前10篇文章
                candidatePapers.addAll(coarseRankLess(userTags.get(user.getId())));
            }
            //使用并查集选取新论文papers中在同一大分类下的论文
            if(usersHeadTag!=null && usersHeadTag.size()>0 && thisUserHeadTagList!=null && thisUserHeadTagList.size()!=0){
                List<String> thisUserHeadTagnameList = new ArrayList<String>();
                for(UserTagKey utk:thisUserHeadTagList){
                    thisUserHeadTagnameList.add(utk.getTagname());
                }
//                for(Paper paper:papers){
//
//                    try {
//                        List<PaperTagKey> paperTagKeys = paperTagDao.selectByPID(paper.getId());
//                        if(paperTagKeys==null || paperTagKeys.size()==0)
//                            continue;
//                        for(UserTagKey utk:thisUserHeadTagList){
//                            System.out.print("user:"+utk.getTagname()+";");
//                        }
//                        for(PaperTagKey ptk :paperTagKeys){
//                            System.out.print("paper:"+getHeadFatherTag(ptk.getTagname())+";");
//                        }
//                        System.out.println(" ");
//
////                        System.out.println(thisUserHeadTagList.get().toString()+"\n::::"+paperTagKeys.toString());
//                        for(PaperTagKey paperTagKey:paperTagKeys){
//                            String paperHeadTagname = getHeadFatherTag(paperTagKey.getTagname());
////                            UserTagKey userTagKey = new UserTagKey(user.getId(),paperHeadTag);
//                            if(thisUserHeadTagnameList.contains(paperHeadTagname)){
//                                candidatePapers.add(paper);
//                                break;
//                            }
//                        }
//                    }catch(Exception e){
//                        System.out.println(e);
//                    }
//                }
//                由paper顶级标签，选出用户标签切合的paper
                for(Paper paper:papers){
                    List<String> pheadtags = papersHeadTags.get(paper.getId());
                    if(pheadtags!=null && pheadtags.size()>0){
                        //若该paper有标签则 保存与用户有相同顶级标签的 文章
                        for(String pht:pheadtags){
                            if(thisUserHeadTagnameList.contains(pht)){
                                candidatePapers.add(paper);
                                break;
                            }
                        }
                    }else {
                        //若该paper无标签 不妨先加入候选集
                        candidatePapers.add(paper);
                    }
                }
            }
            //5为retrive中的limit的值 需要一致.若根本没有把今日的新论文加入到候选集中，则全部加入
            if(candidatePapers.size()<= thisUserHeadTagList.size()*5){
                candidatePapers.addAll(papers);
            }
            // get the k
            modelByUser(user, candidatePapers, users_paper_behaves.get(user.getId()), newPapers);
        }
        LogUtils.info("用户推荐列表计算完成",this.getClass());
    }

    public void modelByMatrix(List<Paper> papers, Map<String,List<UserPaperBehavior>> users_paper_behaves, List<User> users,
                              List<Paper> newPapers, Map<String, List<UserTagKey>> userTags){
        //get the user's papers
        //1 . get all Users
        //2. get every user's papers
        //3. get the paper vec
        //4. cal knn paper for user
        constructUserPaperMatrix(users,users_paper_behaves);
        LogUtils.info("计算用户推荐列表...",this.getClass());
        RecommenderCache.userRecommend = new HashMap<String, List<PaperSim>>();
        for(User user : users){
            //检索
            Set<Paper> candidatePapers = coarseRank(userTags.get(user.getId()));
            if(candidatePapers.size()==0){
                candidatePapers.addAll(papers);
            }
            // get the k
            modelByUserMatrix(user, candidatePapers, users_paper_behaves.get(user.getId()));
        }
        LogUtils.info("用户推荐列表计算完成",this.getClass());
    }

    public void constructUserPaperMatrix(List<User> users, Map<String,List<UserPaperBehavior>> users_paper_behaves){
        INDArray[] usersMatrixArray = new INDArray[users.size()];
        int i = 0 ;
        RecommenderCache.userMapRowID = new HashMap<String, Integer>();
        for(User user : users){
            RecommenderCache.userMapRowID.put(user.getId(), i);
            List<UserPaperBehavior> user_paper_behaves = users_paper_behaves.get(user.getUname());
            INDArray user_vec = Nd4j.zeros(RecommenderCache.paperMatrix.columns());
            if(user_paper_behaves!=null&&user_paper_behaves.size()>0){
                for(UserPaperBehavior ub : user_paper_behaves){
                    Integer row_id = RecommenderCache.paperIDMapRowID.get(ub.getPid());

                    if(row_id!=null){
                        user_vec.putScalar (row_id,ub.getInterest());
                    }
                }
            }
            usersMatrixArray[i] = user_vec;
            i++;
        }
        INDArray userMatrix = Nd4j.vstack(usersMatrixArray);
        RecommenderCache.userPaperSimilarityMatrix = userMatrix.mmul(RecommenderCache.paperMatrix.transpose());

    }

    public boolean isVisit(List<UserPaperBehavior> userPaperBehaviors, String pid){
        for(UserPaperBehavior ub : userPaperBehaviors){
            if(ub.getPid().equals(pid)) return true;
        }
        return false;
    }

    public void modelByUserMatrix(User user, Set<Paper> papers, List<UserPaperBehavior> userPaperBehaviors){
        int rowID = RecommenderCache.userMapRowID.get(user.getId());
        List<PaperSim> rank = new ArrayList<PaperSim>();
        Queue<PaperSim> maxKPaper = new PriorityQueue<PaperSim>(user.getPushnum());
        int num = 0;
        Random random = new Random();
        if(userPaperBehaviors.size()>0){

            for(Paper paper : papers){

                if(RecommenderCache.paperIDMapRowID.get(paper.getId())==null){
                    break;
                }
                if(isVisit(userPaperBehaviors,paper.getId())){
                    break;
                }
                int colID = RecommenderCache.paperIDMapRowID.get(paper.getId());
                PaperSim paperSim = new PaperSim(paper.getId(), RecommenderCache.userPaperSimilarityMatrix.getDouble(rowID,colID));
                if(num < user.getPushnum()){
                    maxKPaper.add(paperSim);
                    num++;
                }else{
                    PaperSim lowest = maxKPaper.peek();
                    if(paperSim.compareTo(lowest)>0){
                        maxKPaper.poll();
                        maxKPaper.add(paperSim);
                    }
                }
            }

        }else{
            for(Paper paper : papers){
                int s = random.nextInt(5)%(5-1+1) + 1;
                PaperSim paperSim = new PaperSim(paper.getId(), s);
                maxKPaper.add(paperSim);
            }
        }
        rank.addAll(maxKPaper);
        RecommenderCache.userRecommend.put(user.getId(), rank);
    }



}
