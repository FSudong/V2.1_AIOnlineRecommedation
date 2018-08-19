package com.seu.kse.service.impl;

import com.seu.kse.bean.Paper;
import com.seu.kse.bean.PaperSims;
import com.seu.kse.dao.PaperMapper;
import com.seu.kse.service.recommender.ReccommendUtils;
import com.seu.kse.service.recommender.RecommenderCache;
import com.seu.kse.service.recommender.model.PaperSim;
import com.seu.kse.util.Constant;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by yaosheng on 2017/5/24.
 */

@Service("paperservice")
public class PaperService {

    @Resource
    PaperMapper paperdao;

    /**
     * 根据时间排序获取limit篇论文
     * @param
     * @return
     */
    public List<Paper> selectPaperByTime(int start,int end){
        List<Paper> papers=paperdao.selectPaperOrderByTime(start,end,10);
        return papers;
    }
    /**
    * @parament
    * @
    * @return 由时间和type共同排序
    **/
    public List<Paper> selectPaperByTimeSource(int start, int end){
        List<Paper> papers = paperdao.selectPaperOrderByTimeSource(start,end,10);
        return papers;
    }

    /**
     * 根据论文id查找相关论文
     */
    public Paper searchPaper(String id){
        Paper paper= paperdao.selectByPrimaryKey(id);
        return paper;
    }
    /**
     * 根据论文判断是否存在该论文
     */

    public Paper getPaperByTitle(String title){
        Paper paper = paperdao.selectByTitle(title);
        return paper;
    }
    public List<Paper> getPaperListByTitle(String title){
        List<Paper> papers = paperdao.selectPaperListByTitle(title);
        return papers;
    }

    /**
     * 插入论文
     */

    public int insertPaper(Paper paper){
        try{
            //查询是否存在
            Paper temp = paperdao.selectByPrimaryKey(paper.getId());
            if(temp == null){
                int line =  paperdao.insert(paper);
                return line;
            }
            return 0;
        }catch (Exception except){
            return 0;
        }
    }

    /**
     * 更新论文
     */

    public int updatePaper(Paper paper){
        return paperdao.updateByPrimaryKey(paper);
    }

    public List<Paper> getAllPaper(){
        return paperdao.selectAllPaper();
    }

    /**
     * TODO 从 neo4j中获取参考文献
     * @param id
     * @return
     */
    public List<Paper> getRefPaper(String id) {
        List<Paper> papers = new ArrayList<Paper>();
        return papers;
    }

    /**
     * 获取所有arxiv的论文
     * @return
     */
    public List<Paper> getArxivPapers(int pageNum, int limit){

        List<Paper> papers  = paperdao.selectAllArxivPaper();
        return papers;
    }

    public List<Paper> getTodayArxivPapers(int pageNum, int limit){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-1);
        Date yesterday = calendar.getTime();
        String yesterday_str = new SimpleDateFormat("yyyyMMdd").format(yesterday);
        List<Paper> papers = paperdao.selectTodayArxiv(pageNum, limit, Integer.parseInt(yesterday_str),0);
        return papers;
    }

    /**
     * 获取论文的k个相似论文
     * @param pid 论文id
     * @param k  最相似的k个
     * @return
     */
    public List<Paper> getSimPaper(String pid, int k){
//        List<Paper> res = new ArrayList<Paper>();
//        List<PaperSim> sims = new ArrayList<PaperSim>();
//        if(RecommenderCache.similarPaperList != null && RecommenderCache.similarPaperList.size()!=0){
//            sims = RecommenderCache.similarPaperList.get(pid);
//        }
//        if(sims!=null&&sims.size()!=0){
//            int minSize = Math.min(k,sims.size());
//            for(int i=0 ;i<minSize;i++){
//                res.add(paperdao.selectByPrimaryKey(sims.get(i).getPid()));
//            }
//        }

        return  calSimPaper(pid,k);
    }

    //使用 优先队列取 top k
    public List<Paper> getSimPaperByMatrix(String pid, int k){
        List<Paper> res = new ArrayList<Paper>();
        if(RecommenderCache.paperIDMapRowID.get(pid)==null){
            return res;
        }
        int index = RecommenderCache.paperIDMapRowID.get(pid);
        INDArray rowsVec = RecommenderCache.paperSimilarityMatrix.getRow(index);
        Queue<PaperSim> maxKPaper = new PriorityQueue<PaperSim>(k);
        for(int i=0; i<rowsVec.length(); i++){
            String simPID = RecommenderCache.rowIDMappaperID.get(i);
            if(pid.equals(simPID)) break;
            PaperSim paperSim = new PaperSim(RecommenderCache.rowIDMappaperID.get(i),rowsVec.getDouble(i));
            if(maxKPaper.size()<k){
                maxKPaper.add(paperSim);
            }else{
                PaperSim lowest = maxKPaper.peek();
                if(paperSim.compareTo(lowest)>0){
                    maxKPaper.poll();
                    maxKPaper.add(paperSim);
                }
            }
        }
        for(PaperSim p : maxKPaper){
            res.add(paperdao.selectByPrimaryKey(p.getPid()));
        }
        Collections.reverse(res);
        return  res;
    }

    public List<Paper> calSimPaper(String pid, int k){
        List<Paper> res = new ArrayList<Paper>();
        if(RecommenderCache.paperIDMapRowID.get(pid)==null){
            return res;
        }
        List<PaperSim> sims = new ArrayList<PaperSim>();
        Queue<PaperSim> maxKPaper = new PriorityQueue<PaperSim>(Constant.SIM_NUM);
        double[] p_vec = RecommenderCache.paperVecs.get(pid);
        for(Map.Entry<String, double[]> e2 : RecommenderCache.paperVecs.entrySet()){
            String pid2 = e2.getKey();
            if(pid.equals(pid2) || pid == pid2 ) continue;
            double sim = ReccommendUtils.cosinSimilarity(p_vec,e2.getValue());
            PaperSim paperSim = new PaperSim(pid2, sim);
            //sims.add(paperSim);
            if(maxKPaper.size()<Constant.SIM_NUM){
                maxKPaper.add(paperSim);
            }else{
                PaperSim lowest = maxKPaper.peek();
                if(paperSim.compareTo(lowest)>0){
                    maxKPaper.poll();
                    maxKPaper.add(paperSim);
                }
            }
        }
        sims.addAll(maxKPaper);
        Collections.reverse(sims);

        int minSize = Math.min(k,sims.size());
        for(int i=0 ;i<minSize;i++){

            res.add(paperdao.selectByPrimaryKey(sims.get(i).getPid()));
        }

        return  res;
    }
//    选出 start 和 end 之间的数据
    public List<Paper> selectLimitPaper(int start, int end){
        List<Paper> papers = paperdao.selectSomePaper(start,end);
        return papers;
    }
}
