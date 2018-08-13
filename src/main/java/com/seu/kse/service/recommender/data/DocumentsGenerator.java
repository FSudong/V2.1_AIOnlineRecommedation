package com.seu.kse.service.recommender.data;


import com.seu.kse.bean.Paper;
import org.deeplearning4j.text.documentiterator.DocumentIterator;
import org.deeplearning4j.text.documentiterator.FileDocumentIterator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class DocumentsGenerator implements DataSetGenerator{
    private String path;

    public DocumentsGenerator(String path){
        this.path = path;
    }
    //每一篇paper生成一个文档
    public DocumentIterator generate(List<Paper> papers){

        for(Paper paper : papers){
            String pid = paper.getId();
            String paperPath = path + System.getProperty("file.separator") + pid;
            File file = new File(paperPath);
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                bw.write(paper.getTitle()+".");
                bw.write(paper.getPaperAbstract()+"\n");
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileDocumentIterator documentIterator = new FileDocumentIterator(path);
        return documentIterator;
    }

}
