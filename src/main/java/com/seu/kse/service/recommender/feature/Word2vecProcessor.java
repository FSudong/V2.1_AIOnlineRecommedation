package com.seu.kse.service.recommender.feature;

import com.seu.kse.bean.Paper;
import com.seu.kse.service.recommender.model.CB.CBKNNModel;
import com.seu.kse.service.recommender.data.PaperDocument;
import com.seu.kse.service.recommender.RecommenderCache;
import com.seu.kse.service.recommender.model.PaperSim;
import com.seu.kse.util.Configuration;
import com.seu.kse.service.recommender.ReccommendUtils;
import com.seu.kse.util.LogUtils;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.stopwords.StopWords;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * Created by yaosheng on 2017/5/26.
 */


public class Word2vecProcessor {
    private static ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    private static String filepath = classloader.getResource(Configuration.sentencesFile).getPath();
    //private static Logger log = LoggerFactory.getLogger(Word2vecProcessor.class);
    public static void modelByWord2vce(){
        try {
            Word2Vec vec;
            SentenceIterator iter = new BasicLineIterator(filepath);
            // Split on white spaces in the line to get words
            TokenizerFactory t = new DefaultTokenizerFactory();

        /*
            CommonPreprocessor will apply the following regex to each token: [\d\.:,"'\(\)\[\]|/?!;]+
            So, effectively all numbers, punctuation symbols and some special symbols are stripped off.
            Additionally it forces lower case for all tokens.
         */
            t.setTokenPreProcessor(new CommonPreprocessor());
            //log.info("Building model....");

            LogUtils.info("Building word2vec model ...",Word2vecProcessor.class);
            vec = new Word2Vec.Builder()
                    .minWordFrequency(5)
                    .iterations(1)
                    .layerSize(Configuration.dimensions)
                    .seed(42)
                    .windowSize(5)
                    .iterate(iter)
                    .tokenizerFactory(t)
                    .stopWords(StopWords.getStopWords())
                    .build();


            LogUtils.info("Fitting Word2Vec model....",Word2vecProcessor.class);
            vec.fit();

            LogUtils.info("Writing word vectors to text file....",Word2vecProcessor.class);
            // Write word vectors to file
            URL url = Thread.currentThread().getContextClassLoader().getResource(Configuration.modelFile);
            if(url==null){
                String root_path =  Thread.currentThread().getContextClassLoader().getResource("/").getPath();
                File file = new File(root_path+"/"+Configuration.modelFile);
                file.createNewFile();
            }
            WordVectorSerializer.writeWord2VecModel(vec, Thread.currentThread().getContextClassLoader().getResource(Configuration.modelFile).getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LogUtils.error(e.getMessage(),Word2vecProcessor.class);
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.error(e.getMessage(),Word2vecProcessor.class);
        }

    }


    public static Word2Vec loadWord2VecModelFromText(){
        LogUtils.info("loading word2vec from Text File",Word2vecProcessor.class);
        Word2Vec vec=WordVectorSerializer.readWord2VecModel(Word2vecProcessor.class.getClassLoader().getResource(Configuration.modelFile).getPath());
        return vec;
    }

    public static void loadPaperVec(){
        try {
            FileInputStream fin = new FileInputStream(CBKNNModel.class.getClassLoader().getResource(Configuration.paper_vec).getPath());
            ObjectInputStream oin = new ObjectInputStream(fin);
            LogUtils.info("loading paper vector",Word2vecProcessor.class);

            RecommenderCache.paperVecs=(Map<String, double[]>) oin.readObject();

            LogUtils.info("loading paper vector complete",Word2vecProcessor.class);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LogUtils.error(e.getMessage(),Word2vecProcessor.class);
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.error(e.getMessage(),Word2vecProcessor.class);
        } catch (ClassNotFoundException e) {
            LogUtils.error(e.getMessage(),Word2vecProcessor.class);
            e.printStackTrace();
        }
    }

    public static void calPaperVec(List<Paper> papers){


            //获得每一篇论文的词表,按空格分词
            Word2DocByAve w2d = new Word2DocByAve();
            for(Paper paper : papers){
                String title = paper.getTitle();
                String paperAbstract = paper.getPaperAbstract();
                String[] words1 = ReccommendUtils.segmentation(title);
                String[] words2 = ReccommendUtils.segmentation(paperAbstract);
                int len = words1.length + words2.length;
                String[] words = new String[len];
                for(int i=0;i<words1.length;i++){
                    words[i] = words1[i];
                }for(int i=0;i<words2.length;i++){
                    words[i] = words2[i];
                }
                //根据 words 计算 paper向量
                double[] docVec = w2d.calDocVec(words);
                RecommenderCache.paperVecs.put(paper.getId(),docVec);
            }

            try{
                String root_path = Word2vecProcessor.class.getClassLoader().getResource("/").getPath();
                FileOutputStream fos = new FileOutputStream(root_path+"/"+Configuration.paper_vec);
                ObjectOutputStream os = new ObjectOutputStream(fos);
                os.writeObject(RecommenderCache.paperVecs);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        LogUtils.info("loading paper2vec model complete",Word2vecProcessor.class);

    }

    /**
     * 训练获得所有论文的相似论文，并持久化
     */
    public  static void trainSimPaper(List<Paper> papers)  {

        //URL url = CBKNNModel.class.getClassLoader().getResource(Configuration.Paper_Model_Path);
        ReccommendUtils.generateSimilarPaperList(papers.size());
        try {
            String root_path = CBKNNModel.class.getClassLoader().getResource("/").getPath();
            FileOutputStream fos = new FileOutputStream(root_path+"/"+Configuration.Paper_Model_Path);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(RecommenderCache.similarPaperList);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
    public static void loadPaperSimModel(){
        FileInputStream fin ;
        URL url = CBKNNModel.class.getClassLoader().getResource(Configuration.Paper_Model_Path);
        try {
            fin = new FileInputStream(url.getPath());
            ObjectInputStream oin = new ObjectInputStream(fin);
            LogUtils.info("读取 paper sims ",CBKNNModel.class);
            RecommenderCache.similarPaperList=(Map<String ,List<PaperSim>>) oin.readObject();
            LogUtils.info("paper sims 计算完成",CBKNNModel.class);
            fin.close();
        } catch (Exception e){

            LogUtils.error(e.getMessage(),CBKNNModel.class);
        }
    }


    public static void process(List<Paper> papers){
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        URL url_root = classloader.getResource("/");
        String filepath = url_root.getPath();
        PaperDocument.ToDocument(filepath+"/"+Configuration.sentencesFile,papers);
        Word2vecProcessor.modelByWord2vce();
        Word2vecProcessor.calPaperVec(papers);
        Word2vecProcessor.trainSimPaper(papers);
    }


}
