package com.seu.kse.service.recommender.feature;


import com.seu.kse.util.Configuration;
import com.seu.kse.service.recommender.ReccommendUtils;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

/**
 * Created by yaosheng on 2017/5/26.
 */
public class Word2DocByAve extends Word2Doc{
    private static Word2Vec vec;
    //StopWords sw= new StopWords(Configuration.stopWords);
    static{
        vec = Word2vecProcessor.loadWord2VecModelFromText();
    }
    public double[] calDocVec(String[] words) {
        double[] docVec = new double[Configuration.dimensions];
        INDArray docMatrix = Nd4j.zeros(Configuration.dimensions);
        int count =0 ;
        for(int i = 0; i<words.length;i++){
            String word = null;
            if(words[i]!=null)
                word = words[i].trim();

            double [] wordVec = null;
            INDArray wordMatrix = null;
            if(word != null ){

                wordVec = vec.getWordVector(word);
                wordMatrix = vec.getWordVectorMatrix(word);
            }
            if(wordVec!=null){
                docVec= ReccommendUtils.sumVecs(docVec,wordVec);
                docMatrix = docMatrix.add(wordMatrix);
                count++;
            }
        }
        docVec= ReccommendUtils.vecDiv(docVec, count);
        return docVec;
    }

    public INDArray calMatrixVec(String[] words) {

        INDArray docMatrix = Nd4j.zeros(Configuration.dimensions);
        int count =0 ;
        for(int i = 0; i<words.length;i++){
            String word = null;
            if(words[i]!=null)
                word = words[i].trim();
            INDArray wordMatrix = null;
            if(word != null && !word.equals("")){
                wordMatrix = vec.getWordVectorMatrix(word);
            }
            if(wordMatrix!=null){

                docMatrix = docMatrix.add(wordMatrix);
                count++;
            }
        }
        docMatrix = docMatrix.div(count);
        return docMatrix;
    }

    /*
    public  double[] calVecByKeyWords(String[] words){
        double[] docVec = new double[Configuration.dimensions];

        String content = "";
        for(String word : words){
            if(word!=null){
                content=content+" "+word;
            }
        }
        List<String> baseWords = ReccommendUtils.extractKeys(content);
        int count =0 ;
        for(int i = 0; i<baseWords.size();i++){
            String word = null;
            if(baseWords.get(i)!=null)
                word = baseWords.get(i).trim();

            double [] wordVec = null;
            if(word != null ){
                wordVec =vec.getWordVector(word);
            }
            if(wordVec!=null){
                docVec= ReccommendUtils.sumVecs(docVec,wordVec);
                count++;
            }
        }
        docVec= ReccommendUtils.vecDiv(docVec, count);
        return docVec;
    }
    */

}
