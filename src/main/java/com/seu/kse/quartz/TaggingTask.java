package com.seu.kse.quartz;

import com.seu.kse.service.impl.TaggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaggingTask {
    TaggingService taggingService;
    @Autowired
    public TaggingTask(TaggingService taggingService){
        this.taggingService = taggingService;
        //taggingService.init();
    }

    public void execute(){
        taggingService.update();
    }
    public void alltext(){
        taggingService.init();
    }

}
